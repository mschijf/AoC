package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point

import tool.coordinate.twodimensional.pos
import tool.coordinate.twodimensional.printAsGrid

fun main() {
    Day10(test=false).showResult()
}

class Day10(test: Boolean) : PuzzleSolverAbstract(test) {

    //position=< 9,  1> velocity=< 0,  2>
    private val coordList = inputLines.map{ pos(it.substringAfter("position=<").substringBefore(">")) }
    private val velocityList = inputLines.map{ pos(it.substringAfter("velocity=<").substringBefore(">")) }

    override fun resultPartOne(): Any {
        var iterationCount = 0
        var last: List<Point>
        var next = coordList
        do {
            last = next
            next = last.mapIndexed { index, coordinate -> coordinate.plusXY(velocityList[index].x, velocityList[index].y ) }
            ++iterationCount
        } while (next.area() < last.area())
        last.toSet().printAsGrid("..", "##")
        println("Seconds waited (part2): ${iterationCount-1}")
        return ""
    }

    override fun resultPartTwo(): Any {
        return "^^^^^^^"
    }

    private fun List<Point>.area(): Long {
        if (this.isEmpty())
            return 0
        val minX = this.minOf { it.x }
        val minY = this.minOf { it.y }
        val maxX = this.maxOf { it.x }
        val maxY = this.maxOf { it.y }
        return (maxX - minX + 1L) * (maxY - minY + 1L)
    }

}


