import sys


def part_one(elves):
    return elves[-1]


def part_two(elves):
    return elves[-1] + elves[-2] + elves[-3]


def main():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = [ l[0:-1] for l in f.readlines() ]
        elves = []

        current = []
        for l in lines:
            if len(l) == 0:
                elves.append(current)
                current = []
                continue
            num = int(l)
            current.append(num)

        
        sums = [ sum(e) for e in elves ]
        sums.sort()
        one = part_one(sums)
        two = part_two(sums)

        print(f"Part one: {one}")
        print(f"Part two: {two}")




if __name__ == "__main__":
    main()
