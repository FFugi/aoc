import java.io.File

fun main(args : Array<String>) {
	var lines = File(args[0]).readLines()
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
		for ((_, occurences) in map) {
			if (occurences == 2 && !wasTwo) {
				wasTwo = true
				two++
			} else if (occurences == 3 && !wasThree) {
				wasThree = true
				three++
			}
		}
	}
	println("First part: ${two * three}")

	lup@ 
	for (a in lines) {
		for (b in lines) {
			var diff = 0
			var lastDiff = 0
			for (i in 0 until a.length){
				if (a[i] != b[i]) {
					diff++	
					lastDiff = i
				}
			}
			if (diff == 1) {
				println("Second part: ${a.substring(0..lastDiff-1)}" +  
						"${a.substring(lastDiff+1..a.length-1)}")
				break@lup
			}
		}
	}
}
