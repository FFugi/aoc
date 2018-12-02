import java.io.File

fun main(args : Array<String>) {
	if (args[1] == "1") {
		firstPart(args[0])
	} else {
		secondPart(args[0])
	}
}

fun firstPart(filename : String) {
	var lines = File(filename).readLines()
	var two : Int = 0
	var three : Int = 0
	val defaultMap = ('a'..'z').map { it to 0 }.toMap()
	lines.forEach { 
		val map = HashMap<Char, Int>(defaultMap)
		it.asSequence().forEach {
			map[it] = (map[it] ?: 0) + 1
		}
		var wasTwo = false
		var wasThree = false
		for ((letter, occurences) in map) {
			if (occurences == 2 && !wasTwo) {
				wasTwo = true
				two++
			} else if (occurences == 3 && !wasThree) {
				wasThree = true
				three++
			}
		}
	}
	println("${two * three}")
}

fun secondPart(filename : String) {
	var lines = File(filename).readLines()
	lup@ for (one in lines) {
		for (two in lines) {
			var diff = 0
			var lastDiff = 0
			for (i in 0 until one.length){
				if (one[i] != two[i]) {
					diff++	
					lastDiff = i
				}
			}
			if (diff == 1) {
				println("${one.substring(0..lastDiff-1)}${one.substring(lastDiff+1..one.length-1)}")
				break@lup
			}
		}
	}
}
