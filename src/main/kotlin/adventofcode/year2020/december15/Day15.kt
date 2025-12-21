package adventofcode.year2020.december15

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day15(test=false).showResult()
}

class Day15(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val numberList = inputLines.first().split(",").map { it.toInt() }.toMutableList()
        val size = numberList.size
        repeat(30 - size) {
            val lastNumber = numberList.removeLast()
            val index = numberList.lastIndexOf(lastNumber)
            val newNumber = if (index >= 0) numberList.size - index else 0
            numberList.add(lastNumber)
            numberList.add(newNumber)
//            println("$it   ${numberList.last()}")
        }
        return numberList.last().toString()
    }

    override fun resultPartTwo(): String {
        val lastCalledMap = inputLines.first()
            .split(",").dropLast(1)
            .withIndex()
            .associate { it.value.toInt() to it.index }
            .toMutableMap()

        val size = lastCalledMap.size
        var lastNumber = inputLines.first().split(",").last().toInt()
        repeat(30_000_000 - size -1) { index ->
//            println("Turn ${index+size+1}   $lastNumber")
            val newLastNumber = if (lastCalledMap.contains(lastNumber)) {
                (index+size) - lastCalledMap[lastNumber]!!
            } else {
                0
            }
            lastCalledMap[lastNumber] = index+size
            lastNumber = newLastNumber
        }
        return lastNumber.toString()
    }

}


