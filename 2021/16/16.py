import sys
import functools


class MinRisk:
    def __init__(self, width, length):
        self.value = width * length * 9

def get_input() -> list[str]:
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        lines = [ l.replace("\n", "").replace("\r", "") for l in lines ]
        return lines[0]


def perform_operation(type: int, operands: list[int]) -> int:
    if type == 0:
        return sum(operands)
    elif type == 1:
        return functools.reduce((lambda x, y: x * y), operands)
    elif type == 2:
        return min(operands)
    elif type == 3:
        return max(operands)
    elif type == 5:
        return 1 if operands[0] > operands[1] else 0
    elif type == 6:
        return 1 if operands[0] < operands[1] else 0
    elif type == 7:
        return 1 if operands[0] == operands[1] else 0

    raise "should not get there!"


def parse_packet(input_bits: str, pos: int, versions: list[int]) -> int:
    VERSION_BIT = 0
    ID_BIT = 3
    version = int(input_bits[pos + VERSION_BIT:pos + VERSION_BIT + 3], 2)
    id = int(input_bits[pos + ID_BIT:pos + ID_BIT + 3], 2)
    versions.append(version)

    data_pos = pos + 6

    # literal
    if id == 4:
        i = data_pos
        parsed_bytes = []
        while True:
            val = int(input_bits[i + 1:i + 5], 2)
            parsed_bytes.append(val)
            is_last = input_bits[i] == "0"
            i += 5
            if is_last:
                break
        parsed_bytes.reverse()
        literal_val = 0
        for idx, val in enumerate(parsed_bytes):
            literal_val += 16 ** idx * val

        parsed = i - pos
        return i - pos, literal_val
    else:
        LEN_MODE_BIT = 6
        len_mode_bits = input_bits[pos + LEN_MODE_BIT] == "0"
        len_start_bit = pos + LEN_MODE_BIT + 1
        data_start_bit = len_start_bit + (15 if len_mode_bits else 11)
        len_val = int(input_bits[len_start_bit:data_start_bit], 2)
        parsed = 0

        vals = []
        if len_mode_bits:
            while parsed < len_val:
                p, val = parse_packet(input_bits, data_start_bit + parsed, versions)
                parsed += p
                vals.append(val)

        else:
            for i in range(len_val):
                p, val = parse_packet(input_bits, data_start_bit + parsed, versions)
                parsed += p
                vals.append(val)
        return data_start_bit - pos + parsed, perform_operation(id, vals)


def main():
    input_hex = get_input()
    input = "".join([ f"{int(c, 16):04b}" for c in input_hex])

    parsed = []
    _, two = parse_packet(input, 0, parsed)

    one = sum(parsed)
    print(f"Part one: {one}")
    print(f"Part two: {two}")


if __name__ == "__main__":
    main()
