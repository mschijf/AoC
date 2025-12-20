package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import adventofcode2017.doOneKnotHashCycle
import adventofcode2017.knotHashEncode

fun main() {
    Day1710(test=false).showResult()
}

class Day1710(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val numbers = IntArray(if (test) 5 else 256) { it }
        val lengths = inputLines.first().split(",").map{it.trim().toInt()}
        numbers.doOneKnotHashCycle(0, 0, lengths)
        return numbers[0]*numbers[1]
    }

    override fun resultPartTwo(): Any {
        return inputLines.first().knotHashEncode()
    }
}


