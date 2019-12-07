#include <iostream>
#include <fstream>
#include <vector>
#include <set>
#include <map>

using namespace std;

struct Node {
    std::string id = {};
    std::vector<Node*> orbitees = {};
    Node* center = nullptr;

    uint32_t getOrbiteesCount(uint32_t depth) {
        uint32_t count = 0;
        for (auto& o : orbitees) {
            count += o->getOrbiteesCount(depth + 1);
        }
        return count + orbitees.size() * (depth + 1);
    }
};

void deleteNodes(Node* center) {
    for (auto& o : center->orbitees) {
        deleteNodes(o);
    }
    delete center;
}

void find(std::string target, Node* node, set<string>& visited, uint32_t orbits, uint32_t& result) {
    if (target == node->id) {
        result = orbits;
        return;
    }
    if (visited.count(node->id) > 0) {
       return;
    }
    visited.insert(node->id);
    if (node->center) {
        find(target, node->center, visited, orbits + 1, result);
    }
    for (auto& o : node->orbitees) {
        find(target, o, visited, orbits + 1, result);
    }
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

    map<string, Node*> nodes{};
    std::string centerId{};
    std::string orbittingId{};
    while (!input.eof()) {
        bool parsingCase = true;
        bool parsingCenter = true;
        while (parsingCase && !input.eof()) {
            char c;
            input.get(c);
            if (parsingCenter) {
                if (c != ')') {
                    centerId += c;
                } else {
                    parsingCenter = false;
                    input.get(c);
                }
            }
            if (!parsingCenter) {
                if (c != '\n') {
                    orbittingId += c;
                } else {
                    parsingCase = false;
                }
            }
        }
        if (input.eof()) {
            break;
        }

        Node* center  = nullptr;
        Node* orbitee = nullptr;
        if (nodes.count(centerId) > 0) {
            center = nodes.at(centerId);
        } else {
            center = new Node{};
            center->id = centerId;
            nodes[centerId] = center;
        }
        if (nodes.count(orbittingId) > 0) {
            orbitee = nodes.at(orbittingId);
        } else {
            orbitee = new Node{};
            orbitee->id = orbittingId;
            nodes[orbittingId] = orbitee;
        }
        orbitee->center = center;
        center->orbitees.push_back(orbitee);
        
        centerId    = "";
        orbittingId = "";
    }

    uint32_t totalOrbits = 0;
    uint32_t orbitsDiff = 0;
    if (nodes.count("COM")) {
        totalOrbits = nodes.at("COM")->getOrbiteesCount(0);
    }
    if (nodes.count("YOU")) {
        auto node = nodes.at("YOU");
        set<string> visited;
        find("SAN", node, visited, 0, orbitsDiff);
    }

    cout << "Part one: " << totalOrbits << endl;
    cout << "Part two: " << orbitsDiff - 2 << endl;

    deleteNodes(nodes.at("COM"));

    return 0;
}
