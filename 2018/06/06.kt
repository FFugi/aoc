import java.io.File
import kotlin.math.abs

data class Point(var distance : Int, var pointNo : Int)

data class Coord(var x : Int, var y : Int)

data class PointInfo(var isInfinite : Boolean, var count : Int)

fun distance(from : Coord, to : Coord) : Int = abs(from.x - to.x) + abs(from.y - to.y)	

fun main(args : Array<String>) {
	val input = File(args[0]).readLines().map { 
		val splited = it.split(',')	
		Coord(splited[0].toInt(), splited[1].drop(1).toInt())
	}

	val maxX = input.maxWith(compareBy({ it.x }))?.x ?: 0
	val minX = input.maxWith(compareBy({ -it.x }))?.x ?: 0
	val maxY = input.maxWith(compareBy({ it.y }))?.y ?: 0
	val minY = input.maxWith(compareBy({ -it.y }))?.y ?: 0

	val map = HashMap<Coord, Point>()
	val counts = Array(input.size, { 0 } )
	input.forEachIndexed { i, cor ->
		for (y in minY..maxY) {
			for (x in minX..maxX) {
				val curDistance = distance(Coord(x, y), cor)
				val mapVal = map.getOrPut(Coord(x, y), { Point(curDistance, 0) } )
				if (curDistance < mapVal.distance) {
					if (mapVal.pointNo != -1) {
						counts[mapVal.pointNo] --
					}
					counts[i]++
					map.put(Coord(x, y), Point(curDistance, i))
					
				} else if (i == 0) {
					counts[i]++
				} else if (curDistance == mapVal.distance) {
					if (mapVal.pointNo != -1) {
						counts[mapVal.pointNo] --
					}
					map.put(Coord(x, y), Point(curDistance, -1))
				}
			}
		}
	}
	val cords = input.mapTo(HashSet<Coord>()) { it }
	for (y in minY..maxY) {
		for (x in minX..maxX) {
			val cur = (map[Coord(x, y + 1)]?.pointNo ?: 0)
			val l = (map[Coord(x - 1, y)]?.pointNo ?: 0)
			val r = (map[Coord(x + 1, y)]?.pointNo ?: 0)
			val t = (map[Coord(x, y - 1)]?.pointNo ?: 0)
			val d = (map[Coord(x, y + 1)]?.pointNo ?: 0)
			if (cords.contains(Coord(x, y))) {
				print('X')
			} else if (cur == -1
				|| cur - r != 0
				|| cur - l != 0
				|| cur - d != 0
				|| cur - t != 0) {
				print('#')
			} else {
				print(' ')
			}
		}
		print('\n')
	}

	val infinitePoints = HashSet<Int>()
	for (x in minX..maxX) {
		var pointNo = map[Coord(x, minY)]!!.pointNo
		infinitePoints.add(pointNo)
		pointNo = map[Coord(x, maxY)]!!.pointNo
		infinitePoints.add(pointNo)
	}
	for (y in minY..maxY) {
		var pointNo = map[Coord(minX, y)]!!.pointNo
		infinitePoints.add(pointNo)
		pointNo = map[Coord(maxX, y)]!!.pointNo
		infinitePoints.add(pointNo)
	}
	val result = counts.filterIndexed { i, _ -> !infinitePoints.contains(i) }.max()
	println("First part: $result")

	// Second Part
	var sizeOfRegion = 0
	for (y in minY..maxY) {
		for (x in minX..maxX) {
			var dist = 0
			input.forEachIndexed { _, cor ->
				dist += distance(Coord(x, y), cor)
			}
			if (dist < 10000) {
				sizeOfRegion++
			}
		}
	}
	println("Second part: $sizeOfRegion")
}
