package main

import (
    "bufio"
    "fmt"
    "log"
    "os"
    "strconv"
    "strings"
)

type OperationType uint8

const (
	Toggle OperationType = iota
	TurnOn
	TurnOff
)

type Operation struct {
    opType OperationType
    coords [4]int
}

const LAMP_SIDE_COUNT = 1000

func get_lamp_idx(x, y int) int {
    return y * LAMP_SIDE_COUNT + x
}

func get_lamp_pos(idx int) (int, int) {
    x := idx % LAMP_SIDE_COUNT
    y := idx / LAMP_SIDE_COUNT
    return x, y
}


func main() {
    filename := os.Args[1]
    file, err := os.Open(filename)
    if err != nil {
        log.Fatal(err)
    }
    defer file.Close()

    lines := []string{}
    scanner := bufio.NewScanner(file)
    for scanner.Scan() {
        lines = append(lines, scanner.Text())
    }

    operations := make([]Operation, len(lines))

    for idx, line := range lines {
        if strings.HasPrefix(line, "toggle") {
            operations[idx].opType = Toggle
        } else if strings.HasPrefix(line, "turn on") {
            operations[idx].opType = TurnOn
        } else if strings.HasPrefix(line, "turn off") {
            operations[idx].opType = TurnOff
        }

        coordStr := strings.Replace(line, "toggle", "", -1)
        coordStr = strings.Replace(coordStr, "turn on", "", -1)
        coordStr = strings.Replace(coordStr, "turn off", "", -1)
        coordStr = strings.Replace(coordStr, "through", "", -1)
        coordStr = strings.Replace(coordStr, ",", " ", -1)

        splitted := strings.Fields(coordStr)
        for split_idx, numStr := range splitted {
            num, _ := strconv.Atoi(numStr)
            operations[idx].coords[split_idx] = num
        }
    }

    lamps_one := make([]bool, LAMP_SIDE_COUNT * LAMP_SIDE_COUNT)
    for _, op := range operations {
        x1 := op.coords[0]
        y1 := op.coords[1]
        x2 := op.coords[2]
        y2 := op.coords[3]

        for x := x1; x <= x2; x++ {
            for y := y1; y <= y2; y++ {
                idx := get_lamp_idx(x, y)
                val := lamps_one[idx]
                switch op.opType {
                case Toggle:
                    val = !val
                case TurnOn:
                    val = true
                case TurnOff:
                    val = false
                }
                lamps_one[idx] = val
            }
        }
    }

    turnedOn := 0
    for _, val := range lamps_one {
        if val {
            turnedOn++
        }
    }

    fmt.Printf("Part one: %d\n", turnedOn)

    lamps_two := make([]int, LAMP_SIDE_COUNT * LAMP_SIDE_COUNT)
    for _, op := range operations {
        x1 := op.coords[0]
        y1 := op.coords[1]
        x2 := op.coords[2]
        y2 := op.coords[3]

        for x := x1; x <= x2; x++ {
            for y := y1; y <= y2; y++ {
                idx := get_lamp_idx(x, y)
                val := lamps_two[idx]
                switch op.opType {
                case Toggle:
                    val += 2
                case TurnOn:
                    val++
                case TurnOff:
                    if val > 0 {
                        val--
                    }
                }
                lamps_two[idx] = val
            }
        }
    }

    brightness := 0
    for _, val := range lamps_two {
        brightness += val
    }

    fmt.Printf("Part two: %d\n", brightness)
}
