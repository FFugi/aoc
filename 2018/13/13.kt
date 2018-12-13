import java.io.File

data class Position(var x : Int, var y : Int)

data class Cart(var dir : Direction, var turn : Int = 0)

enum class Direction {
	UP, DOWN, LEFT, RIGHT
}

enum class Turn {
	LEFT, STRAIGHT, RIGHT
}

fun turns() = listOf(Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT)

fun Cart.rotate(turn : Turn) {
	when (this.dir) {
		Direction.LEFT -> this.dir = when (turn) {
			Turn.LEFT -> Direction.DOWN
			Turn.RIGHT -> Direction.UP
			else -> Direction.LEFT
		}
		Direction.RIGHT -> this.dir = when (turn) {
			Turn.LEFT -> Direction.UP
			Turn.RIGHT -> Direction.DOWN
			else -> Direction.RIGHT
		}
		Direction.UP -> this.dir = when (turn) {
			Turn.LEFT -> Direction.LEFT
			Turn.RIGHT -> Direction.RIGHT
			else -> Direction.UP
		}
		Direction.DOWN -> this.dir = when (turn) {
			Turn.LEFT -> Direction.RIGHT
			Turn.RIGHT -> Direction.LEFT
			else -> Direction.DOWN
		}
	}
}

fun Cart.turnIfNeeded(ch : Char) {
	when (ch) {
		'+' -> {
			this.rotate(turns()[this.turn % 3])	
			this.turn ++
		}
		'/' -> {
			this.rotate(when (this.dir) {
				Direction.DOWN -> Turn.RIGHT
				Direction.UP -> Turn.RIGHT
				Direction.LEFT -> Turn.LEFT
				Direction.RIGHT -> Turn.LEFT
			})
		}
		'\\' -> {
			this.rotate(when (this.dir) {
				Direction.DOWN -> Turn.LEFT
				Direction.UP -> Turn.LEFT
				Direction.LEFT -> Turn.RIGHT
				Direction.RIGHT -> Turn.RIGHT
			})
		}
		else -> {}
	}
}

fun main(args : Array<String>) {
	val input = File(args[0]).readLines()
	val rails = HashMap<Position, Char>()
	val carts = HashMap<Position, Cart>()
	input.forEachIndexed { y, line ->
		line.forEachIndexed { x, ch ->
			val pos = Position(x, y)
			when (ch) {
				'|' -> rails[pos] = '|'
				'-' -> rails[pos] = '-'
				'/' -> rails[pos] = '/'
				'\\' -> rails[pos] = '\\'
				'+' -> rails[pos] = '+'
				'<' ->  {
					rails.put(pos, '-')
					carts.put(pos.copy(), Cart(Direction.LEFT))
				}
				'>' ->  {
					rails.put(pos, '-')
					carts.put(pos.copy(), Cart(Direction.RIGHT))
				}
				'^' ->  {
					rails.put(pos, '|')
					carts.put(pos.copy(), Cart(Direction.UP))
				}
				'v' ->  {
					rails.put(pos, '|')
					carts.put(pos.copy(), Cart(Direction.DOWN))
				}
				else -> {}
			}
		}
	}
	val cmp = Comparator<Position>({a, b -> when {
		a.y < b.y -> -1
		a.y > b.y -> 1
		a.x < b.x -> -1
		a.x > b.x -> 1
		else -> 0
	}})
	var wasFirstCrash = false
	var finished = false
	do {
		val keys = carts.keys.toMutableList().sortedWith(cmp)
		keys.forEach { key ->
			if (carts.contains(key)) {
				val cart = carts.remove(key)!!
				val newPos = when (cart.dir) {
					Direction.LEFT -> Position(key.x - 1, key.y)
					Direction.RIGHT -> Position(key.x + 1, key.y)
					Direction.UP -> Position(key.x, key.y - 1)
					Direction.DOWN -> Position(key.x, key.y + 1)
				}
				if (carts.contains(newPos)) {
					carts.remove(newPos)
					if (!wasFirstCrash) {
						wasFirstCrash = true
						println("First part: ${newPos.x},${newPos.y}")
					}
				} else {
					cart.turnIfNeeded(rails.getOrElse(newPos, { '+' } ))
					carts.put(newPos, cart)
				}
			}
		}
		if (carts.size == 1) {
			finished = true
			val last = carts.asIterable().first()
			println("Second part: ${last.key.x},${last.key.y}")
		}
	} while (!finished)
}
