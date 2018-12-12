import java.io.File
import java.util.LinkedList
import java.util.Deque

fun Boolean.toChar() : Char = if (this) '#' else '.'

fun Char.toBoolean() : Boolean = if (this == '#') true else false

fun sumAfter(iterations : Long, pattern : Map<String, Boolean>, initState : String) : Long {
	val state = HashMap(initState.mapIndexed { 
		index, st -> index to if (st == '#') true else false 
	}.toMap())

	val que = LinkedList<Long>()
	for (i in 0L..4L) {
		que.addLast(i)	
	}

	var previousSum = 0
	var baseSum = 0
	var constDiff = 0
	var indexWhenDetected = 0L
	var wasInterrupted = false

	for (i in 0..iterations) {
		val filtered = state.filter { it.value == true }
		val min = (filtered.minBy { it.key }?.key ?: 0) - 2
		val max = (filtered.maxBy { it.key }?.key ?: 0) + 2
		val toChange = hashSetOf<Int>()
		for (pos in min..max) {
			val sequence = StringBuilder()
			for (j in -2..2) {
				val ch = state.getOrPut(pos + j, { false }).toChar()
				sequence.append(ch)
			}
			val current = sequence[2]
			val result = pattern.getOrElse(sequence.toString(), { false }).toChar()
			if (current != result) {
				toChange.add(pos)
			}
		}
		val currentSum = state.filter { it.value == true }
									.map { it.key }
									.fold(0, { sum, elem -> sum + elem })
		val diff = currentSum - previousSum 
		que.removeFirst()
		que.addLast(diff.toLong())
		wasInterrupted = que.fold(true, { acc, it -> when {
			!acc -> acc
			it != que.first() -> false
			else -> true
		}})
		if (wasInterrupted) {
			baseSum = currentSum 
			indexWhenDetected = i
			constDiff = diff
			break
		} 
		previousSum = currentSum
	
		toChange.forEach {
			state[it] = !state.getOrElse(it, { false })
		}
	}
	return if (wasInterrupted) {
		baseSum + constDiff*(iterations - indexWhenDetected)
	} else {
		previousSum.toLong()
	}
}

fun main(args : Array<String>) {
	val input = File(args[0]).readLines()
	val initState = input[0].drop(15)
	val pattern = input.subList(2, input.size).map {
		it.substring(0..4) to if (it[9] == '#') true else false 
	}.toMap()
	println("First part: ${sumAfter(20, pattern, initState)}")
	println("Second part: ${sumAfter(50000000000, pattern, initState)}")
}
