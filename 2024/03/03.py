import re
import sys


def handleline(line, ignoreDoDont):
    patternStr = "mul[(][0-9]{1,3},[0-9]{1,3}[)]" if ignoreDoDont else "mul[(][0-9]{1,3},[0-9]{1,3}[)]|do[(][)]|don't[(][)]"

    r = re.compile(patternStr)

    muls = r.findall(line)

    enabled = True
    sums = []
    for mul in muls:
        if not ignoreDoDont:
            if mul == "do()":
                enabled = True
                continue
            if mul == "don't()":
                enabled = False
                continue

            if not enabled:
                continue

        splitted = mul.split(',')

        intA = int(splitted[0][4:])
        intB = int(splitted[1][0:-1])

        res = intA * intB

        sums.append(res)

    return sum(sums)


def main():
    with open(sys.argv[1]) as f:
        lines = f.readlines()

        input = ""
        for l in lines:
            input += l

        one = handleline(input, True)
        two = handleline(input, False)
    
        print(f"part one: {one}")
        print(f"part two: {two}")


if __name__ == "__main__":
    main()
