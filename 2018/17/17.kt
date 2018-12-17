import java.io.File

data class Position(val x : Int, val y : Int)

fun Position.left() = Position(this.x - 1, this.y)

fun Position.right() = Position(this.x + 1, this.y)

fun Position.down() = Position(this.x, this.y + 1)

enum class Type {
	BOTTOM,
	WALL
}

fun printMe(clay : HashSet<Position>, water : HashMap<Position, Char>) {
	val maxY = clay.maxBy( { it.y } )?.y ?: 0
	val minX = clay.minBy( { it.x } )?.x ?: 0 - 1
	val maxX = clay.maxBy( { it.x } )?.x ?: 0 + 1
	for (y in 0..maxY) {
		for (x in minX..maxX) {
			print( when {
				x == 500 && y == 0 -> '+'
				clay.contains(Position(x, y)) ->  '#' 
				water.contains(Position(x, y)) -> water[Position(x, y)]
				else -> ' '
			})
		}
		println()
	}
}

fun searchForWalls(
		from : Position, 
		clay : HashSet<Position>, 
		water : HashMap<Position, Char>,
		isRight : Boolean = true) 
		: Boolean
{
	val toCheck = if (isRight) from.right() else from.left()
	if (!clay.contains(from.down()) && water.getOrElse(from.down(), {'.'}) != '~') {
		return false
	} else if (
			(clay.contains(from.down()) || 
			water.getOrElse(from.down(), {'.'}) == '~') && 
			clay.contains(toCheck)) {
		water.put(from, '~')	
		return true
	}
	val result = searchForWalls(toCheck, clay, water, isRight)
	if (result) {
		water.put(from, '~')	
	} 
	return result
}

fun flow(from : Position, clay : HashSet<Position>, water : HashMap<Position, Char>, maxY : Int) {
	water.put(from, '|')
	if (!clay.contains(from.down()) && !water.contains(from.down()) &&
	from.y != maxY - 1)
	{
		flow(from.down(), clay, water, maxY)
		if (water.getOrElse(from.down(), {'.'}) == '~'){
			if (clay.contains(from.right()) && clay.contains(from.left())) {
				searchForWalls(from, clay, water)
			}
			if (!water.contains(from.left()) && !clay.contains(from.left())) {
				flow(from.left(), clay, water, maxY)
			} 
			if (!water.contains(from.right()) && !clay.contains(from.right())) {
				flow(from.right(), clay, water, maxY)
			} 
		}
	} else if (from.y != maxY - 1 &&
	water.getOrElse(from.down(), {'.'}) != '|' ) {
		if (!water.contains(from.left()) && !clay.contains(from.left())) {
			flow(Position(from.x - 1, from.y), clay, water, maxY)
		} 
		if (clay.contains(from.left())) {
			searchForWalls(from, clay, water, true)
		}
		if (clay.contains(from.right())) {
			searchForWalls(from, clay, water, false)
		}

		if (!water.contains(from.right()) &&
		!clay.contains(from.right())) {
			flow(from.right(), clay, water, maxY)
		} 
	}
}

fun main(args : Array<String>) {
	val clay = HashSet<Position>()
	val input = File(args[0]).readLines()
	input.forEach { line ->
		val splited = line.replace(",", "").split(' ')
		val coord = splited[0].substringAfter('=').toInt()
		val to = splited[1].substringAfter("..").toInt()
		val from = splited[1].substringAfter("=").substringBefore("..").toInt()
		if (splited[0][0] == 'x') {
			for (y in from..to) {
				clay.add(Position(coord, y))	
			}
		} else {
			for (x in from..to) {
				clay.add(Position(x, coord))	
			}
		}
	}
	val maxY = (clay.maxBy( { it.y } )?.y ?: 0) + 1
	val minY = (clay.minBy( { it.y } )?.y ?: 0)

	val water = HashMap<Position, Char>()
	flow(Position(500, minY), clay, water, maxY)
	val second = water.values.filter { it == '~' }.size
//	printMe(clay, water)
	println("First part: ${water.size}")
	println("Second part: $second")
}
