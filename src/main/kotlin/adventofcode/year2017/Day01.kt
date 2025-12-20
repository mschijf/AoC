package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day01(test=false).showResult()
}

class Day01(test: Boolean) : PuzzleSolverAbstract(test) {

    private val list = inputLines.first().map { it.digitToInt() }

    override fun resultPartOne(): Any {
        return list.filterIndexed { index, i -> i == list[(index+1) % list.size] }.sum()
    }

    override fun resultPartTwo(): Any {
        return list.filterIndexed { index, i -> i == list[(index+list.size/2) % list.size] }.sum()
    }
}


