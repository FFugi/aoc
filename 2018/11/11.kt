data class Point(val x : Int, val y : Int)
data class Square(val pos : Point, val size : Int)

fun main(args : Array<String>) {
	val serialNo = args[0].toInt()
	val sumGrid = HashMap<Square, Int>()
	val dim = 300
	for (y in 1..300) {
		for (x in 1..300) {
			val rackID = (x + 10)
			val pwr = (((rackID * y + serialNo) * rackID) / 100).rem(10) - 5
			sumGrid[Square(Point(x, y), 1)] = pwr
		}
	}
	println("------Progress------|")
	for (size in 2..dim) {
		if (size % (dim / 20) == 0) { print('#') }
		for (y in 1..(dim - size + 1)) {
			for (x in 1..(dim - size + 1)) {
				var sum = sumGrid.getOrElse(Square(Point(x, y), size - 1), { 0 })
				for (i in 0 until size) {
					sum += sumGrid.getOrElse(Square(Point(x + size - 1, y + i), 1), { 0 } )
				}
				for (i in 0 until size - 1) {
					sum += sumGrid.getOrElse(Square(Point(x + i, y + size - 1), 1), { 0 } )
				}
				sumGrid[Square(Point(x, y), size)] = sum
			}
		}
	}

	val first = sumGrid.filter { it.key.size == 3 }.maxBy( { it.value } )?.key?.pos
	val second = sumGrid.maxBy( { it.value } )?.key
	println("\nFirst part: ${first?.x},${first?.y}")
	println("Second part: ${second?.pos?.x},${second?.pos?.y},${second?.size}")
}
