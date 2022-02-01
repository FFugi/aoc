
import sys


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        input = []
        for l in lines:
            point_splitted = l.split("->")
            if len(point_splitted) != 2:
                continue #eof

            points = [ p.split(",") for p in point_splitted ]
            points = points[0] + points[1]
            points = [ int(p) for p in points ]
            input.append( ((points[0], points[1]), (points[2], points[3])) )

        return input


def is_vertical(line):
    return line[0][0] == line[1][0]


def is_horizontal(line):
    return line[0][1] == line[1][1]


def update_points(points, point):
    val = 1
    if point in points:
        val = points[point] + 1
    points[point] = val
    return points


def part_one(lines):
    filtered = [ l for l in lines if is_horizontal(l) or is_vertical(l) ]
    return part_two(filtered)


def part_two(lines):
    points = {}

    for line in lines:
        start_x = line[0][0]
        start_y = line[0][1]
        end_x   = line[1][0]
        end_y   = line[1][1]

        step_x = 0
        step_y = 0
        if start_x > end_x:
            step_x = -1
        if start_x < end_x:
            step_x = 1
        if start_y > end_y:
            step_y = -1
        if start_y < end_y:
            step_y = 1

        end_x += step_x
        end_y += step_y

        x = start_x
        y = start_y
        while not (x == end_x and y == end_y):
            points = update_points(points, (x, y))
            x += step_x
            y += step_y
 
    result = 0
    for point, val in points.items():
        if val > 1:
            result += 1

    return result


def main():
    lines = get_input()

    one = part_one(lines)
    two = part_two(lines)

    print(f"Part one: {one}")
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()
