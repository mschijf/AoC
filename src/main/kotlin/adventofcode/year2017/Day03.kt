package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.spiral.spiralIndexToPoint
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.printAsGrid

fun main() {
    Day03(test=false).showResult()
}

class Day03(test: Boolean) : PuzzleSolverAbstract(test) {

    private val puzzleInput = inputLines.first().toInt()

    override fun resultPartOne(): Any {
        val xx = (1..25).associateWith { it.spiralIndexToPoint() }
//        (1..25).associateBy { it.spiralIndexToPoint()  }.printAsGrid { "%25s".format(xx[it]!!) }
        val origin = 1.spiralIndexToPoint()
        return puzzleInput.spiralIndexToPoint().distanceTo(origin)
    }

    override fun resultPartTwo(): Any {
        val set = mutableMapOf<Point, Int>()
        var newSum = 1
        set[1.spiralIndexToPoint()] = newSum
        var i = 2
        while (newSum < puzzleInput) {
            val point = i.spiralIndexToPoint()
            val neighbors = point.allWindDirectionNeighbors()
            newSum = neighbors.sumOf { set[it]?:0 }
            set[i.spiralIndexToPoint()] = newSum
            i++
        }

        return newSum
    }

}


