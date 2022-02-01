
import sys


def get_bits_per_line(lines):
    return len(lines[0]) - 1


def calc_stats(lines):
    bits_per_line = get_bits_per_line(lines)
    stats_zero = [ 0 for _ in range(bits_per_line) ]
    stats_one  = [ 0 for _ in range(bits_per_line) ]
    for l in lines:
        for idx, ch in enumerate(l):
            if ch == '0':
                stats_zero[idx] += 1
            if ch == '1':
                stats_one[idx] += 1

    return stats_one, stats_zero


def part_one(lines):
    bits_per_line = get_bits_per_line(lines)
    stats_one, stats_zero = calc_stats(lines)

    epsilon = ""
    gamma = ""
    for idx in range(bits_per_line):
        one  = stats_one[idx]
        zero = stats_zero[idx]
        if one > zero:
            epsilon += "1"
            gamma   += "0"
        else:
            epsilon += "0"
            gamma   += "1"

    epsilon = int(epsilon, base=2)
    gamma = int(gamma, base=2)

    return gamma * epsilon


def filter(lines, bit_to_preserve_init, preserve_most_common):
    bits_per_line = get_bits_per_line(lines)
    for idx in range(bits_per_line):
        filtered = []
        stats_one, stats_zero = calc_stats(lines)
        zero = stats_zero[idx]
        one  = stats_one[idx]
        bit_to_preserve = bit_to_preserve_init
        if zero > one:
            bit_to_preserve = "0" if preserve_most_common else "1"
        if one > zero:
            bit_to_preserve = "1" if preserve_most_common else "0"
        for l in lines:
            if l[idx] == bit_to_preserve:
                filtered.append(l)
        lines = filtered
        if len(lines) == 1:
            break

    return lines[0]

def part_two(lines):
    oxygen = filter(lines, "1", True)
    co2 = filter(lines, "0", False)

    print(oxygen)
    print(co2)

    oxygen = int(oxygen, base=2)
    co2 = int(co2, base=2)

    return co2 * oxygen


def main():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()

        one = part_one(lines)
        two = part_two(lines)

        print(f"Part one: {one}")
        print(f"Part two: {two}")




if __name__ == "__main__":
    main()
