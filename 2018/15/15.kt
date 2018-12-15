import java.io.File
import kotlin.math.abs

enum class FigType(val ch : Char) {
	ELF('E'), GOBLIN('G')
}

enum class Field(val ch : Char) {
	WALL('W'), FLOOR('.')
}

data class Position(var x : Int, var y : Int)

data class Figure(val type : FigType, var HP : Int = 200)

fun getDistance(from : Position, to : Position) = abs(from.x - to.x) + abs(from.y - to.y)	

fun Boolean.toChar() = if (this) '#' else '.'	

fun printMap(walls : HashMap<Position, Field>, units : HashMap<Position, Figure>) {
	for (y in 0..10) {
		for (x in 0..10) {
			val pos = Position(x, y)
			val ch = units[pos]?.type?.ch ?: walls.getOrElse(pos, { Field.WALL }).ch
			print(ch)
		}
		println()
	}
}

fun getCmp() = Comparator<Position>({a, b -> when {
	a.y < b.y -> -1
	a.y > b.y -> 1
	a.x < b.x -> -1
	a.x > b.x -> 1
	else -> 0
}})

fun main(args : Array<String>) {
	val input = File(args[0]).readLines()
	val walls = HashMap<Position, Field>()
	val units = HashMap<Position, Figure>()
	input.forEachIndexed { y, line ->
		line.forEachIndexed { x, ch ->
			when (ch) {
				'#' -> walls.put(Position(x, y), Field.WALL)
				'.' -> walls.put(Position(x, y), Field.FLOOR)
				'E' -> {
					walls.put(Position(x, y), Field.FLOOR)
					units.put(Position(x, y), Figure(FigType.ELF))
				}
				'G' -> {
					walls.put(Position(x, y), Field.FLOOR)
					units.put(Position(x, y), Figure(FigType.GOBLIN))
				}
				else -> {}
			}
		}
	}


	var i = 5
	while (i > 0) {
		val keys = units.keys.sortedWith(getCmp())
		keys.forEach { currentKey ->
			val inRange = HashSet<Position>()
			keys.forEach { otherKey ->
				if (otherKey != currentKey && units[otherKey]!! != units[currentKey]!!) {
					val toCheck = listOf(
							Position(otherKey.x, otherKey.y - 1),
							Position(otherKey.x, otherKey.y + 1),
							Position(otherKey.x - 1, otherKey.y),
							Position(otherKey.x + 1, otherKey.y)
					)
					toCheck.forEach { pos -> 
						if (!units.contains(pos) &&
						walls.getOrElse(pos, { Field.WALL }) == Field.FLOOR) 
						{
							inRange.add(pos)
						}
					}
				}
			}
			println(inRange.size)
		}
		i--
	}

	printMap(walls, units)
}
