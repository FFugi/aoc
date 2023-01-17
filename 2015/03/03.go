package main

import (
    "fmt"
     "os"
)

type coord struct {
    x int
    y int
}

type advance_func func(int, rune) coord

func advance(ch rune, position *coord) {
    if ch == '^' {
        position.y += 1
    } else if  ch == 'v' {
        position.y -= 1
    } else if ch == '<' {
        position.x -= 1
    } else if  ch == '>' {
        position.x += 1
    }
}

func iterate(moves string, adv advance_func) int {
    var positions = make(map[coord]struct{})
    exists := struct{}{}

    positions[coord{0, 0}] = exists
    for i, ch := range moves {
        current_pos := adv(i, ch)

        _, ok := positions[current_pos]
        if !ok {
            positions[current_pos] = exists
        }
    }

    return len(positions)
}

func main() {
    filename := os.Args[1]
    dat, _ := os.ReadFile(filename)
    moves := string(dat)

    position_one := coord{0, 0}
    advance_one := func(num int, ch rune) coord {
        advance(ch, &position_one)
        return position_one
    }

    position_a := coord{0, 0}
    position_b := coord{0, 0}
    advance_two := func(num int, ch rune) coord {
        var position *coord
        if num % 2 == 0 {
            position = &position_a
        } else {
            position = &position_b
        }
        advance(ch, position)

        return *position
    }

    one := iterate(moves, advance_one)
    fmt.Printf("Part one: %d\n", one)

    two := iterate(moves, advance_two)
    fmt.Printf("Part two: %d\n", two)
}
