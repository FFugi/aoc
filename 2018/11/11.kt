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
				var sum = 0
				if (size % 2 == 0) {
					val half = size / 2
					sum += sumGrid.getOrElse(Square(Point(x, y), half), { 0 })
					sum += sumGrid.getOrElse(Square(Point(x + half, y), half), { 0 })
					sum += sumGrid.getOrElse(Square(Point(x, y + half), half), { 0 })
					sum += sumGrid.getOrElse(Square(Point(x + half, y + half), half), { 0 })
				} else {
					val smaller = size / 2
					val bigger = smaller + 1
					sum += sumGrid.getOrElse(Square(Point(x, y), bigger), { 0 })
					sum += sumGrid.getOrElse(Square(Point(x + bigger, y), smaller), { 0 })
					sum += sumGrid.getOrElse(Square(Point(x, y + bigger), smaller), { 0 })
					sum += sumGrid.getOrElse(Square(Point(x + smaller, y + smaller), bigger), { 0 })
					sum -= sumGrid.getOrElse(Square(Point(x + smaller, y + smaller), 1), { 0 })
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
