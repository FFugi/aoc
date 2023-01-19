import sys
import heapq


class MinRisk:
    def __init__(self, width, length):
        self.value = width * length * 9

def get_input() -> list[str]:
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        lines = [ l.replace("\n", "").replace("\r", "") for l in lines ]
        lines = [ [int(lx) for lx in ly] for ly in lines ]
        return lines


def get_possible_moves(curr_pos: tuple[int, int], max_dim: tuple[int, int]) -> list[tuple[int, int]]:
    moves = []
    if curr_pos[0] != max_dim[0]:
        moves.append((curr_pos[0] + 1, curr_pos[1]))
    if curr_pos[1] != max_dim[1]:
        moves.append((curr_pos[0], curr_pos[1] + 1))
    if curr_pos[0] != 0:
        moves.append((curr_pos[0] - 1, curr_pos[1]))
    if curr_pos[1] != 0:
        moves.append((curr_pos[0], curr_pos[1] - 1))
    return moves


def djikstra(caves: list[list[int]]) -> int:
    max_y = len(caves) - 1
    max_x = len(caves[0]) - 1

    inf_val = max_y * max_x * 9
    max_dim = (max_x, max_y)

    start = (0, 0)

    pq = []
    heapq.heappush(pq, (0, start))

    distances = {}
    for y in range(max_y + 1):
        for x in range(max_x + 1):
            distances[(x, y)] = inf_val

    distances[start] = 0

    curr_pos = start
    while len(pq) > 0:
        _, curr_pos = heapq.heappop(pq)
        neighbours = get_possible_moves(curr_pos, max_dim)
        for n in neighbours:
            dist = caves[n[0]][n[1]] + distances[curr_pos]
            if dist < distances[n]:
                distances[n] = dist
                heapq.heappush(pq, (dist, n))

    return distances[max_dim]


def prepare_part_two_map(caves: list[list[int]]) -> list[list[int]]:
    height = len(caves)
    width  = len(caves[0])

    new_width = width * 5
    new_height = height * 5

    new_map = []

    for y in range(new_height):
        row = []
        for x in range(new_width):
            orig_x = x % width
            orig_y = y % height
            orig_val = caves[orig_y][orig_x]

            tile_x = x // width
            tile_y = y // height
            to_add = tile_x + tile_y
            new_val = orig_val + to_add
            if new_val > 9:
                new_val = new_val % 10 + 1
            row.append(new_val)
        new_map.append(row)

    return new_map


def main():
    caves_one = get_input()

    one = djikstra(caves_one)
    print(f"Part one: {one}")

    caves_two = prepare_part_two_map(caves_one)

    two = djikstra(caves_two)
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()
