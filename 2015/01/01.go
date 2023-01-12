package main

import (
	"fmt"
 	"os"
 	"strings"
)

func main() {
	filename := os.Args[1]
	dat, _ := os.ReadFile(filename)
	str := string(dat)

	ups := strings.Count(str, "(")
	downs := strings.Count(str, ")")

	one := ups - downs
	fmt.Printf("Part one: %d\n", one)

	two := 0
	level := 0
	for i, letter := range str {
		if letter == '(' {
			level += 1
		} else {
			level -= 1
		}
		if level == -1 {
			two = i + 1
			break
		}
	}
	fmt.Printf("Part two: %d", two)
}
