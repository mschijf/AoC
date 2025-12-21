package adventofcode.year2019

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day01(test=false).showResult()
}

class Day01(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        return inputLines.sumOf { it.toInt() / 3 - 2 }.toString()
    }

    override fun resultPartTwo(): String {
        return inputLines.sumOf { fuelNeeded(it.toInt() / 3 - 2) }.toString()
    }

    private fun fuelNeeded(module: Int): Int {
        return if (module <= 0)
            0
        else
            module + fuelNeeded(module / 3 - 2)
    }
}


