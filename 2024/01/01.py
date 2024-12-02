
import sys


def splitted(line):
    s = line.split()
    return (int(s[0]), int(s[1]))


def main():
    with open(sys.argv[1]) as f:
        lines = f.readlines()

        pairs = [splitted(s) for s in lines]

        lhs = [p[0] for p in pairs]
        rhs = [p[1] for p in pairs]

        lhs.sort()
        rhs.sort()

        l = zip(lhs, rhs)

        diffs = sum([ abs(p[0] - p[1]) for p in l ])

        print(f"Part one: {diffs}")

        s = 0
        for n in lhs:
            s += n * rhs.count(n)

        print(f"Part two: {s}")


if __name__ == "__main__":
    main()

