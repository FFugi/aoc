import sys
import copy


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        return [ [ int(h) for h in l[:len(l) - 1] ] for l in lines ]


def valid_boundaries(x, y):
    return not (x < 0 or y < 0 or x >= 10 or y >= 10)


def get_val(grid, x, y):
    return grid[y][x]


def inc_val(grid, x, y):
    grid[y][x] += 1


def reset_val(grid, x, y):
    grid[y][x] = 0


def get_neighbours(point):
    x, y = point
    points = [ 
        (x - 1, y - 1), 
        (x - 1, y    ), 
        (x - 1, y + 1), 
        (x,     y - 1), 
        (x,     y + 1), 
        (x + 1, y - 1), 
        (x + 1, y    ), 
        (x + 1, y + 1), 
      ]
    return [ v for v in points if valid_boundaries(v[0], v[1]) ]


def flashed(grid, x, y):
    return get_val(grid, x, y) > 9


def flash(grid, x, y):
    for p_x, p_y in get_neighbours((x, y)):
        if flashed(grid, p_x, p_y):
            continue
        inc_val(grid, p_x, p_y)
        if flashed(grid, p_x, p_y):
            flash(grid, p_x, p_y)


def clean_flashed(grid, x, y):
    if flashed(grid, x, y):
        reset_val(grid, x, y)


def for_each(grid, call):
    for y in range(10):
        for x in range(10):
            call(grid, x, y)


def disp(grid):
    for y in range(10):
        for x in range(10):
            print(get_val(grid, x, y), end='')
        print()
    print()


def step(grid):
    count = 0
    def calc_flashed(grid, x, y):
        nonlocal count
        if flashed(grid, x, y):
            count += 1

    flashed_points = []
    def locate_flashed(grid, x, y):
        nonlocal flashed_points
        if flashed(grid, x, y):
            flashed_points.append( (x, y ) )

    for_each(grid, inc_val)
    for_each(grid, locate_flashed)

    for x, y in flashed_points:
        flash(grid, x, y)

    for_each(grid, calc_flashed)
    for_each(grid, clean_flashed)

    disp(grid)

    return count


def part_one(grid):
    steps = 100
    count = 0
    
    for _ in range(steps):
        count += step(grid)

    return count


def part_two(grid):
    steps = 1

    while step(grid) != 100:
        steps += 1

    return steps


def main():
    grid = get_input()

    grid_2 = copy.deepcopy(grid)
    one = part_one(grid)
    two = part_two(grid_2)

    print(f"Part one: {one}")
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()

