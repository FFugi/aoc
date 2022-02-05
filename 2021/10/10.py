import sys


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        return [ l[:len(l) - 1] for l in lines ]

matching = {
    "(": ")",
    "{": "}",
    "[": "]",
    "<": ">"
        }

opening = set( [ "(", "{", "<", "[" ] )

def part_one(lines):
    score_map = {
        ")": 3,
        "]": 57,
        "}": 1197,
        ">": 25137
    }

    corrupted_line_ids = []

    score = 0
    for idx, line in enumerate(lines):
        stack = []
        for ch in line:
            if ch in opening:
                stack.append(ch)
            else:
                opened = stack.pop()
                if matching[opened] != ch:
                    score += score_map[ch]
                    corrupted_line_ids.append(idx)
                    break

    return score, corrupted_line_ids


def part_two(lines):
    score_map = {
        ")": 1,
        "]": 2,
        "}": 3,
        ">": 4
    }

    scores = []
    for line in lines:
        stack = []
        for ch in line:
            if ch in opening:
                stack.append(ch)
            else:
                stack.pop()
        score = 0
        while len(stack) > 0:
            ch = stack.pop()
            score *= 5
            score += score_map[matching[ch]]
        scores.append(score)

    scores.sort()
    return scores[ len(scores) // 2 ]


def main():
    input = get_input()

    one, corrupted = part_one(input)
    two = part_two( [ l for idx, l in enumerate(input) if not idx in corrupted] )

    print(f"Part one: {one}")
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()

