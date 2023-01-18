package main

import (
    "bufio"
    "fmt"
    "log"
    "os"
    "strings"
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
    for i, line := range lines {
        clear_line := strings.TrimRightFunc(line, func(c rune) bool {
            return c == '\r' || c == '\n'
        })
        lines[i] = clear_line
    }

    characters_diff := 0
    for _, line := range lines {
        // start with 2 as we have open and closing double quote
        characters_diff += 2
        for i := 1; i < len(line)-1; {
            if line[i] == '\\' {
                if line[i+1] == '\\' || line[i+1] == '"' {
                    characters_diff++
                    i += 2
                    continue
                }
                if line[i+1] == 'x' {
                    characters_diff += 3
                    i += 3
                    continue
                }
            }
            i++
        }
    }

    one := characters_diff
    fmt.Printf("Part one: %d\n", one)

    additional := 0
    for _, line := range lines {
        // start with 2 as we have open and closing double quote
        slash_count := strings.Count(line, "\\")
        quote_count := strings.Count(line, "\"")
        additional += 2 + slash_count + quote_count
    }

    two := additional
    fmt.Printf("Part two: %d\n", two)
}
