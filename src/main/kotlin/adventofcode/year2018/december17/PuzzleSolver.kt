package adventofcode.year2018.december17

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.coordinate.twodimensional.printGrid

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val waterSpring = pos(500,0)
    private val claySet = inputLines.flatMap{ line -> line.toPosList()}.toSet()
    private val minX = claySet.minOf { it.x }
    private val minY = claySet.minOf { it.y }
    private val maxX = claySet.maxOf { it.x }
    private val maxY = claySet.maxOf { it.y }
    private val upperLeft = pos(minX-1, 0)
    private val lowerRight = pos(maxX+1, maxY+1)

    private val wetSandSet = mutableSetOf<Point>()
    private val waterSet = mutableSetOf<Point>()

    override fun resultPartOne(): Any {
        dropDown(waterSpring)
        //printWaterWell()
        return (waterSet + wetSandSet).filter { it.y in minY..maxY }.size
    }

    override fun resultPartTwo(): Any {
        return waterSet.size
    }

    private fun printWaterWell() {
        Pair(upperLeft, lowerRight).printGrid { c ->
            when (c) {
                in setOf(waterSpring)-> "+"
                in claySet -> "#"
                in waterSet -> "~"
                in wetSandSet -> "|"
                else -> "."
            }
        }
    }

    private fun String.toPosList(): List<Point> {
        return when {
            this.startsWith("x") -> {
                val x = this.substringAfter("x=").substringBefore(",").toInt()
                val yRange = this.substringAfter("y=").substringBefore("..").toInt()..this.substringAfter("..").toInt()
                yRange.map { y -> pos(x, y) }
            }

            this.startsWith("y") ->  {
                val y = this.substringAfter("y=").substringBefore(",").toInt()
                val xRange = this.substringAfter("x=").substringBefore("..").toInt()..this.substringAfter("..").toInt()
                xRange.map { x -> pos(x, y) }
            }

            else -> throw Exception("unexpected start of string")
        }
    }

    private fun dropDown(c: Point) {
        if (c in claySet || c.y > maxY)
            return
        if (c in wetSandSet)
            return
        if (c in waterSet)
            return

        wetSandSet += c

        dropDown(c.down())
        if (c.down() in claySet || c.down() in waterSet) {
            dropDown(c.left())
            dropDown(c.right())
        }

        if (c.canBeFilled()) {
            c.fillWithWater()
        }
    }
    private fun Point.canBeFilled(): Boolean {
        var walker = this.left()
        while (walker !in claySet && (walker.plusY(1) in claySet || walker.plusY(1) in waterSet)) {
            walker = walker.left()
        }
        if (walker !in claySet)
            return false

        walker = this.right()
        while (walker !in claySet && (walker.plusY(1) in claySet || walker.plusY(1) in waterSet)) {
            walker = walker.right()
        }
        if (walker !in claySet)
            return false
        return true
    }

    private fun Point.fillWithWater() {
        waterSet += this
        var walker = this.left()
        while (walker !in claySet) {
            waterSet += walker
            walker = walker.left()

        }
        walker = this.right()
        while (walker !in claySet) {
            waterSet += walker
            walker = walker.right()
        }
    }

}


