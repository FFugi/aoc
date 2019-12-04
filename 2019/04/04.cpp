#include <fstream>
#include <iostream>
#include <cstring>

using namespace std;


pair<bool, bool> checkPasswd(uint32_t passwd) {
    if (passwd > 999999) {
        return { false, false };
    }
    uint8_t digits[6];
    uint32_t cp = passwd;
    for (size_t i = 6; i > 0; i--) {
        digits[i - 1] = passwd % 10;
        passwd /= 10;
    }
    uint8_t occurences[10];
    memset(occurences, 0, 10 * sizeof(uint8_t));
    uint8_t prev = 0;
    for (auto& d : digits) {
        if (d < prev) {
            return { false, false };
        }
        occurences[d]++;
        prev = d;
    }
    bool hasDouble = false;
    bool hasDoubleEx = false;
    for (auto& d : occurences) {
        if (d >= 2) {
            hasDouble = true;
        }
        if (d == 2) {
            hasDoubleEx = true;
        }
    }

    return { hasDouble, hasDoubleEx };
}


int main(int argc, char **argv) {
    if (argc == 1) {
        return 1;
    }
    fstream input{};
    input.open(argv[1]);
    if (!input.good()) {
        cout << "Could not open file!" << endl;
        return 1;
    }
    uint32_t from, to;
    char c;
    input >> from >> c >> to;

    uint32_t partOne = 0;
    uint32_t partTwo = 0;
    for (uint32_t i = from; i <= to; i++) {
        partOne += checkPasswd(i).first ? 1 : 0;
        partTwo += checkPasswd(i).second ? 1 : 0;
    }

    cout << "Part one: " << partOne << endl;
    cout << "Part two: " << partTwo << endl;

    return 0;
}
