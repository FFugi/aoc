import sys


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        return [ [ int(h) for h in l[:len(l) - 1] ] for l in lines ]


def map_dimensions(h_map):
    width = len(h_map[0])
    height = len(h_map)
    return width, height


def get_height(h_map, x, y):
    return h_map[y][x]


def get_neighbour_heights(h_map, x, y):
    width, height = map_dimensions(h_map)
    neighbours = []

    if x != 0:
        neighbours.append(h_map[y][x - 1])
    if x != width - 1:
        neighbours.append(h_map[y][x + 1])
    if y != 0:
        neighbours.append(h_map[y - 1][x])
    if y != height - 1:
        neighbours.append(h_map[y + 1][x])

    return neighbours


def get_lowest_locations(h_map):
    lowest_points = []
    for y, row in enumerate(h_map):
        for x, h in enumerate(row):
            neighbours = get_neighbour_heights(h_map, x, y)
            lowest = min(neighbours)
            if h < lowest:
                lowest_points.append( (x, y) )
    return lowest_points


def part_one(h_map):
    lowest_points = get_lowest_locations(h_map)
    return sum( [ get_height(h_map, pos[0], pos[1]) + 1 for pos in lowest_points ] )


def check_point(h_map, previous, point):
    width, height = map_dimensions(h_map)
    x, y = point
    p_x, p_y = previous
    
    if x < 0 or y < 0 or x >= width or y >= height:
        return False

    prev_height = get_height(h_map, p_x, p_y)
    height = get_height(h_map, x, y)

    return height < 9 and height > prev_height
    

def get_neighbours(point):
    x, y = point
    return [ (x - 1, y), (x + 1, y), (x, y - 1), (x, y + 1) ] 


def get_neighbourhood(point):
    return [ [ n, point ] for n in get_neighbours(point) ]


def calc_basin(h_map, point):
    x, y = point
    basin = set( [ point ] )

    height = get_height(h_map, x, y)
    
    to_see = get_neighbourhood(point)
    while len(to_see) > 0:
        to_check, prev = to_see.pop()
        if to_check in basin:
            continue
        if check_point(h_map, prev, to_check):
            basin.add(to_check)
            to_see += get_neighbourhood(to_check)

    return len(basin) 


def part_two(h_map):
    lowest_points = get_lowest_locations(h_map)
    sizes = sorted( [ calc_basin(h_map, point) for point in lowest_points ], reverse=True )

    return sizes[0] * sizes[1] * sizes[2]


def main():
    input = get_input()

    one = part_one(input)
    two = part_two(input)

    print(f"Part one: {one}")
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()

