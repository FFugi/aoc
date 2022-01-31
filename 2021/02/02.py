
import sys


def parse_line(line):
    splitted = line.split()
    direction = splitted[0]
    val = int(splitted[1])
    return (direction, val)

def main():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        commands = [ parse_line(l) for l in lines ]

        # part one
        horizontal = 0
        depth = 0
        for c in commands:
            if c[0] == "forward":
                horizontal += c[1]
            elif c[0] == "down":
                depth += c[1]
            elif c[0] == "up":
                depth -= c[1]

        one = horizontal * depth

        #part two
        horizontal = 0
        depth = 0
        aim = 0
        for c in commands:
            if c[0] == "forward":
                horizontal += c[1]
                depth += aim * c[1]
            elif c[0] == "down":
                aim += c[1]
            elif c[0] == "up":
                aim -= c[1]

        two= horizontal * depth
        
        print(f"Part one: {one}")
        print(f"Part two: {two}")




if __name__ == "__main__":
    main()
