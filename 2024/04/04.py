import re
import sys


def check_p_one(start_x, start_y, data, dirX, dirY, w, h):
    x = start_x
    y = start_y

    match_idx = 0

    match_str = "XMAS"

    for _ in range(len(match_str)):
        if x >= w or y >= h or x < 0 or y < 0:
            return False

        ch = data[y][x]

        if not ch == match_str[match_idx]:
            return False

        match_idx += 1
        x += dirX
        y += dirY

    return True

def check_p_two(x, y, data, w, h):
    if x + 1 >= w or y + 1 >= h or x - 1 < 0 or y - 1< 0:
        return False
    
    if data[y][x] != "A":
        return False

    cases = [
        "MSMS",
        "MMSS",
        "SMSM",
        "SSMM",
    ]

    chars = [
        data[y - 1][x - 1],
        data[y - 1][x + 1],
        data[y + 1][x - 1],
        data[y + 1][x + 1],
    ]

    for c in cases:
        if c == "".join(chars):
            return True

    return False


def main():
    with open(sys.argv[1]) as f:
        lines = f.readlines()

        h = len(lines)
        w = len(lines[0])

        directions = [
          ( 1,  0),
          (-1,  0),
          ( 0,  1),
          ( 0, -1),
          ( 1,  1),
          ( 1, -1),
          (-1, -1),
          (-1,  1)
        ]

        one = 0
        two = 0
        for y, line in enumerate(lines):
            for x, _ in enumerate(line):
                for d in directions:
                    if check_p_one(x, y, lines, d[0], d[1], w, h):
                        one += 1
    
                if check_p_two(x, y, lines, w, h):
                    print(f"two {x}, {y}")
                    two += 1
    
        print(f"part one: {one}")
        print(f"part two: {two}")


if __name__ == "__main__":
    main()