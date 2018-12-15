fun Int.getDigits() : List<Int> {
	var num = this	
	val list = mutableListOf<Int>()
	do {
		list.add(num % 10)
		num /= 10
	} while(num > 0)
	return list.reversed()
}

fun deb(list : List<Int>, elf1 : Int, elf2 : Int) {
	list.forEachIndexed { i, it ->
		when (i) {
			elf1 -> print("($it)")
			elf2 -> print("[$it]")
			else -> print(" $it ")
		}
	}
	println()
}

fun main(args : Array<String>) {
	val input = args[0].toInt()
	val recepies = mutableListOf(3, 7)
	var elf1 = 0
	var elf2 = 1
	val sequence = mutableListOf(3, 7)
	var i = 0L
	var finished = false
	var firstFinished = false
	while (!finished) {
		val current = recepies[elf1] + recepies[elf2]
		val toAdd = current.getDigits()
		recepies.addAll(toAdd)
		val diff1 = recepies.size - elf1
		val diff2 = recepies.size - elf2
		elf1 = (recepies.size + (1 + recepies[elf1] - diff1)) % recepies.size
		elf2 = (recepies.size + (1 + recepies[elf2] - diff2)) % recepies.size

		if (!firstFinished && recepies.size > input + 10) {
			firstFinished = true
			println("First part: ${recepies.subList(input, input + 10)
							.fold ("", { acc, ch -> acc + ch.toString() })}")
		}

		toAdd.forEach {
			sequence.add(it)
			if (sequence.size > args[0].length) {
				sequence.removeAt(0)
				i++
			}
			val result = sequence.fold("", { acc, ch -> acc + ch.toString() })
			if (result.toInt() == input) {
				finished = true
				println("Second part: $i")
			}
		}
	}
}
