import sys


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = [ l.replace("\r", "").replace("\n", "") for l in f.readlines() ]
        return lines[0]


def calc(line, char_count):
    iters = len(line) - (char_count - 1)
    for idx in range(iters):
        sequence = line[idx:idx + char_count]
        if len(set(sequence)) == char_count:
            return idx + char_count

    return 0


def main():
    input = get_input()

    one = calc(input, 4)
    two = calc(input, 14)

    print(f"Part one: {one}")
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()
