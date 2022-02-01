
import sys


def get_input():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = f.readlines()
        numbers = [ int(n) for n in lines[0].split(",") ]

        boards = []
        raw_boards = lines[1:]
        board = []

        for l in raw_boards:
            splitted = l.split()
            if len(splitted) == 0:
                if len(board) > 0:
                    boards.append(board)
                board = []
                continue
            board = board + [ int(n) for n in splitted ]
        
        return numbers, boards


def create_boards_map(boards):
    num_to_board_pos = {}

    for b_idx, board in enumerate(boards):
        for n_idx, num in enumerate(board):
            entry = (b_idx, n_idx)
            list_entry = num_to_board_pos[num] if num in num_to_board_pos else []
            list_entry.append(entry)
            num_to_board_pos[num] = list_entry

    return num_to_board_pos


def board_pos_to_coords(pos):
    y = pos // 5
    x = pos % 5
    return x, y


def coords_to_board_pos(x, y):
    return x + 5 * y


def check_board_col(board, col):
    for n in range(5):
        idx = coords_to_board_pos(col, n)
        if not board[idx]:
            return False
    return True


def check_board_row(board, row):
    for n in range(5):
        idx = coords_to_board_pos(n, row)
        if not board[idx]:
            return False
    return True


def check_board(board, inserted_idx):
    x, y = board_pos_to_coords(inserted_idx)

    return check_board_col(board, x) or check_board_row(board, y)


def calc_board_score(board, board_marks, idx_just_marked):
    board_score = 0
    board_zipped = list(zip(board, board_marks))
    for entry in board_zipped:
        entry_val = entry[0]
        marked    = entry[1]
        if not marked:
            board_score += entry_val

    return board_score * board[idx_just_marked]


def part_one(numbers, boards):
    boards_count = len(boards)

    boards_marked = [ [ False ] * 25 for _ in range(boards_count) ]
    num2board = create_boards_map(boards)

    for num in numbers:
        num_in_boards = num2board[num]
        for board_entry in num_in_boards:
            board_idx = board_entry[0]
            position  = board_entry[1]


            boards_marked[board_idx][position] = True
            result = check_board(boards_marked[board_idx], position)

            if result:
                return calc_board_score(boards[board_idx], boards_marked[board_idx], position)


def part_two(numbers, boards):
    boards_count = len(boards)

    boards_marked = [ [ False ] * 25 for _ in range(boards_count) ]
    num2board = create_boards_map(boards)

    wins = 0
    winner_boards = set()

    for num in numbers:
        num_in_boards = num2board[num]
        for board_entry in num_in_boards:
            board_idx = board_entry[0]
            position  = board_entry[1]

            if board_idx in winner_boards:
                continue

            boards_marked[board_idx][position] = True
            result = check_board(boards_marked[board_idx], position)

            if result:
                wins += 1
                winner_boards.add(board_idx)
                if wins == boards_count:
                    return calc_board_score(boards[board_idx], boards_marked[board_idx], position)


def main():
    numbers, boards = get_input()

    one = part_one(numbers, boards)
    two = part_two(numbers, boards)

    print(f"Part one: {one}")
    print(f"Part two: {two}")




if __name__ == "__main__":
    main()
