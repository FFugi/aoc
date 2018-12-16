import java.io.File

data class Example(
		val before : List<Int>,
		val instruction : List<Int>, 
		val after : List<Int>)

fun exec(registers : List<Int>, instruction : List<Int>, code : Opcode) : List<Int> {
	val list = registers.toMutableList()
	val a = instruction[1]
	val b = instruction[2]
	val c = instruction[3]
	list[c] = when (code) {
		Opcode.ADDR -> registers[a] + registers[b]
		Opcode.ADDI -> registers[a] + instruction[2]
		Opcode.MULR -> registers[a] * registers[b]
		Opcode.MULI -> registers[a] * instruction[2]
		Opcode.BANR -> registers[a] and registers[b]
		Opcode.BANI -> registers[a] and b
		Opcode.BORR -> registers[a] or registers[b]
		Opcode.BORI -> registers[a] or b
		Opcode.SETR -> registers[a]
		Opcode.SETI -> a
		Opcode.GTIR -> if (a > registers[b]) 1 else 0
		Opcode.GTRI -> if (registers[a] > b) 1 else 0
		Opcode.GTRR -> if (registers[a] > registers[b]) 1 else 0
		Opcode.EQIR -> if (a == registers[b]) 1 else 0
		Opcode.EQRI -> if (registers[a] == b) 1 else 0
		Opcode.EQRR -> if (registers[a] == registers[b]) 1 else 0
	}
	return list
}

enum class Opcode {
	ADDR, ADDI, 
	MULR, MULI,
	BANR, BANI, BORR, BORI,
	SETR, SETI,
	GTIR, GTRI, GTRR,
	EQIR, EQRI, EQRR,
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
			if (exec(before, instruction, value).toString() == after.toString()) {
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
		registers = exec(registers, instruction, opcodes[opcode])
	}
	println("Second part: ${registers[0]}")
}
