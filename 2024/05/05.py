import re
import sys



def parse_rule(r):
    splitted = r.split("|")
    return (int(splitted[0]), int(splitted[1]))

def check_if_good(n1, n2, rule_tuples):
    for ot in rule_tuples:
        if n1 == ot[0] and n2 == ot[1]:
            return True
        
        if n1 == ot[1] and n2 == ot[0]:
            return False
        
    return True

def check_order_line(line, rule_tuples):
    for idx in range(len(line) - 1):
        n1 = line[idx]
        n2 = line[idx + 1]
        if not check_if_good(n1, n2, rule_tuples):
            return False
    
    return True


def fix_line(line, rule_tuples):
    line_good = False
    while not line_good:
        for idx in range(len(line) - 1):
            n1 = line[idx]
            n2 = line[idx + 1]
            if not check_if_good(n1, n2, rule_tuples):
                line[idx]     = n2
                line[idx + 1] = n1

        line_good = check_order_line(line, rule_tuples)


def main():
    with open(sys.argv[1]) as f:
        lines = f.readlines()

        rule_lines  = [l for l in lines if "|" in l]
        order_lines = [[int(n) for n in l.split(",")] for l in lines if "," in l]

        rule_tuples = [parse_rule(r) for r in rule_lines]

        num_set = set()

        for r in rule_tuples:
            num_set.add(r[0])
            num_set.add(r[1])

        num_list = list(num_set)

        lines_good = [ l for l in order_lines if     check_order_line(l, rule_tuples) ]
        lines_bad  = [ l for l in order_lines if not check_order_line(l, rule_tuples) ]

        good_mids = [ l[len(l) // 2] for l in lines_good ]

        bad_mids = []

        for bl in lines_bad:
            fix_line(bl, rule_tuples)
            mid_idx = len(bl) // 2
            bad_mids.append(bl[mid_idx])

        one = sum(good_mids)
        two = sum(bad_mids)
        print(f"part one: {one}")
        print(f"part two: {two}")


if __name__ == "__main__":
    main()