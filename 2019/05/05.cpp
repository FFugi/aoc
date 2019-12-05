#include <iostream>
#include <fstream>
#include <vector>
#include <cstdint>
#include <queue>

using namespace std;

using Code   = std::vector<int32_t>;
using Input  = std::queue<int32_t>;
using Output = std::queue<int32_t>;

enum Opcodes {
    add   = 1,
    mul   = 2,
    in    = 3,
    out   = 4,
    jmp_t = 5,
    jmp_f = 6,
    lt    = 7,
    eq    = 8
};

struct Opcode {
    uint8_t op = {};
    uint8_t mode[3] = {};
};

Opcode decodeOpcode(int32_t opcode) {
    Opcode decoded;
    decoded.op = opcode % 100;
    opcode /= 100;
    for (size_t i = 0; i < 3; i++) {
        decoded.mode[i] = opcode % 10;
        opcode /= 10;
    }
    return decoded;
}

bool exec(size_t& ip, Code& code, Input& input, Output& output) {
    if (ip >= code.size()) {
        return false;
    }
    Opcode opcode = decodeOpcode(code[ip]);
    if (opcode.op == mul || opcode.op == add) {
        if (ip + 3 >= code.size()) {
            return false;
        }
        int32_t op1 = code[ip + 1];
        int32_t op2 = code[ip + 2];
        int32_t res = code[ip + 3];
        if ( (opcode.mode[0] == 0 && op1 > code.size()) ||
             (opcode.mode[1] == 0 && op2 > code.size()) ||
             (opcode.mode[2] == 0 && res > code.size()))
        {
            return false;
        }
        int32_t val1 = opcode.mode[0] == 0 ? code[op1] : op1;
        int32_t val2 = opcode.mode[1] == 0 ? code[op2] : op2;
        int32_t result;
        if (opcode.op == add) {
              result = val1 + val2;    
        } else {
              result = val1 * val2;    
        }
        code[res] = result;
        ip += 4;
    } else if (opcode.op == in) {
        if (ip + 1 >= code.size()) {
            return false;
        }
        int32_t op = code[ip + 1];
        if ( (opcode.mode[0] == 0 && op > code.size()) ) {
            return false;
        }    
        int32_t toInput = input.front();
        input.pop();
        code[op] = toInput;
        ip += 2;
    } else if (opcode.op == out) {
        if (ip + 1 >= code.size()) {
            return false;
        }
        int32_t op = code[ip + 1];
        if ( (opcode.mode[0] == 0 && op > code.size()) ) {
            return false;
        }    
        int32_t val = opcode.mode[0] == 0 ? code[op] : op;
        output.push(val);
        ip += 2;
    } else if (opcode.op == jmp_t || opcode.op == jmp_f) {
        if (ip + 2 >= code.size()) {
            return false;
        }
        int32_t op1 = code[ip + 1];
        int32_t op2 = code[ip + 2];
        if ( (opcode.mode[0] == 0 && op1 > code.size()) ||
             (opcode.mode[1] == 0 && op2 > code.size()) )
        {
            return false;
        }    
        int32_t val1 = opcode.mode[0] == 0 ? code[op1] : op1;
        int32_t val2 = opcode.mode[1] == 0 ? code[op2] : op2;
        if ( (opcode.op == jmp_t && val1 != 0) || 
             (opcode.op == jmp_f && val1 == 0) )
        {
            ip = val2;    
        } else {
            ip += 3;
        }
    } else if (opcode.op == lt || opcode.op == eq) {
        if (ip + 3 >= code.size()) {
            return false;
        }
        int32_t op1 = code[ip + 1];
        int32_t op2 = code[ip + 2];
        int32_t res = code[ip + 3];
        if ( (opcode.mode[0] == 0 && op1 > code.size()) ||
             (opcode.mode[1] == 0 && op2 > code.size()) ||
             (opcode.mode[2] == 0 && res > code.size()))
        {
            return false;
        }
        int32_t val1 = opcode.mode[0] == 0 ? code[op1] : op1;
        int32_t val2 = opcode.mode[1] == 0 ? code[op2] : op2;
        int32_t result;
        if ( (opcode.op == lt && val1 < val2) ||
             (opcode.op == eq && val1 == val2) )
        {
            code[res] = 1;
        } else {
            code[res] = 0;
        }
        ip += 4;
    } else {
        return false;
    }
    return true;
}

void execCode(Code& code, Input& input, Output& output) {
    size_t ip = 0;
    while (exec(ip, code, input, output));
}

bool setProgram(Code& code, Code& ref) {
    if (code.size() != code.size()) {
        return false;
    }
    for (size_t i = 0; i < code.size(); i++) {
        code[i] = ref[i];
    }
    return true;
}

int32_t readResult(Output& output) {
    int32_t result;
    while (!output.empty()) {
        auto o = output.front();
        if (output.size() == 1) {
            result = o;
        }
        output.pop();
    }
    return result;
}

int main(int argc, char **argv) {
    if (argc == 1) {
        return 1;
    }
    fstream input;
    input.open(argv[1]);
    if (!input.good()) {
        cout << "Could not open file!" << endl;
        return 1;
    }
    Code baseCode;
    Code codeCode;
    int32_t num;
    char c;
    while (!input.eof()) {
        input >> num >> c;
        baseCode.push_back(num);
        codeCode.push_back(num);
    }

    Input programInput{};
    Output programOutput{};
    programInput.push(1);

    execCode(codeCode, programInput, programOutput);
    cout << "Part one: " << readResult(programOutput) << endl;

    while (!programInput.empty()) {
        programInput.pop();
    }
    programInput.push(5);
    setProgram(codeCode, baseCode);
    execCode(codeCode, programInput, programOutput);
    cout << "Part two: " << readResult(programOutput) << endl;

    return 0;
}
