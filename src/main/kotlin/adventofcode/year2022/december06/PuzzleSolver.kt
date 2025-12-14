package adventofcode.year2022.december06

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.distinct

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        return firstWindowWithAllDifferentCharacters(4).toString()
    }

    override fun resultPartTwo(): String {
        return firstWindowWithAllDifferentCharacters(14).toString()
    }

    private fun firstWindowWithAllDifferentCharacters(windowSize: Int): Int {
        return inputLines
            .first()
            .windowed(windowSize)
            .indexOfFirst{it.distinct().length == it.length}
            .plus(windowSize)
    }
}

//----------------------------------------------------------------------------------------------------------------------
