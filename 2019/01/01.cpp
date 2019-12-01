#include <iostream>
#include <fstream>
#include <string>

using namespace std;

int fuelForMass(int mass) {
    return max(0, mass / 3 - 2);
}

int fuelForFuel(int fuel) {
	int toAdd;
	int sum = 0;
	int fuelToCalc = fuel;
	do {
		toAdd = fuelForMass(fuelToCalc);
		sum += toAdd;
		fuelToCalc = toAdd;
	} while (toAdd > 0);
	return sum;
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

	int mass;
	int modulesFuel = 0;
	int fuelFuel = 0;
	while (input >> mass) {
		int toAdd = fuelForMass(mass);
		modulesFuel += toAdd;
		fuelFuel += fuelForFuel(toAdd);
	}
	cout << "Part one: " << modulesFuel << endl;
	cout << "Part two: " << modulesFuel + fuelFuel << endl;
	
    return 0;
}
