#include <iostream>
#include <fstream>
#include <vector>
#include <cstdint>

using namespace std;

using Code = std::vector<int32_t>;

bool exec(size_t& ip, Code& code, bool haltIfUnknown) {
    if (ip >= code.size()) {
        return false;
    }
    int32_t opcode = code[ip];
    if (opcode == 1 || opcode == 2) {
        if (ip + 3 >= code.size()) {
            return false;
        }
        int32_t op1 = code[code[ip + 1]];
        int32_t op2 = code[code[ip + 2]];
        int32_t resultAddr = code[ip + 3];
        int32_t result;
        if (opcode == 1) {
              result = op1 + op2;    
        } else {
              result = op1 * op2;    
        }
        code[resultAddr] = result;
        ip += 4;
    } else {
        if (haltIfUnknown) {
            return false;
        }
        ip++;
    }
    return true;
}

int32_t execCode(Code& code, int32_t noun, int32_t verb, bool halt) {
    code[1] = noun;
    code[2] = verb;
    size_t ip = 0;
    while (exec(ip, code, halt));
    return code[0];
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
    Code codeCode;
    Code baseCode;
    int32_t num;
    char c;
    while (!input.eof()) {
        input >> num >> c;
        codeCode.push_back(num);
        baseCode.push_back(num);
    }

    cout << "Part one: " << execCode(codeCode, 12, 2, true) << endl;

    for (int32_t n = 0; n <= 99; n++) {
        for (int32_t v = 0; v <= 99; v++) {
            setProgram(codeCode, baseCode);
            if (execCode(codeCode, n, v, true) == 19690720) {
                cout << "Part two: " << 100 * n + v << endl;
            }
        }
    }

    return 0;
}
