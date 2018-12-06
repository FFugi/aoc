import java.io.File
import kotlin.math.abs

data class Point(var distance : Int, var pointNo : Int)

data class Coord(var x : Int, var y : Int)

data class PointInfo(var isInfinite : Boolean, var count : Int)

fun distance(from : Coord, to : Coord) : Int {
	return abs(from.x - to.x) + abs(from.y - to.y)	
}

fun main(args : Array<String>) {
	val input = File(args[0]).readLines().map { 
		val splited = it.split(',')	
		Coord(splited[0].toInt(), splited[1].drop(1).toInt())
	}.sortedWith(compareBy({ it.x }, { it.y }))

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
