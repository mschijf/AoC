package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day1501(test = false).showResult()
}

class Day1501(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return inputLines.first()
            .map { if (it == '(') 1 else -1 }
            .sum()
    }

    override fun resultPartTwo(): Any {
        return inputLines.first()
            .runningFold(0) { acc, c -> if (c == '(') acc + 1 else acc - 1 }
            .indexOfFirst { it == -1 }
    }
}


