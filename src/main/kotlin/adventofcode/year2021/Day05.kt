package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.LinePiece
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import kotlin.math.sign

fun main() {
    Day05(test=false).showResult()
}

class Day05(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    val linePieceList = inputLines.map { it.split(" -> ").map { pos(it) } }.map { LinePiece(it[0], it[1]) }

    override fun resultPartOne(): Any {
        return linePieceList
            .filter { it.isHorizontal || it.isVertical }
            .flatMap { it.toPointSet() }
            .groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .size
    }

    override fun resultPartTwo(): Any {
        return linePieceList
            .filter { it.isHorizontal || it.isVertical || it.isDiagonal }
            .flatMap { it.toPointSet() }
            .groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .size
    }
}

private fun LinePiece.toPointSet() : Set<Point> {
    return if (this.isHorizontal) {
        (this.minX .. this.maxX).map { x -> pos(x, this.minY) }.toSet()
    } else if (this.isVertical) {
        (this.minY .. this.maxY).map { y -> pos(this.minX, y) }.toSet()
    } else if (this.isDiagonal) {
        val signY = (this.to.y - this.from.y).sign
        if (this.to.x > this.from.x)
            (this.from.x .. this.to.x).mapIndexed { idx, x -> pos(x, this.from.y + idx * signY) }.toSet()
        else
            (this.from.x downTo this.to.x).mapIndexed { idx, x -> pos(x, this.from.y + idx * signY) }.toSet()
    } else {
        emptySet<Point>()
    }
}

//
//private fun LinePiece.overlappingPoints(other: LinePiece): Int {
//    //assuming horizontal or vertical lines
//
//    if (this.isHorizontal && other.isHorizontal) {
//        return if (this.minY == other.minY) {
//            val diff = min(this.maxX, other.maxX) - max(this.minX, other.minX)
//            if (diff < 0)
//                0
//            else
//                diff + 1
//        } else {
//            0
//        }
//    } else if (this.isVertical && other.isVertical) {
//        return if (this.minX == other.minX) {
//            val diff = min(this.maxY, other.maxY) - max(this.minY, other.minY)
//            if (diff < 0)
//                0
//            else
//                diff + 1
//        } else {
//            0
//        }
//    } else {
//
//    }
//}
//
//
