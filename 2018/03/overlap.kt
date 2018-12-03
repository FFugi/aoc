import java.io.File

typealias Coord = Pair<Int, Int>

data class Record(val id : String, val coords : Coord, val dimensions : Coord)

fun main(args : Array<String>) {
	val input = File(args[0]).readLines()
	val mySet = HashMap<Coord, Boolean>()
	var overlappedSquares = 0
	val list : List<Record> = input.map { 
		val lineArgs = it.split(' ')	
		Record(lineArgs[0],
			lineArgs[2].substring(0..(lineArgs[2].length - 2))
			.split(',')
			.map { it.toInt() }
			.zipWithNext()[0],
			lineArgs[3].split('x')
			.map { it.toInt() }
			.zipWithNext()[0])
	}
	for (record in list) {
		val (id, coords, dimensions) = record
		for (x in (coords.first) until (coords.first + dimensions.first)) {
			for (y in (coords.second) until (coords.second + dimensions.second)) {
				if (mySet[Coord(x, y)] == null) {
					mySet[Coord(x, y)] = false 
				} else if (mySet[Coord(x, y)] == false) {
					mySet[Coord(x, y)] = true 
					overlappedSquares++
				} 
			}
		}
	}
	for (record in list) {
		val (id, coords, dimensions) = record
		var isOk = true
		for (x in (coords.first) until (coords.first + dimensions.first)) {
			for (y in (coords.second) until (coords.second + dimensions.second)) {
				if (mySet[Coord(x, y)] != null && mySet[Coord(x, y)] == true) {
					isOk = false
				}
			}
		}
		if (isOk) {
			println("second part: $id")
		}
	}
	println("first part: $overlappedSquares")
}
