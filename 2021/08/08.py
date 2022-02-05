import sys


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        def process(x):
            splitted = x.split("|")
            for i in range(2):
                a = splitted[i].split()
                splitted[i] = a
            return splitted
        input = list(map(process, lines))
        return input


def part_one(input):
    sum = 0
    for l in input:
        to_process = l[1]
        for digit in to_process:
            if len(digit) in [ 2, 3, 4, 7 ]:
                sum += 1
    return sum


def sort(entry):
    return [ "".join(sorted(v)) for v in entry ]


def process_entry(entry):
    to_process, displayed = entry

    to_process = sort(to_process)
    displayed = sort(displayed)

    known = {}
    digit_map = {}

    for digit in to_process:
        l = len(digit)
        if l == 2: # 1
            digit_map[1] = digit
            known[digit] = 1
        elif l == 3: # 7
            known[digit] = 7
        elif l == 4: # 4
            digit_map[4] = digit
            known[digit] = 4
        elif l == 7: # 8
            known[digit] = 8

    # segments which are a part of 1
    one_segments = [ s for s in digit_map[1] ] 

    # segments which are a part of 4 but not of 1
    four_segments = [ s for s in digit_map[4] if s not in one_segments ] 
    
    def process_criteria(list_to_process, criterias):
        for digit in list_to_process:
            as_four = 0
            as_one  = 0
            for segment in digit:
                if segment in one_segments:
                    as_one += 1
                if segment in four_segments:
                    as_four += 1
            for number, callback in criterias:
                if callback(as_one, as_four):
                    known[digit] = number

    len_6 = [ d for d in to_process if len(d) == 6 ]
    len_5 = [ d for d in to_process if len(d) == 5 ]

    criteria_6 = [
        ( 0, ( lambda one, four: four != 2 ) ),
        ( 6, ( lambda one, four: four == 2 and one == 1 ) ),
        ( 9, ( lambda one, four: four == 2 and one == 2 ) )
    ]
    criteria_5 = [
        ( 2, ( lambda one, four: four == 1 and one == 1 ) ),
        ( 3, ( lambda one, four: four == 1 and one == 2 ) ),
        ( 5, ( lambda one, four: four == 2 and one == 1 ) )
    ]

    process_criteria(len_6, criteria_6)
    process_criteria(len_5, criteria_5)

    displayed.reverse()
    val = 0
    for i, digit in enumerate(displayed):
        multiplier = pow(10, i)
        val += known[digit] * multiplier

    return val


def part_two(input):
    sum = 0
    for entry in input:
        val = process_entry(entry)
        sum += val
    return sum


def main():
    input = get_input()

    one = part_one(input)
    two = part_two(input)

    print(f"Part one: {one}")
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()

