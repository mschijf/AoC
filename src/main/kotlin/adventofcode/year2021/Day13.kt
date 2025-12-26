package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.coordinate.twodimensional.printAsGrid
import tool.mylambdas.splitByCondition

fun main() {
    Day13(test=false).showResult()
}

class Day13(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    private val dotSet = inputLines.splitByCondition { it.isBlank() }.first().map { pos(it)}.toSet()
    private val foldActionList = inputLines.splitByCondition { it.isBlank() }.last().map { FoldLine.of(it)}

    override fun resultPartOne(): Any {
        val foldAction = foldActionList.first()
        return dotSet.map { dot -> dot.foldALongLine(foldAction)}.toSet().size
    }

    override fun resultPartTwo(): Any {
        foldActionList
            .fold(dotSet) { acc, foldLine -> acc.map { dot -> dot.foldALongLine(foldLine) }.toSet() }
            .printAsGrid("..", "##")
        return "^^^^ (HZLEHJRK)"
    }

    private fun Point.foldALongLine(foldLine: FoldLine): Point {
        return if (foldLine.along == 'y') {
            if (this.y <= foldLine.lineLevel) {
                this
            } else {
                pos(this.x, 2 * foldLine.lineLevel - this.y)
            }
        } else {
            if (this.x <= foldLine.lineLevel) {
                this
            } else {
                pos(2 * foldLine.lineLevel - this.x, this.y)
            }
        }
    }
}

data class FoldLine(val along: Char, val lineLevel: Int) {
    companion object {
        fun of(raw: String): FoldLine {
            return FoldLine (
                along = raw.substringAfter("fold along ").first(),
                lineLevel = raw.substringAfter("=").toInt()
            )
        }
    }
}


