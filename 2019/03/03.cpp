#include <fstream>
#include <iomanip>
#include <iostream>
#include <limits>
#include <map>
#include <set>

using namespace std;


using Position  = pair<int32_t, int32_t>;
using Positions = map<Position, uint8_t>;
using Steps     = map<Position, uint32_t>;
using Intersections = set<Position>;


void stepPos(Position& pos, char direction) {
    switch (direction) {
        case 'R':
            pos.first++;
            break;
        case 'L':
            pos.first--;
            break;
        case 'U':
            pos.second++;
            break;
        case 'D':
            pos.second--;
            break;
        default:
            cout << "unknown direction" << endl;
            break;
    }
}

void putWire(
        Positions& positions,
        string wire,
           int8_t wireNum,
        Intersections& intersections,
        Steps& steps)
{
    size_t curr_pos = 0;
    size_t prev_pos = 0;
    Position pos{ 0, 0 };
    uint32_t step = 0;
    while (curr_pos < wire.size()) {
        string code{};
        prev_pos = curr_pos;
        curr_pos = wire.find(',', curr_pos);
        if (curr_pos == string::npos) {
            code =  wire.substr(prev_pos, wire.size());
        } else {
            code = wire.substr(prev_pos, curr_pos - prev_pos);
            curr_pos += 1;
        }
        int32_t distacne = stoi(code.substr(1));
        char direction = code[0];
        for (uint32_t i = 0; i < distacne; i++) {
            step++;
            stepPos(pos, direction);
            if (positions.count(pos) != 0 &&
                positions.at(pos) != wireNum) 
            {
                intersections.insert(pos);
            }
            if (positions.count(pos) == 0) {
                positions[pos] = wireNum;
            } else {
                positions[pos] |= wireNum;
            }
            steps.insert({pos, steps.count(pos) == 0 ? step : steps.at(pos)});
        }
    }
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
    string wire1{};
    string wire2{};
    input >> wire1;
    input >> wire2;

    Positions positions{};
    Intersections intersections{};
    Steps steps1{};
    Steps steps2{};

    putWire(positions, wire1, 1, intersections, steps1);
    putWire(positions, wire2, 2, intersections, steps2);

    uint32_t distance = numeric_limits<uint32_t>::max();
    for (auto& i : intersections) {
        uint32_t iDist = abs(i.first) + abs(i.second);
        distance = min(distance, iDist);
    }
    cout << "Part one: " << distance << endl;

    uint32_t minSteps = numeric_limits<uint32_t>::max();
    for (auto& i : intersections) {
        uint32_t steps = steps1.at(i) + steps2.at(i);
        minSteps = min(minSteps, steps);
    }
    cout << "Part two: " << minSteps << endl;

    return 0;
}
