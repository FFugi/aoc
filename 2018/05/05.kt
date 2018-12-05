import java.io.File
import kotlin.math.abs

fun reactMe(input : String) : String {
	var output = input
	do {
		val range = output.indices	
		var wasChange = false
		for (i in range) {
			if (i < range.last) {
				if (abs(output[i] - output[i + 1]) == 32){
					wasChange = true
					output = output.replaceRange(i, i + 2, "  ")
				}
			}
		}
		output = output.filter{ it != ' ' }
	} while (wasChange)
	return output
}

fun main(args : Array<String>) {
	var input = File(args[0]).readLines()[0]
	
	var values = mutableListOf<Int>()
	for (c in 'a'..'z') {
		val tmp = input.filter { it != c && it != c - 32 }
		val value = reactMe(tmp).length
		values.add(value)
		println("$c ${value}")	
	}
	println("First part: ${reactMe(input).length}")
	println("Second part: ${values.min()}")
}
