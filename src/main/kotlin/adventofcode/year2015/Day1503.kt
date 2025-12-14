package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import tool.coordinate.twodimensional.Direction
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day1503(test=false).showResult()
}

class Day1503(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return inputLines.first().createSet().size
    }

    override fun resultPartTwo(): Any {
        val santaSet = inputLines.first().filterIndexed { index, c ->  index % 2 == 0}.createSet()
        val roboSet = inputLines.first().filterIndexed { index, c ->  index % 2 == 1}.createSet()
        return (santaSet + roboSet).size
    }

    private fun String.createSet():Set<Point> {
        var start = pos(0,0)
        val result = mutableSetOf(start)
        this.forEach { ch ->
            start = start.moveOneStep(Direction.ofSymbol(ch.toString()))
            result.add(start)
        }
        return result
    }
}


