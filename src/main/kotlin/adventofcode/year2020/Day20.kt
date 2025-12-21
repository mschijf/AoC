package adventofcode.year2020

import adventofcode.PuzzleSolverAbstract
import adventofcode.year2020.toolday20.SquareArrangement
import adventofcode.year2020.toolday20.Tile
import tool.mylambdas.splitByCondition

fun main() {
    Day20(test=false).showResult()
}

class Day20(test: Boolean) : PuzzleSolverAbstract(test) {

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