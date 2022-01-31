
import sys


def part_one(measurements):
    counter = 0
    prev = measurements[0]
    for idx, m in enumerate(measurements):
        if idx == 0:
            continue
        if m > prev:
            counter += 1
        prev = m
    return counter


def calc_three(measurements, idx):
    sum = 0
    for i in range(3):
        sum += measurements[idx + i]
    return sum


def part_two(measurements):
    counter = 0
    prev = 0
    iters = len(measurements) - 2
    for i in range(iters):
        val = calc_three(measurements, i)
        if i > 0 and val > prev:
            counter += 1
        prev = val
    return counter 


def main():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        measurements = [ int(n) for n in lines ]
        
        one = part_one(measurements)
        two = part_two(measurements)

        print(f"Part one: {one}")
        print(f"Part two: {two}")




if __name__ == "__main__":
    main()
