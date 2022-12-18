import sys


def get_char(s):
    return list(s)[0]


def get_priority(item):
    character = get_char(item)
    int_val = ord(character)
    uppercase_val = int_val - ord(get_char("A"))
    if uppercase_val <= 27:
        return uppercase_val + 27
    lowercase_val = int_val - ord(get_char("a"))
    return lowercase_val + 1


def part_one(rucksacks):
    common_items = []
    for r in rucksacks:
        sets = [set(), set()]
        for i in range(0, 2):
            for item in r[i]:
                sets[i].add(item)
        common_item = list(sets[0].intersection(sets[1]))[0]
        common_items.append(common_item)

    return sum([get_priority(i) for i in common_items])


def part_two(groups):
    common_items = []

    for g in groups:
        sets = [set(), set(), set()]
        for i, rucksack in enumerate(g):
            for item in rucksack:
                sets[i].add(item)
        common_item = list(sets[0].intersection(sets[1]).intersection(sets[2]))[0]
        common_items.append(common_item)

    return sum([get_priority(i) for i in common_items])


def main():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = [ l[0:-1] for l in f.readlines() if len(l) > 1]

        rucksacks = [ ( l[:len(l)//2], l[len(l)//2:] ) for l in lines ]
        groups = [ [ lines[i], lines[i + 1], lines[i + 2] ] for i in range(0, len(lines), 3) ]

        one = part_one(rucksacks)
        two = part_two(groups)

        print(f"Part one: {one}")
        print(f"Part two: {two}")


if __name__ == "__main__":
    main()

