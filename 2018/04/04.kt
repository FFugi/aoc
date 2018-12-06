import java.io.File

fun main(args : Array<String>) {
	val sorted = File(args[0]).readLines().sortedWith(compareBy( { it.substring(1..16)} ))
	val guardMap = HashMap<Int, HashMap<Int, Int>>()
	var currentId = -1
	var feltWhen = -1;
	for (record in sorted) {
		val words = record.split(' ')
		val minutes = words[1].split(':')[1].substring(0..1).toInt()
		if (words.size == 6) {
			currentId = words[3].substring(1 until words[3].length).toInt()
		}
		else if (words.size == 4 && words[2] == "falls") {
			feltWhen = minutes
		} else {
			for (i in feltWhen until minutes) {
				val map = guardMap.getOrPut(currentId, { HashMap<Int, Int>() })
				val min = map.getOrElse(i, { 0 })
				map.put(i, min + 1)
			}
		}
	}
	val list = guardMap.toList()
	val id = list.sortedByDescending { 
		 it.second.toList().map{ it.second }.reduce { a, b -> a + b } 
	}[0].first

	val minute = guardMap[id]!!.toList().sortedByDescending { it.second }[0].first
	println("First part: ${id * minute}")

	var minutoOfMax = -1
	var maxMinute = -1
	var guardIdOfMax = -1
	for (guard in guardMap) {
		var max = guard.value.maxBy { it.value }
		if (max!!.value > maxMinute) {
			maxMinute = max.value
			minutoOfMax = max.key
			guardIdOfMax = guard.key
		}

	}
	println("Second part: ${minutoOfMax * guardIdOfMax}")

}
