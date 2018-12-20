import java.io.File

data class Position(val x : Int, val y : Int)

fun Position.getNeighbours() : List<Position> {
	val (x, y) = this
	return listOf(
			Position(x - 1, y - 1),
			Position(x - 1, y),
			Position(x - 1, y + 1),
			Position(x, y - 1),
			Position(x, y + 1),
			Position(x + 1, y - 1),
			Position(x + 1, y),
			Position(x + 1, y + 1))
}

// Debug function
fun printMe(map : MutableMap<Position, Char>) {
	val maxY = map.keys.maxBy { it.y }?.y ?: 0
	val maxX = map.keys.maxBy { it.x }?.x ?: 0
	for (y in 0..maxY) {
		for (x in 0..maxX) {
			print(map.getValue(Position(x, y)))
		}
		println()
	}
	println()
}

fun getShape(map : MutableMap<Position, Char>) : String {
	val maxY = map.keys.maxBy { it.y }?.y ?: 0
	val maxX = map.keys.maxBy { it.x }?.x ?: 0
	val str = StringBuilder()
	for (y in 0..maxY) {
		for (x in 0..maxX) {
			str.append(map.getValue(Position(x, y)))
		}
	}
	return str.toString()
}

fun doIteration(map : MutableMap<Position, Char>) : MutableMap<Position, Char> {
	val nextMap = HashMap<Position, Char>(map).withDefault( { ' ' } )
	map.keys.forEach { key ->
		val neighbours = key.getNeighbours()
		.map { n -> map.getValue(n) }
		.fold(HashMap<Char, Int>().withDefault( { 0 } ), { acc, it -> 
			acc.put(it, acc.getValue(it) + 1) 
			acc
		})
		when (map.getValue(key)) {
			'.' -> if (neighbours.getValue('|') >= 3) nextMap.put(key, '|')
			'|' -> if (neighbours.getValue('#') >= 3) nextMap.put(key, '#')
			'#' -> if (!(neighbours.getValue('#') >= 1 && neighbours.getValue('|') >= 1)) {
				nextMap.put(key, '.')
			}
		}
	}
	return nextMap
}

fun getResult(map : MutableMap<Position, Char>) : Int {
	val lumberyards = map.values.fold(0, { acc, it -> if (it == '#') acc + 1 else acc })
	val trees = map.values.fold(0, { acc, it -> if (it == '|') acc + 1 else acc })
	return trees * lumberyards
}

fun main(args : Array<String>) {
	var map = HashMap<Position, Char>().withDefault( { ' ' } )
	File(args[0]).readLines()
	.forEachIndexed { y, line ->
		line.forEachIndexed { x, ch ->
			map.put(Position(x, y), ch)
		}
	}

	val shapeMap = HashMap<String, Int>()
	var turn = 0
	var finished = false
	val iterations = 1000000000
	while (!finished) {
		val shape = getShape(map)
		if (!finished && shapeMap.contains(shape)) {
			val repeatedIndex = shapeMap[shape]!!
			val cycleLen = turn - repeatedIndex
			val cycleValues = mutableListOf<Int>()
			finished = true
			for (i in 0 until cycleLen) {
				cycleValues.add(getResult(map))
				map = doIteration(map)
			}
			val indexToRead = (iterations - repeatedIndex) % cycleLen
			println("Second part: ${cycleValues[indexToRead]}")
		} else {
			shapeMap.put(shape, turn)
		}
		map = doIteration(map)

		if (turn == 9) {
			println("First part: ${getResult(map)}")
		}
		turn++
	}
}
