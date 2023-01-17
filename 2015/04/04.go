package main

import (
    "crypto/md5"
    "fmt"
    "os"
    "strings"
)

type verify_func func([16]byte) bool

func check(input_str string, verify verify_func) int {
    i := 0
    for {
        string_num := fmt.Sprintf("%s%d", input_str, i)
        bytes := []byte(string_num)
        sum := md5.Sum(bytes)

        if verify(sum) {
            break
        }
        i++
    }
    return i
}

func main() {
    filename := os.Args[1]
    dat, _ := os.ReadFile(filename)
    input_str := string(dat)

    input_str = strings.TrimRightFunc(input_str, func(c rune) bool {
        return c == '\r' || c == '\n'
    })

    verify_one := func(sum [16]byte) bool {
        if sum[0] == 0 && sum[1] == 0 && (sum[2] & 0xF0) == 0 {
            return true
        }
        return false
    }
    verify_two := func(sum [16]byte) bool {
        if sum[0] == 0 && sum[1] == 0 && sum[2] == 0 {
            return true
        }
        return false
    }
    one := check(input_str, verify_one)
    fmt.Printf("Part one: %d\n", one)

    two := check(input_str, verify_two)
    fmt.Printf("Part two: %d\n", two)
}
