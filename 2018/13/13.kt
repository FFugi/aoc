import java.io.File
//import java.util.TreeMap

data class Position(var x : Int, var y : Int)

data class Cart(val pos : Position, var dir : Direction, var turn : Int = 0)

enum class Direction {
	UP, DOWN, LEFT, RIGHT
}

enum class Turn {
	LEFT, STRAIGHT, RIGHT
}

fun turns() = listOf(Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT)

fun rotate(cart : Cart, turn : Turn) {
	when (cart.dir) {
		Direction.LEFT -> cart.dir = when (turn) {
			Turn.LEFT -> Direction.DOWN
			Turn.RIGHT -> Direction.UP
			else -> Direction.LEFT
		}
		Direction.RIGHT -> cart.dir = when (turn) {
			Turn.LEFT -> Direction.UP
			Turn.RIGHT -> Direction.DOWN
			else -> Direction.RIGHT
		}
		Direction.UP -> cart.dir = when (turn) {
			Turn.LEFT -> Direction.LEFT
			Turn.RIGHT -> Direction.RIGHT
			else -> Direction.UP
		}
		Direction.DOWN -> cart.dir = when (turn) {
			Turn.LEFT -> Direction.RIGHT
			Turn.RIGHT -> Direction.LEFT
			else -> Direction.DOWN
		}
	}
}

fun turnIfNeeded(cart : Cart, ch : Char) {
	when (ch) {
		'+' -> {
			rotate(cart, turns()[cart.turn % 3])	
			cart.turn ++
		}
		'/' -> {
			rotate(cart, when (cart.dir) {
				Direction.DOWN -> Turn.RIGHT
				Direction.UP -> Turn.RIGHT
				Direction.LEFT -> Turn.LEFT
				Direction.RIGHT -> Turn.LEFT
			})
		}
		'\\' -> {
			rotate(cart, when (cart.dir) {
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
	val carts = mutableListOf<Cart>()
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
					carts.add(Cart(pos.copy(), Direction.LEFT))
				}
				'>' ->  {
					rails.put(pos, '-')
					carts.add(Cart(pos.copy(), Direction.RIGHT))
				}
				'^' ->  {
					rails[pos] = '|'
					carts.add(Cart(pos.copy(), Direction.UP))
				}
				'v' ->  {
					rails[pos] = '|'
					carts.add(Cart(pos.copy(), Direction.DOWN))
				}
				else -> {}
			}
		}
	}
	val cmp = Comparator<Cart>({a, b -> when {
		a.pos.y < b.pos.y -> -1
		a.pos.y > b.pos.y -> 1
		a.pos.x < b.pos.x -> -1
		a.pos.x > b.pos.x -> 1
		else -> 0
	}})
	var wasFirstCrash = false
	var finished = false
	do {
		val toRemove = mutableListOf<Cart>()
		carts.sortWith(cmp)
		carts.forEach { cart ->
			when (cart.dir) {
				Direction.LEFT -> cart.pos.x--
				Direction.RIGHT -> cart.pos.x++
				Direction.UP -> cart.pos.y--
				Direction.DOWN -> cart.pos.y++
			}
			turnIfNeeded(cart, rails.getOrElse(cart.pos, { '+' } ))
			carts.forEach { 
				if (it != cart && it.pos == cart.pos) {
					if (!wasFirstCrash) {
						wasFirstCrash = true
						println("First part: ${it.pos.x},${it.pos.y}")
					}
					toRemove.add(it)
					toRemove.add(cart)
				}
			}
		}
		toRemove.forEach {
			carts.remove(it)
		}
		if (carts.size == 1) {
			finished = true
			val last = carts.first()
			println("Second part: ${last.pos.x},${last.pos.y}")
		}
	} while (!finished)
}
