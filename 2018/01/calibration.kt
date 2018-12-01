import java.io.File

fun main(args : Array<String>) {
	if (args[1] == "1") {
		firstPart(args[0])
	} else {
		secondPart(args[0])
	}
}

fun firstPart(filename : String) {
	var offset : Int = 0
	File(filename).forEachLine { offset += it.toInt() }
	println(offset)
}

fun secondPart(filename : String) {
	var offset : Int = 0
	var found : Boolean = false
	var result : Int = 0
	var mySet : HashSet<Int> = hashSetOf(0)
	while (!found) { 
		File(filename).forEachLine {
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
	println(result)
}
