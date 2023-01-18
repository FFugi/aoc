package main

import (
    "bufio"
    "fmt"
    "log"
    "math"
    "os"
    "strconv"
    "strings"
)

type action func([]int)

func forAllPermutations(permutation []int, left, right int, f action) {
    if left == right {
        f(permutation)
    } else {
        for i := left; i <= right; i++ {
            permutation[left], permutation[i] = permutation[i], permutation[left]
            forAllPermutations(permutation, left+1, right, f)
            permutation[left], permutation[i] = permutation[i], permutation[left]
        }
    }
}

type Distance struct {
    endpoints [2]string
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
    exists := struct{}{}
    endPoints := make(map[string]struct{})
    distances := make(map[Distance]int)
    for _, line := range lines {
        splitted := strings.Fields(line)
        source := splitted[0]
        destination := splitted[2]
        distance_val, _ := strconv.Atoi(splitted[4])
        key_a := Distance{[2]string{source, destination}}
        key_b := Distance{[2]string{destination, source}}
        distances[key_a] = distance_val
        distances[key_b] = distance_val
        endPoints[source] = exists
        endPoints[destination] = exists
    }
    endPointsCount := len(endPoints)

    endPointArr := make([]string, endPointsCount)
    {
        idx := 0
        for endpoint := range endPoints {
            endPointArr[idx] = endpoint
            idx++
        }
    }

    minimalDistance := math.MaxInt
    maximalDistance := math.MinInt
    doit := func(permutation []int) {
        distance := 0
        for i := 1; i < len(permutation); i++ {
            idxSrc := permutation[i-1]
            idxDst := permutation[i]
            key := Distance{[2]string{endPointArr[idxSrc], endPointArr[idxDst]}}
            distance += distances[key]
        }
        if distance < minimalDistance {
            minimalDistance = distance
        }
        if distance > maximalDistance {
            maximalDistance = distance
        }
    }

    indexes := make([]int, endPointsCount)
    for i := range indexes {
        indexes[i] = i
    }

    forAllPermutations(indexes, 0, endPointsCount-1, doit)

    fmt.Printf("Part one: %d\n", minimalDistance)
    fmt.Printf("Part two: %d\n", maximalDistance)
}
