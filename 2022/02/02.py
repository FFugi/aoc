import sys

from enum import Enum


class Shape(Enum):
    ROCK     = 1
    PAPER    = 2
    SCISSORS = 3

def beats(shape):
    if shape == Shape.ROCK:
        return Shape.SCISSORS
    if shape == Shape.SCISSORS:
        return Shape.PAPER
    if shape == Shape.PAPER:
        return Shape.ROCK


def gets_beated(shape):
    if shape == Shape.SCISSORS:
        return Shape.ROCK
    if shape == Shape.PAPER:
        return Shape.SCISSORS
    if shape == Shape.ROCK:
        return Shape.PAPER


def score_for_outcome(my_shape, their_shape):
    if my_shape == their_shape:
        return 3 
    if beats(my_shape) == their_shape:
        return 6
    return 0


def score_for_shape(shape):
    return shape.value


char_to_shape = {
        "A" : Shape.ROCK,
        "B" : Shape.PAPER,
        "C" : Shape.SCISSORS,
        "X" : Shape.ROCK,
        "Y" : Shape.PAPER,
        "Z" : Shape.SCISSORS,
    }


def part_one(games):
    games = [ [char_to_shape[g[0]], char_to_shape[g[1]]] for g in games ]
    result = sum( [ score_for_outcome(g[1], g[0]) + score_for_shape(g[1]) for g in games] )
    return result


def choose_shape(game_status, their_shape):
    if game_status == 'Y':
        return their_shape
    if game_status == 'X':
        return beats(their_shape)
    if game_status == 'Z':
        return gets_beated(their_shape)


def part_two(games):
    games = [ [ char_to_shape[g[0]], choose_shape(g[1], char_to_shape[g[0]]) ] for g in games ]

    result = sum( [ score_for_outcome(g[1], g[0]) + score_for_shape(g[1]) for g in games ] )
    return result


def main():
    filename = sys.argv[1]
    with open(filename, "r", encoding="utf-8") as f:
        lines = [ l[0:-1] for l in f.readlines() if len(l) > 1]

        games = [ [l[0], l[2]] for l in lines ]

        one = part_one(games)
        two = part_two(games)

        print(f"Part one: {one}")
        print(f"Part two: {two}")


if __name__ == "__main__":
    main()
