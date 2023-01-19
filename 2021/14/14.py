import sys
import itertools


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        lines = [ l.replace("\n", "").replace("\r", "") for l in lines ]
        return lines


def calc_occurences(polymer: str) -> dict[str, int]:
    letters = set(polymer)

    count = {}
    for l in letters:
        count[l] = polymer.count(l)

    return count


def get_possible_letters(polymer: str, rules: dict[str, str]) -> set[str]:
    letters = set(polymer)

    for k in rules.keys():
        letters.add(k[0])
        letters.add(k[1])
        val = rules[k]
        letters.add(val)
        letters.add(val)

    return letters


def calc(polymer: str, rules: dict[str, str], steps: int) -> str:
    letter_count = calc_occurences(polymer)
    letters_set = get_possible_letters(polymer, rules)

    combinations = list(itertools.combinations_with_replacement(letters_set, 2))
    l = len(combinations)

    for i in range(l):
        c = combinations[i]
        combinations.append((c[1], c[0]))

    pairs = {}
    for c in combinations:
        pairs[c] = 0

    for i in range(len(polymer) - 1):
        key = (polymer[i], polymer[i + 1])
        pairs[key] += 1

    for l in letters_set:
        if l not in letter_count:
            letter_count[l] = 0

    def add_to_dict(my_map, key, val):
        if key in my_map:
            my_map[key] += val
        else:
            my_map[key] = val

    for i in range(steps):
        to_add = {}
        to_delete = {}
        for pair, count in pairs.items():
            if count == 0:
                continue
            new_letter = rules[f"{pair[0]}{pair[1]}"]
            to_add_a = (pair[0], new_letter)
            to_add_b = (new_letter, pair[1])

            add_to_dict(to_delete, pair, count)
            add_to_dict(to_add, to_add_a, count)
            add_to_dict(to_add, to_add_b, count)

            letter_count[new_letter] += count
        for k, count in to_add.items():
            pairs[k] += count
        for k, count in to_delete.items():
            pairs[k] -= count

    counts = [c for _, c in letter_count.items()]
    return max(counts) - min(counts)


def main():
    input = get_input()

    polymer = input[0]

    rules = {}
    for r in input[2:]:
        splitted = r.split(" -> ")
        rules[splitted[0]] = splitted[1]

    one = calc(polymer, rules, 10)
    print(f"Part one: {one}")

    two = calc(polymer, rules, 40)
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()
