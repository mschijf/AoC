package adventofcode.year2016

import adventofcode.PuzzleSolverAbstract

import tool.coordinate.twodimensional.Direction
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day1601(test=false).showResult()
}

class Day1601(test: Boolean) : PuzzleSolverAbstract(test) {

    private val moves = inputLines.first().split(", ").map{Pair(it[0], it.drop(1).toInt())}

    override fun resultPartOne(): Any {
        var pos = pos(0,0)
        var dir = Direction.UP
        moves.forEach {
            dir = if (it.first == 'R') dir.rotateRight() else dir.rotateLeft()
            pos = pos.moveSteps(dir, it.second)
        }
        return pos.distanceTo(pos(0,0))
    }

    override fun resultPartTwo(): Any {
        val visited = mutableSetOf<Point>()
        var pos = pos(0,0)
        var dir = Direction.UP
        visited += pos
        moves.forEach {
            dir = if (it.first == 'R') dir.rotateRight() else dir.rotateLeft()
            repeat(it.second) {
                pos = pos.moveOneStep(dir)
                if (pos in visited) {
                    return pos.distanceTo(pos(0,0))
                }
                visited += pos
            }
        }
        return -1
    }

}


