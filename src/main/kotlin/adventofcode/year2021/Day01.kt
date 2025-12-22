package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day01(test=false).showResult()
}

class Day01(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    override fun resultPartOne(): Any {
        return inputLines
            .map {it.toInt()}
            .zipWithNext { a, b -> b > a }
            .count { it }
    }

    override fun resultPartTwo(): Any {
        return inputLines
            .map {it.toInt()}
            .windowed(3)
            .map { it.sum()}
            .zipWithNext { a, b -> b > a }
            .count { it }
    }
}


