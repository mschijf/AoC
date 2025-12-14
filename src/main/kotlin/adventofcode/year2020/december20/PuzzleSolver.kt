package adventofcode.year2020.december20

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.splitByCondition

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val tileList = inputLines.splitByCondition { it.isEmpty() }.map { Tile(it) }
    init {
        tileList.forEach {it.updateMatchList(tileList)}
    }

    override fun resultPartOne(): String {
        val squareArrangement = SquareArrangement(tileList)
        val arrangement = squareArrangement.getFirstArrangement()
        return arrangement.getCornerSquaresProduct().toString()
    }

    override fun resultPartTwo(): String {
        val squareArrangement = SquareArrangement(tileList)
        val arrangement = squareArrangement.getArrangementWithSeaMonsters()
        return arrangement.getRoughness().toString()
    }

}