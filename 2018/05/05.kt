import java.io.File
import kotlin.math.abs

fun reactMe(input : String) : String {
	val output = StringBuilder()
	input.forEach { c ->
		val last = output.getOrElse(output.length - 1, { c } )
		if (abs(c - last)  == 32) {
			output.setLength(output.length - 1)
		} else {
			output.append(c)
		}
	}
	return output.toString()
}

fun main(args : Array<String>) {
	var input = File(args[0]).readLines()[0]
	var second = ('a'..'z').map { c ->
		reactMe( input.filter { it != c && it != c - 32 } ).length
	}.toList().min()
	println("First part: ${reactMe(input).length}")
	println("Second part: $second")
}
