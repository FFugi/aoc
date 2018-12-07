import java.io.File

data class Worker(var task : Char, var time : Int, val id : Int)

fun main(args : Array<String>) {
	val firstPart = StringBuilder()
	val map1 = HashMap<Char, HashSet<Char>>()
	val input = File(args[0]).readLines()
	val values = HashSet<Char>()
	input.forEach { 
		val splited = it.split(' ')
		map1.getOrPut(splited[1].get(0), { HashSet<Char>() }).add(splited[7].get(0))
		values.add(splited[7].get(0))
	}
	values.forEach { 
		if (!map1.containsKey(it)) {
			map1.put(it, hashSetOf('0'))
		}
	}
	val map2 = HashMap(map1)

	do {
		val available = HashSet<Char>()
		map1.forEach { e ->
			var shouldInclude = true
			e.value.forEach { 
				map1.forEach { s ->
					if (s.value.contains(e.key)) {
						shouldInclude = false
					}
				}
			}
			if (shouldInclude) {
				available.add(e.key)
			}
		}
		val min = available.min()
		firstPart.append(min ?: "")
		map1.remove(min)
	} while(available.size > 0)

	println("First part: ${firstPart.toString()}")

	var second = 0
	val workers = Array(5, { Worker('.', 0, it) } )
	val availableWorkers = hashSetOf(0, 1, 2, 3, 4)
	val tasksAvailable = HashSet<Char>()
	val tasksBeingDone = HashSet<Char>()
	var updateTasks = true
	do {
		if (updateTasks) {
			updateTasks = false
			map2.forEach { e ->
				var shouldInclude = true
				e.value.forEach { 
					map2.forEach { s ->
						if (s.value.contains(e.key)) {
							shouldInclude = false
						}
					}
				}
				if (shouldInclude && !tasksBeingDone.contains(e.key)) {
					tasksAvailable.add(e.key)
				}
			}
		}

		// disposeTasks
		if (tasksBeingDone.size <= 5 && tasksAvailable.size > 0) {
			while (availableWorkers.size > 0) {
				val worker = availableWorkers.first()	
				availableWorkers.remove(worker)	
				val min = tasksAvailable.min() ?: '.'
				tasksAvailable.remove(min)
				if (min != '.') { tasksBeingDone.add(min) }
				workers[worker].task = min
				workers[worker].time = if (min == '.') 0 else (min + 1 - 'A').toInt() + 60
			}
		}
		// update workers state
		workers.forEach {
			it.time -= if (it.time > 0) 1 else 0
			if (it.time == 0) {
				availableWorkers.add(it.id)	
				tasksBeingDone.remove(it.task)
				updateTasks = true
				map2.remove(it.task)
			}
		}
		second++
	} while(map2.size > 0)//tasksAvailable.size + tasksBeingDone.size > 0)
	println("Second part: ${second}")
}
