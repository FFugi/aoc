import java.io.File

data class Vector2(var x : Int, var y : Int)

data class Point(val pos : Vector2, val vel : Vector2)

fun printMe(list : List<Point>, start : Vector2, stop : Vector2) {
	val map = list.map { it.pos }.toSet()
	for (y in start.y..stop.y) {
		for (x in start.x..stop.x) {
			if (map.contains(Vector2(x, y))) {
				print('#') 
			} else { 
				print('.') 
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
	do {
		val maxR = pts.maxBy { it.pos.x }?.pos?.x ?: 0
		val maxL = pts.minBy { it.pos.x }?.pos?.x ?: 0
		val maxTop = pts.minBy { it.pos.y }?.pos?.y ?: 0
		val maxBot = pts.maxBy { it.pos.y }?.pos?.y ?: 0
		val diff = maxBot - maxTop
		if (diff < 10) {
			println("First part:")
			printMe(pts, Vector2(maxL, maxTop), Vector2(maxR, maxBot))
		}
		pts.forEach {
			it.pos.x += it.vel.x
			it.pos.y += it.vel.y
		}
		i++
	} while (diff > 10)
	println("Second part: ${i - 1}")
}
