import java.io.File

data class Data(var index : Int, val list : IntArray)

data class Node(val sum : Int, val value : Int)

fun recursiveSum(dat : Data) : Node {
	val childrenCnt = dat.list[dat.index]
	val metaDataCnt = dat.list[dat.index + 1]
	dat.index += 2
	if (childrenCnt == 0) {
		var sum = 0
		for (i in (dat.index) until (dat.index + metaDataCnt)) {
			sum += dat.list[i]
			dat.index++
		}
		return Node(sum, sum)
	} else {
		var sum = 0
		val childrenValues = HashMap<Int, Int>()
		for (i in 0 until childrenCnt) {
			val child = recursiveSum(dat)
			childrenValues[i] = child.value
			sum += child.sum
		}
		var value = 0
		val metaDataIndex = dat.index
		for (i in metaDataIndex until (metaDataIndex + metaDataCnt)) {
			val meta = dat.list[i]
			if (meta - 1 < childrenValues.size) {
				value += childrenValues.getOrElse(meta - 1, { 0 } )
			}
			sum += meta
			dat.index++
		}
		return Node(sum, value)
	}
}

fun main(args : Array<String>) {
	val input = File(args[0]).readLines()[0].split(' ').map { it.toInt() }.toIntArray()
	val dat = Data(0, input)
	val result = recursiveSum(dat)
	println("First part: ${result.sum}\nSecond part: ${result.value}")
}
