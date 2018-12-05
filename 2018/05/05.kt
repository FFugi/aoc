import java.io.File
import kotlin.math.abs

fun reactMe(input : String) : String {
	var output = input
	var range = output.indices
	var i = 0
	while (i < range.last) {
		do {
			var wasChange = false
			if (i < range.last) {
				if (abs(output[i] - output[i + 1]) == 32){
					wasChange = true
					output = output.replaceRange(i, i + 2, "")
					range = output.indices
					if (i > 0) { i-- }
				}
			}
		} while (wasChange)
		i++
	}
	return output
}

fun main(args : Array<String>) {
	var input = File(args[0]).readLines()[0]
	var second = ('a'..'z').map { c ->
		reactMe( input.filter { it != c && it != c - 32 } ).length
	}.toList().min()
	println("First part: ${reactMe(input).length}")
	println("Second part: $second")
}
