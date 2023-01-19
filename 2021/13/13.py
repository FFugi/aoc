import sys


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        lines = [ l.replace("\n", "").replace("\r", "") for l in lines ]
        return lines


def fold(dots: set[tuple[int, int]], fold: tuple[str, int]):
    axis = fold[0]
    axis_val = fold[1]

    dot_list = list(dots)
    for dot in dot_list:
        idx = 0 if axis == "x" else 1

        if dot[idx] > axis_val:
            dots.remove(dot)
            new_val = axis_val - (dot[idx] - axis_val)
            new_dot = (new_val, dot[1]) if axis == "x" else (dot[0], new_val)
            dots.add(new_dot)


def display(dots: set[tuple[int, int]]):
    max_x = 0
    max_y = 0
    for d in dots:
        max_x = d[0] if d[0] > max_x else max_x
        max_y = d[1] if d[1] > max_y else max_y

    for y in range(max_y + 1):
        for x in range(max_x + 1):
            val = "X" if (x, y) in dots else " "
            print(val, end="")
        print()


def main():
    input = get_input()

    folds = []
    dots = set()
    for l in input:
        if len(l) == 0:
            continue
        if l.startswith("fold along"):
            splitted = l.split("=")
            num = int(splitted[1])
            axis = splitted[0].split(" ")[-1]
            folds.append((axis, num))
        else:
            splitted = l.split(",")
            x = int(splitted[0])
            y = int(splitted[1])
            dots.add((x, y))

    fold(dots, folds[0])

    one = len(dots)
    print(f"Part one: {one}")

    for i, f in enumerate(folds):
        if i == 0:
            continue
        fold(dots, f)

    print(f"Part two:")
    display(dots)


if __name__ == "__main__":
    main()
