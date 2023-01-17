package main

import (
    "bufio"
    "fmt"
    "log"
    "os"
    "strings"
)

func contains_bad(line *string) bool {
    bad_seq := []string{"ab", "cd", "pq", "xy"}
    for _, bad := range bad_seq {
        if strings.Contains(*line, bad) {
            return true
        }
    }
    return false
}

func check_vovels(line *string) bool {
    exists := struct{}{}
    vovels := map[rune]struct{}{
        'a': exists,
        'e': exists,
        'i': exists,
        'o': exists,
        'u': exists,
    }
    sum := 0
    for _, ch := range *line {
        _, ok := vovels[ch]
        if (ok) {
            sum += 1
        }
        if (sum >= 3) {
            return true
        }
    }
    return false
}

func check_repetition(line *string) bool {
    prev := ([]rune(*line))[0]
    for _, current := range (*line)[1:] {
        if current == prev {
            return true
        }
        prev = current
    }
    return false
}


func check_two_non_overlapping(line *string) bool {
    var sequences = make(map[[2]byte]int)

    for idx := 0; idx < len(*line) - 1; idx++ {
        one := (*line)[idx]
        two := (*line)[idx + 1]
        sequence := [2]byte{one , two}
        val, ok := sequences[sequence]
        if ok {
            if val != idx - 1 {
                return true
            }
        } else {
            sequences[sequence] = idx
        }
    }
    return false
}


func check_between(line *string) bool {
    for idx := 1; idx < len(*line) - 1; idx++ {
        prev := (*line)[idx - 1]
        next := (*line)[idx + 1]
        if prev == next {
            return true
        }
    }
    return false
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

    nice_one := 0
    for _, line := range lines {
        if contains_bad(&line) {
            continue
        }
        if (check_repetition(&line) && check_vovels(&line)) {
            nice_one++
        }
    }
    fmt.Printf("Part one: %d\n", nice_one)

    nice_two := 0
    for _, line := range lines {
        if (check_between(&line) && check_two_non_overlapping(&line)) {
            nice_two++
        }
    }
    fmt.Printf("Part two: %d\n", nice_two)
}
