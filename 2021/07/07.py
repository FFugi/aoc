import sys


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        numbers = [ int(val) for val in lines[0].split(",") ]
        return numbers 


def cost_two(distance):
    cost = 0
    for i in range(distance):
        cost += i + 1
    return cost


def cost_one(distance):
    return distance 


def calc(positions, calc_func):
    min_val = min(positions)
    max_val = max(positions)

    cache = {}

    costs = []
    for pos in range(min_val, max_val):
        cost_sum = 0
        for crab in positions:
            distance = abs(pos - crab)
            if distance in cache:
                cost_sum += cache[distance]
            else:
                cost = calc_func(distance)
                cost_sum += cost
                cache[distance] = cost
        costs.append(cost_sum)

    return min(costs)


def part_one(positions):
    return calc(positions, cost_one)


def part_two(positions):
    return calc(positions, cost_two)


def main():
    positions = get_input()

    one = part_one(positions)
    two = part_two(positions)

    print(f"Part one: {one}")
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()

