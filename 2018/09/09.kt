import java.util.LinkedList
import java.io.File

fun getScore(playerCount : Int, lastMarble : Int) : Long {
	val playerPts = LongArray(playerCount)
	val circle = LinkedList<Long>()
	circle.add(0)

	var i = 0
	var it = circle.listIterator()
	do {
		val currentPlayer = i % playerCount
		val currentMarble = i + 1L
		if (currentMarble % 23 == 0L){
			var removedVal = 0L
			for (j in 0 until 7) {
				if (it.hasPrevious()) {
					removedVal = it.previous()
				} else {
					it = circle.listIterator(circle.size)
					removedVal = it.previous()
				}
			}
			it.remove()
			val points = removedVal + currentMarble
			playerPts[currentPlayer] += points
		} else {
			for (j in 0 until 2) {
				if (it.hasNext()) {
					it.next()
				} else {
					it = circle.listIterator()
					it.next()
				}
			}
			it.add(currentMarble)
			it.previous()
		}
		i++
	} while(i < lastMarble)
	return playerPts.max() ?: 0
}

fun main(args : Array<String>) {
	val input = File(args[0]).readLines()[0].split(' ')
	val players = input[0].toInt()
	val lastMarble = input[6].toInt()	
	println("First part: ${getScore(players, lastMarble)}")
	println("Second part: ${getScore(players, lastMarble * 100)}")
}
