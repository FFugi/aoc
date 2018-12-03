import java.io.File

fun main(args : Array<String>) {
	val input = File(args[0]).readLines()

	var first = input.map { it.toInt() }.reduce { a, b -> a + b }
	println("First part: $first")

	var offset : Int = 0
	var found : Boolean = false
	var result : Int = 0
	var mySet : HashSet<Int> = hashSetOf(0)
	while (!found) { 
		input.forEach {
			if (!found) {
				offset += it.toInt()
				if (!mySet.contains(offset)) {
					mySet.add(offset)
				} else {
					found = true
					result = offset
				}
			}
		}
	}
	println("Second part: $result")
}

