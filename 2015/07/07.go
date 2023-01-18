package main

import (
    "bufio"
    "fmt"
    "log"
    "os"
    "regexp"
    "strconv"
    "strings"
)

type GateType uint8
type WireType uint8

const (
    NOT GateType = iota
    AND
    OR
    LSHIFT
    RSHIFT
)

const (
    WIRE WireType = iota
    GATE
    IMM
)

type Gate struct {
    gateType GateType
    inputs   [2]Input
}

type Input struct {
    isImm    bool
    immValue uint16
    wire     string
}

type Wire struct {
    wireType WireType
    gate     *Gate
    immValue uint16
    wire     string
}

func get_immediate(op *string) *uint16 {
    matched, _ := regexp.MatchString(`\d`, *op)
    if matched {
        val, _ := strconv.Atoi(*op)
        imm := uint16(val)
        return &imm
    }
    return nil
}

func get_input(op *string) Input {
    var input Input
    imm := get_immediate(op)
    if imm != nil {
        input.isImm = true
        input.immValue = *imm
    } else {
        input.isImm = false
        input.wire = *op
    }
    return input
}

func get_gate(ops []string) *Gate {
    gates := map[string]GateType{
        "AND":    AND,
        "OR":     OR,
        "NOT":    NOT,
        "LSHIFT": LSHIFT,
        "RSHIFT": RSHIFT,
    }

    var ty GateType
    found := false
    for _, op := range ops[:2] {
        val, ok := gates[op]
        if !ok {
            continue
        }
        found = true
        ty = val
    }
    if !found {
        return nil
    }
    var gate Gate
    gate.gateType = ty
    if ty == NOT {
        gate.inputs[0] = get_input(&ops[1])
    } else {
        gate.inputs[0] = get_input(&ops[0])
        gate.inputs[1] = get_input(&ops[2])
    }
    return &gate
}

func handleInput(wires *map[string]Wire, input Input, cached *map[string]uint16) uint16 {
    if input.isImm {
        return input.immValue
    }
    return traverse(wires, input.wire, cached)
}

func traverse_impl(wires *map[string]Wire, name string, cached *map[string]uint16) uint16 {
    if cached_val, ok := (*cached)[name]; ok {
        return cached_val
    }

    val, ok := (*wires)[name]
    if !ok {
        msg := fmt.Sprintf("unknown wire name: %s", name)
        panic(msg)
    }

    if val.wireType == IMM {
        return val.immValue
    } else if val.wireType == WIRE {
        v := traverse(wires, val.wire, cached)
        return v
    }

    input_a := handleInput(wires, val.gate.inputs[0], cached)
    if val.gate.gateType == NOT {
        return ^input_a
    }

    input_b := handleInput(wires, val.gate.inputs[1], cached)
    switch val.gate.gateType {
    case AND:
        return input_a & input_b
    case OR:
        return input_a | input_b
    case LSHIFT:
        return input_a << input_b
    case RSHIFT:
        return input_a >> input_b
    }
    panic("should not reach here")
}

func traverse(wires *map[string]Wire, name string, cached *map[string]uint16) uint16 {
    val := traverse_impl(wires, name, cached)
    (*cached)[name] = val
    return val
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

    wires := make(map[string]Wire)

    for _, line := range lines {
        splitted := strings.Fields(line)
        wire_name := splitted[len(splitted)-1]
        var wire Wire
        if gate := get_gate(splitted[:]); gate != nil {
            wire.gate = gate
            wire.wireType = GATE
        } else {
            input := get_input(&splitted[0])
            if input.isImm {
                wire.immValue = input.immValue
                wire.wireType = IMM
            } else {
                wire.wire = splitted[0]
                wire.wireType = WIRE
            }
        }

        wires[wire_name] = wire
    }
    cached := make(map[string]uint16)
    one := traverse(&wires, "a", &cached)
    fmt.Printf("Part one: %d\n", one)

    cached = make(map[string]uint16)
    cached["b"] = one

    two := traverse(&wires, "a", &cached)
    fmt.Printf("Part two: %d\n", two)
}
