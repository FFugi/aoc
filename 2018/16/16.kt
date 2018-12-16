import java.io.File

data class Example(
		val before : List<Int>,
		val instruction : List<Int>, 
		val after : List<Int>)

// Additions
fun addr(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = registers[a] + registers[b]
	return list
}

fun addi(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val c = instruction[3]
	list[c] = registers[a] + instruction[2]
	return list
}

// Multiplications 
fun mulr(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = registers[a] * registers[b]
	return list
}

fun muli(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val c = instruction[3]
	list[c] = registers[a] * instruction[2]
	return list
}

// Bitwise
fun banr(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = registers[a] and registers[b]
	return list
}

fun bani(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = registers[a] and b
	return list
}

fun borr(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = registers[a] or registers[b]
	return list
}

fun bori(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = registers[a] or b
	return list
}

// Assignment
fun setr(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val c = instruction[3]
	list[c] = registers[a]
	return list
}

fun seti(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val c = instruction[3]
	list[c] = a
	return list
}

// Greater-than testing:
fun gtir(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = if (a > registers[b]) 1 else 0
	return list
}

fun gtri(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = if (registers[a] > b) 1 else 0
	return list
}

fun gtrr(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = if (registers[a] > registers[b]) 1 else 0
	return list
}

// Equality testing:
fun eqir(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = if (a == registers[b]) 1 else 0
	return list
}

fun eqri(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = if (registers[a] == b) 1 else 0
	return list
}

fun eqrr(registers : List<Int>, instruction : List<Int>) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = if (registers[a] == registers[b]) 1 else 0
	return list
}

enum class Opcode(val exec : (List<Int>, List<Int>) -> List<Int>) {
	ADDR({ reg, inst -> addr(reg, inst) }), 
	ADDI({ reg, inst -> addi(reg, inst) }), 

	MULR({ reg, inst -> mulr(reg, inst) }), 
	MULI({ reg, inst -> muli(reg, inst) }),

	BANR({ reg, inst -> banr(reg, inst) }), 
	BANI({ reg, inst -> bani(reg, inst) }), 
	BORR({ reg, inst -> borr(reg, inst) }), 
	BORI({ reg, inst -> bori(reg, inst) }),

	SETR({ reg, inst -> setr(reg, inst) }), 
	SETI({ reg, inst -> seti(reg, inst) }),

	GTIR({ reg, inst -> gtir(reg, inst) }),
	GTRI({ reg, inst -> gtri(reg, inst) }), 
	GTRR({ reg, inst -> gtrr(reg, inst) }),

	EQIR({ reg, inst -> eqir(reg, inst) }), 
	EQRI({ reg, inst -> eqri(reg, inst) }), 
	EQRR({ reg, inst -> eqrr(reg, inst) }),

	NONE({ _, _ -> listOf<Int>() })
}


fun main(args : Array<String>) {
	val part1 = File(args[0])
	.readLines()
	.filter { it.contains("\\s+".toRegex()) }
	.withIndex()
	.groupBy { it.index / 3 }
	.map { 
		Example(
				it.value[0].value
				.replace("(Before:\\s+\\[|\\])".toRegex(), "")
				.split(", ")
				.map { it.toInt() },
				it.value[1].value
				.split("\\s+".toRegex())
				.map { it.toInt() },
				it.value[2].value
				.replace("(After:\\s+\\[|\\])".toRegex(), "")
				.split(", ")
				.map { it.toInt() })
	}

	var counter = 0
	val codeSets = Array<HashSet<Opcode>>(16, { hashSetOf() } )
	part1.forEach {
		var i = 0
		val ( before, instruction, after ) = it
		Opcode.values().forEach { value ->
			if (value.exec(before, instruction).toString() == after.toString()) {
				codeSets[instruction[0]].add(value)
				i++
			}
		}
		if (i >= 3) {
			counter++
		}
	}

	println("First part: $counter")
	
	do {
		val singleValues = codeSets.filter { it.size == 1 }.map { it.first() }
		codeSets.forEach { codeSet ->
			if (codeSet.size > 1) {
				singleValues.forEach { toDelete ->
					codeSet.remove(toDelete)
				}
			}
		}
	} while(singleValues.size != 16)

	val opcodes = codeSets.map { it.first() }
	val part2 = File(args[1]).readLines()
	.map { it.split(' ').map { it.toInt() } }

	var registers =  listOf(0, 0, 0, 0)

	part2.forEach { instruction ->
		val opcode = instruction[0]
		registers = opcodes[opcode].exec(registers, instruction)
	}
	println("Second part: ${registers[0]}")
}
