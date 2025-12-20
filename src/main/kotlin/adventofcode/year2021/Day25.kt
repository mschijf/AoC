package adventofcode.year2021

import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day25(test=false).showResult()
}

class Day25(test: Boolean) : PuzzleSolverAbstract(test) {

    private var seaFloor = inputLines
        .flatMapIndexed { y, line -> line.mapIndexed { x, ch ->  pos(x,y) to ch} }
        .toMap()
        .filterValues {it != '.'}
        .toMutableMap()

    private val maxX = seaFloor.keys.maxOf { it.x }
    private val maxY = seaFloor.keys.maxOf { it.y }

    override fun resultPartOne(): String {
        var count = 0
        do {
            val moved = seaFloor.moveOneStep()
            count++
        } while (moved)
//        seaFloor.printAsGrid { it.toString() }
        return count.toString()
    }

    override fun resultPartTwo(): String {
        return "no part2"
    }

    private fun MutableMap<Point, Char>.moveOneStep(): Boolean {
        val eastMovers = seaFloor.eastMovers()
        eastMovers.forEach { seaFloor.remove(it) }
        seaFloor.putAll(eastMovers.moveEast().associateWith { '>' })
        val southMovers = seaFloor.southMovers()
        southMovers.forEach { seaFloor.remove(it) }
        seaFloor.putAll(southMovers.moveSouth().associateWith { 'v' })
        return eastMovers.isNotEmpty() || southMovers.isNotEmpty()
    }


    private fun Map<Point, Char>.eastMovers(): List<Point>  {
        val xx = this.filterValues { it == '>' }.keys
        val yy = xx.map { it.east().moduloEast() }
        val zz = yy.filter{ it !in this.keys }
        return this.filterValues { it == '>' }.keys.filter { it.east().moduloEast() !in this.keys }
    }

    private fun List<Point>.moveEast(): List<Point>  {
        return this.map { it.east().moduloEast()}
    }

    private fun Map<Point, Char>.southMovers(): List<Point>  {
        return this.filterValues { it == 'v' }.keys.filter { it.south().moduloSouth() !in this.keys }
    }

    private fun List<Point>.moveSouth(): List<Point>  {
        return this.map { it.south().moduloSouth()}
    }

    private fun Point.moduloEast() : Point {
        return pos(x % (maxX+1), y)
    }

    private fun Point.moduloSouth() : Point {
        return pos(x, y % (maxY+1))
    }
}