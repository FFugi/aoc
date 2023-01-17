package main

import (
    "fmt"
    "bufio"
    "os"
    "log"
    "strings"
    "strconv"
    "sort"
)

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

    if err := scanner.Err(); err != nil {
        log.Fatal(err)
    }

    dimensions := [][3]int{}
    for _, line := range lines {
        splitted := strings.Split(line, "x")
        dims := [3]int{}
        for i, v := range splitted {
            dims[i], err = strconv.Atoi(v)
            if err != nil {
                log.Fatal(err)
            }
        }

        dimensions = append(dimensions, dims)
    }

    sum := 0
    for _, dim := range dimensions {
        fields := [3]int{ dim[0] * dim[1], dim[1] * dim[2], dim[0] * dim[2]}
        sort.Ints(fields[:])
        min := fields[0]
        sum += 2 * (fields[0] + fields[1] + fields[2]) + min
    }

    one := sum
    fmt.Printf("Part one: %d\n", one)

    sum = 0
    for _, dim := range dimensions {
        volume := dim[0] * dim[1] * dim[2]
        sort.Ints(dim[:])

        sum += 2 * (dim[0] + dim[1]) + volume
    }

    two := sum
    fmt.Printf("Part two: %d", two)
}
