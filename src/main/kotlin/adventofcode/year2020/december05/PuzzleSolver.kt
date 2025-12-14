package adventofcode.year2020.december05

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val boardingpassList = inputLines.map { toSeatId(it) }

    override fun resultPartOne(): String {
        return boardingpassList.max().toString()
    }

    override fun resultPartTwo(): String {
        val sortedBoardingPass = boardingpassList.sorted()
        for (i in 1 until sortedBoardingPass.size-1) {
            if (sortedBoardingPass[i-1] == sortedBoardingPass[i] - 2)
                return (sortedBoardingPass[i] - 1).toString()
        }
        return "NOT FOUND"
    }


    private fun toSeatRow(bp: String): Int {
        return bp.substring(0,7)
            .replace('F', '0').replace('B', '1')
            .toInt(2)
    }

    private fun toSeatCol(bp: String): Int {
        return bp.substring(7)
            .replace('L', '0').replace('R', '1')
            .toInt(2)
    }
    private fun toSeatId(bp: String) = 8*toSeatRow(bp) + toSeatCol(bp)
}

