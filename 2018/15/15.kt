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

fun Position.getNeighbours() = listOf(
				Position(this.x, this.y - 1),
				Position(this.x, this.y + 1),
				Position(this.x - 1, this.y),
				Position(this.x + 1, this.y))

fun getPosCmp() = Comparator<Position>({a, b -> when {
	a.y < b.y -> -1
	a.y > b.y -> 1
	a.x < b.x -> -1
	a.x > b.x -> 1
	else -> 0
}})

fun floodFill(
		pos : Position,
		value : Int,
		floodMap : HashMap<Position, Int>, 
		walls : HashMap<Position, Field>, 
		units : HashMap<Position, Figure>,
		isFirst : Boolean) {
	if (isFirst || (
		!units.contains(pos) &&
		walls.getOrElse(pos, { Field.WALL }) != Field.WALL &&
		floodMap.getOrElse(pos, { Int.MAX_VALUE }) > value)) {
		floodMap.put(pos, value)
		val toFill = pos.getNeighbours()
		toFill.forEach {  
			floodFill(it, value + 1, floodMap, walls, units, false)
		}
	}
}

fun printFlood(floodMap : HashMap<Position, Int>) {
	for (y in 0..6) {
		for (x in 0..6) {
			val toPut = floodMap.getOrElse(Position(x, y), { "x" }).toString()
			print(toPut)
			print(' ')
		}
		println()
	}
}

fun attackIfPossible(key : Position, units : HashMap<Position, Figure>, elfHit : Int) : Boolean {
	val curType = units[key]!!.type
	val toAttack = key.getNeighbours()
	.filter { when (units.getOrElse(it, { null } )?.type) {
		null -> false
		curType -> false
		else -> true
	}}.map { it to units[it]!! }
	if (toAttack.size > 0){
		val target = toAttack
		// to export
		.sortedWith(Comparator<Pair<Position, Figure>>({a, b -> 
			when (a.second.HP.compareTo(b.second.HP)) {
				0 -> {
					getPosCmp().compare(a.first, b.first)
				}
				else -> a.second.HP.compareTo(b.second.HP)
			}
		})).first()
		target.second.HP -= if (curType == FigType.GOBLIN) 3 else elfHit
		if (target.second.HP <= 0) {
			units.remove(target.first)
		}
		return true
	} else {
		return false
	}
}

fun main(args : Array<String>) {
	val input = File(args[0]).readLines()
	var secondFinished = false
	for (hit in 3..200) {
		var initElfCount = 0
		val walls = HashMap<Position, Field>()
		val units = HashMap<Position, Figure>()
		input.forEachIndexed { y, line ->
			line.forEachIndexed { x, ch ->
				when (ch) {
					'#' -> walls.put(Position(x, y), Field.WALL)
					'.' -> walls.put(Position(x, y), Field.FLOOR)
					'E' -> {
						initElfCount++
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
		var i = 0
		var finished = false
		while (!finished) {
			val keys = units.keys.sortedWith(getPosCmp())
			keys.forEach keyLoop@{ currentKey ->
				if (!units.contains(currentKey) || finished) {
					return@keyLoop
				}
				// Check for enemies
				val inRange = HashSet<Position>()
				val others = units.keys.sortedWith(getPosCmp())
				others.forEach { otherKey ->
					if (otherKey != currentKey && 
					units[otherKey]!!.type != units[currentKey]!!.type) {
						val toCheck = otherKey.getNeighbours()
						toCheck.forEach { pos -> 
							if (!units.contains(pos) &&
							walls.getOrElse(pos, { Field.WALL }) == Field.FLOOR) 
							{
								inRange.add(pos)
							}
						}
					}
				}
				// Check for neighbours
				if (!attackIfPossible(currentKey, units, hit)) {
					// Movement
					val floodMap = HashMap<Position, Int>()
					floodFill(currentKey, 0, floodMap, walls, units, true)
					val reachable = inRange.map { it to floodMap.getOrElse(it, { Int.MAX_VALUE }) }
					.filter { it.second != Int.MAX_VALUE }
					val nearestDist = reachable.minBy( { it.second } )?.second
					val nearest = reachable.filter { it.second == nearestDist }
					.minWith(Comparator<Pair<Position, Int>>({a, b -> 
						getPosCmp().compare(a.first, b.first)
					}))

					if (nearest != null) {
						val destFloodMap = HashMap<Position, Int>()
						val dest = nearest.first
						floodFill(dest, 0, destFloodMap, walls, units, true)
						val next = currentKey.getNeighbours()
						.map { it to destFloodMap.getOrElse(it, { Int.MAX_VALUE })}
						.minWith(Comparator<Pair<Position, Int>>({a, b -> 
							when (a.second.compareTo(b.second)) {
								0 -> {
									getPosCmp().compare(a.first, b.first)
								}
								else -> a.second.compareTo(b.second)
							}
						}))!!

						val cur = units.remove(currentKey)!!
						units.put(next.first, cur)
						attackIfPossible(next.first, units, hit)
					}
				}
				finished = units.values.fold(true, { acc, it -> when {
					!acc -> acc
					it.type != units.values.first().type -> false
					else -> true
				}})
				if (finished) {
					val sum = units.values.fold(0, { acc, it -> acc + it.HP })
					if (hit == 3) {
						println("First part: ${i * sum}")
					} else {
						if (units.values.first().type == FigType.ELF && 
						units.size == initElfCount) {
							println("Second part: ${i * sum}")
							secondFinished = true
						}
					}
				}
			}
			i++
		}
		if (secondFinished) {
			break
		}
	}
}
