import sys
import re
import copy


def get_stacks(lines):
    def is_stack_line(line):
        splitted = line.split()
        if len(splitted) == 0:
            return False
        return splitted[0].startswith("[")
    stack_lines = [ l for l in lines if is_stack_line(l) ]
    stack_count = max([ len(l) for l in stack_lines ]) // 4 + 1

    stacks = [[] for _ in range(stack_count) ]

    for l in stack_lines:
        for match in re.finditer("\[.\]", l):
            pos = match.span()[0]
            ch = match.group(0)[1]
            idx = pos // 4
            stacks[idx].insert(0, ch)

    return stacks


def get_moves(lines):
    def prepare(line):
        splitted = line.split()
        return int(splitted[1]), int(splitted[3]) - 1, int(splitted[5]) - 1

    return [ prepare(l) for l in lines if l.startswith("move") ]


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = [ l.replace("\r", "").replace("\n", "") for l in f.readlines() ]
        return get_stacks(lines), get_moves(lines)


def doit(stacks, moves, action):
    for m in moves:
        num, src, dst = m
        action(stacks, num, src, dst)

    return "".join([ s[-1] for s in stacks ])


def main():
    stacks_one, moves = get_input()
    stacks_two = copy.deepcopy(stacks_one)

    def action_one(stacks, num, src, dst):
        for _ in range(num):
            ch = stacks[src].pop()
            stacks[dst].append(ch)

    def action_two(stacks, num, src, dst):
        to_pop = stacks[src][-num:]
        stacks[src] = stacks[src][:-num]
        stacks[dst].extend(to_pop)

    one = doit(stacks_one, moves, action_one)
    two = doit(stacks_two, moves, action_two)

    print(f"Part one: {one}")
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()
