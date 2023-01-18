package main

import (
    "bytes"
    "fmt"
    "os"
    "strings"
)

func lookAndSay(input string) string {
    var builder bytes.Buffer
    streakCount := 1
    for idx := 0;; idx++ {
        ch := input[idx]
        end := idx+1 == len(input)

        if end || input[idx + 1] != ch {
            builder.WriteString(fmt.Sprintf("%d%c", streakCount, ch))
            streakCount = 1
            if end {
                break
            }
        } else {
            streakCount++
        }
    }
    return builder.String()
}

func doIt(input string, num int) int {
    for i := 0; i < num; i++ {
        input = lookAndSay(input)
    }
    return len(input)
}

func main() {
    filename := os.Args[1]
    dat, _ := os.ReadFile(filename)
    input_str := string(dat)

    input_str = strings.TrimRightFunc(input_str, func(c rune) bool {
        return c == '\r' || c == '\n'
    })

    one := doIt(input_str, 40)
    fmt.Printf("Part one: %d\n", one)

    two := doIt(input_str, 50)
    fmt.Printf("Part two: %d\n", two)
}
