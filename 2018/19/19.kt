import java.io.File

fun exec(registers : MutableList<Int>, instruction : List<Int>, code : Opcode) {
	val a = instruction[0]
	val b = instruction[1]
	val c = instruction[2]
	registers[c] = when (code) {
		Opcode.ADDR -> registers[a] + registers[b]
		Opcode.ADDI -> registers[a] + instruction[1]
		Opcode.MULR -> registers[a] * registers[b]
		Opcode.MULI -> registers[a] * instruction[1]
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
		else -> registers[0]
	}
}

enum class Opcode {
	ADDR, ADDI, 
	MULR, MULI,
	BANR, BANI, BORR, BORI,
	SETR, SETI,
	GTIR, GTRI, GTRR,
	EQIR, EQRI, EQRR,
	NONE
}

fun String.toOpcode() : Opcode {
	return when (this) {
		"addr" -> Opcode.ADDR
		"addi" -> Opcode.ADDI
		"mulr" -> Opcode.MULR
		"muli" -> Opcode.MULI
		"banr" -> Opcode.BANR
		"bani" -> Opcode.BANI
		"borr" -> Opcode.BORR
		"bori" -> Opcode.BORI
		"setr" -> Opcode.SETR
		"seti" -> Opcode.SETI
		"gtir" -> Opcode.GTIR
		"gtri" -> Opcode.GTRI
		"gtrr" -> Opcode.GTRR
		"eqir" -> Opcode.EQIR
		"eqri" -> Opcode.EQRI
		"eqrr" -> Opcode.EQRR
		else -> Opcode.NONE
	}
}

data class Instruction(val op : Opcode, val a : Int, val b : Int, val c : Int)

fun main(args : Array<String>) {
	val input = File(args[0]).readLines()
	val ip = input[0].split(' ')[1].toInt()
	val instructions = input.drop(1).map { 
		val splited = it.split(' ')
		Instruction(
				splited[0].toOpcode(), 
				splited[1].toInt(), 
				splited[2].toInt(), 
				splited[3].toInt())
	}.toList()
//	instructions.forEach { println(it) }

	var registers =  mutableListOf(0, 0, 0, 0, 0, 0)
	var finished = false
	while (!finished) {
		val (opcode, a, b, c) = instructions[registers[ip]]
		var list = mutableListOf<Int>()
		list.addAll(registers)
//		print("$registers, after: ")
		exec(registers, listOf(a, b, c), opcode)
		registers.forEachIndexed { i, el -> 
			list[i] = el - list[i]
		}
//		print(registers)
//		println(" $list")
		if (registers[ip] > 11) {
			println(registers)
		}
		val nextIndex = (registers[ip] ?: 0) + 1
		if (nextIndex >= instructions.size) {
			finished = true
		} else {
			registers[ip] = nextIndex
		}
	}
	println(registers[0])
}
