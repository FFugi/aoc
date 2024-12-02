import sys


def is_increasing(l):
    for i in range(len(l) - 1):
        if l[i] >= l[i + 1]:
            return False
    return True


def is_decreasing(l):
    for i in range(len(l) - 1):
        if l[i] <= l[i + 1]:
            return False
    return True


def is_diff_ok(l):
    for i in range(len(l) - 1):
        diff = abs(l[i] - l[i + 1])

        diffOk = diff >= 1 and diff <= 3

        if not diffOk:
            return False
    return True


def is_safe(l):
    return (is_increasing(l) or is_decreasing(l)) and is_diff_ok(l)


def part_one(lines_int):
    safe = [ l for l in lines_int if is_safe(l) ]
    return len(safe)


def is_safe_considering_bad_signals(l):
    for i in range(len(l)):
        sublist = l[:i] + l[i + 1:]
        if is_safe(sublist):
            return True
    return False


def part_two(lines_int):
    unsafe = [ l for l in lines_int if not is_safe(l) ]

    unsafe_but_safe = [ l for l in unsafe if is_safe_considering_bad_signals(l)]

    return part_one(lines_int) + len(unsafe_but_safe)


def main():
    with open(sys.argv[1]) as f:
        lines_str = f.readlines()

        lines_int = [ [int(i) for i in l.split()] for l in lines_str ]

        one = part_one(lines_int)
        two = part_two(lines_int)

        print(f"part one: {one}")
        print(f"part two: {two}")


if __name__ == "__main__":
    main()