import sys


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        timers = [ int(val) for val in lines[0].split(",") ]
        return timers


def get_new_fish(days, init):
    fish = []
    toadd = init
    while True:
        if toadd < days:
            fish.append(days - toadd)
        if toadd >= days:
            break
        toadd += 7
    return fish


def fish_produced(days, init):
    if days == 0:
        return 0
    result = (days + 6 - init) // 7
    if result < 0:
        result = 0
    return result


def calc(timers, days):
    cached_vals = {}

    for d in range(days + 1):
        for i in range(0,10):
            current = (d, i)
            a = fish_produced(d, i)
            if a == 0 or a == 1:
                cached_vals[current] = a
                continue
            fishes = [ (d, 9) for d in get_new_fish(d, i) ]
            val = len(fishes)
            for f in fishes:
                if f not in cached_vals:
                    a = fish_produced(f[0], f[1])
                    cached_vals[f] = a
                val += cached_vals[f]
            cached_vals[current] = val

    init_fish_count = len(timers)
    sum = init_fish_count
    for t in timers:
        cur = (days, t)
        sum += cached_vals[cur]

    return sum


def part_two(timers):
    return calc(timers, 256)


def part_one(timers):
    return calc(timers, 80)


def main():
    timers = get_input()

    one = part_one(timers)
    two = part_two(timers)

    print(f"Part one: {one}")
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()

