import sys


def contains(lhs, rhs):
    return lhs[0] >= rhs[0] and lhs[1] <= rhs[1]


def part_one(pairs):
    return sum([ 1 if contains(p[1], p[0]) or contains(p[0], p[1]) else 0 for p in pairs ])


def contains_any(lhs, rhs):
    if lhs[0] >= rhs[0] and lhs[0] <= rhs[1]:
        return True
    if lhs[1] >= rhs[0] and lhs[1] <= rhs[1]:
        return True
    return False


def part_two(pairs):
    return sum([ 1 if contains_any(p[1], p[0]) or contains_any(p[0], p[1]) else 0 for p in pairs ])


def str_to_range(s):
    splitted = s.split("-")
    start = int(splitted[0])
    end = int(splitted[1])
    return (start, end)


def main():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = [ l[0:-1] for l in f.readlines() if len(l) > 1]

        str_pairs = [ l.split(",") for l in lines ]

        pairs = [ (str_to_range(p[0]), str_to_range(p[1])) for p in str_pairs ]

        one = part_one(pairs)
        two = part_two(pairs)

        print(f"Part one: {one}")
        print(f"Part two: {two}")


if __name__ == "__main__":
    main()

