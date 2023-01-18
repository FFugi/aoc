import sys
import copy

class Counter:
    def __init__(self):
        self.path_count = 0


class Visited:
    def __init__(self):
        self.caves = set()
        self.small_twice = False


def is_big(cave: str) -> bool:
    return cave[0] >= 'A' and cave[0] <= 'Z'

def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        lines = [ l.replace("\n", "") for l in lines ]
        lines = [ l.replace("\r", "") for l in lines ]
        return lines


def visit(cave: str, connections: dict[str, list[str]], visited: Visited, context: Counter):
    if cave == "end":
        context.path_count += 1
        return

    if cave in visited.caves and not is_big(cave):
        return

    visited.caves.add(cave)

    connected_caves = connections[cave]
    for visit_candidate in connected_caves:
        visit(visit_candidate, connections, copy.deepcopy(visited), context)


def visit_2(cave: str, connections: dict[str, list[str]], visited: Visited, context: Counter):
    if cave == "end":
        context.path_count += 1
        return

    if cave == "start":
        if cave in visited.caves:
            return
    else:
        small = not is_big(cave)
        if small and cave in visited.caves and visited.small_twice:
            return
        if small and cave in visited.caves and not visited.small_twice:
            visited.small_twice = True

    visited.caves.add(cave)

    connected_caves = connections[cave]
    for visit_candidate in connected_caves:
        visit_2(visit_candidate, connections, copy.deepcopy(visited), context)


def main():
    input = get_input()

    connections = {}
    connection_list = [ l.split("-") for l in input ]

    def connect(c1: str, c2: str):
        if c1 not in connections.keys():
            connections[c1] = [ c2 ]
        else:
            connections[c1].append(c2)

    for c in connection_list:
        connect(c[0], c[1])
        connect(c[1], c[0])

    context = Counter()
    visited_map = Visited()
    visit("start", connections, visited_map, context)
    print(f"Part one: {context.path_count}")

    context = Counter()
    visited_map = Visited()
    visit_2("start", connections, visited_map, context)
    print(f"Part two: {context.path_count}")


if __name__ == "__main__":
    main()
