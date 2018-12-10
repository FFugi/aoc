import java.io.File

data class Vector2(var x : Int, var y : Int)

data class Point(val pos : Vector2, val vel : Vector2)

fun maxPoint() = Point(Vector2(Int.MIN_VALUE, Int.MIN_VALUE), Vector2(0, 0))
fun minPoint() = Point(Vector2(Int.MAX_VALUE, Int.MAX_VALUE), Vector2(0, 0))

fun <T> List<T>.minmax(cmp : Comparator<T>) : Pair<T, T> {
	return this.fold( Pair(this[0], this[0]), {
		(min, max), current ->
			Pair(
					if (cmp.compare(current, min) == -1) current else min,
					if (cmp.compare(current, max) == 1) current else max
			)
		})
}

fun printMe(list : List<Point>, start : Vector2, stop : Vector2) {
	val map = list.map { it.pos }.toSet()
	for (y in start.y..stop.y) {
		for (x in start.x..stop.x) {
			if (map.contains(Vector2(x, y))) {
				print('#') 
			} else { 
				print(' ') 
			}
		}
		print('\n')
	}
	print('\n')
}

fun main(args : Array<String>) {
	val pts = File(args[0]).readLines()
	.map {
		val splited = it.split("((\\s)|<|>|,)".toRegex())
		.filter { it != "position=" && it != "velocity=" && it.length > 0 }
		.map { it.toInt() }
		Point(Vector2(splited[0], splited[1]), Vector2(splited[2], splited[3]))
	}
	var i = 0

	val cmp = Comparator<Point>({a, b -> when {
		a.pos.y < b.pos.y -> -1
		a.pos.y > b.pos.y -> 1
		a.pos.x < b.pos.x -> -1
		a.pos.x > b.pos.x -> 1
		else -> 0
	}})
		
	do {
		val (min, max) = pts.minmax(cmp)
		val diff = max.pos.y - min.pos.y
		if (diff < 10) {
			println("First part:")
			printMe(pts, min.pos, max.pos)
		}
		pts.forEach {
			it.pos.x += it.vel.x
			it.pos.y += it.vel.y
		}
		i++
	} while (diff > 10)
	println("Second part: ${i - 1}")
}
