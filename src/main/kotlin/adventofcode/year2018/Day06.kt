package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day06(test=false).showResult()
}

class Day06(test: Boolean) : PuzzleSolverAbstract(test) {
    private val coordList = inputLines.map { pos(it)}

    private val maxX = coordList.maxOf{ it.x }
    private val maxY = coordList.maxOf{ it.y }

    override fun resultPartOne(): Any {
        val gridMap = createMapPuzzleOne()
//        gridMap.printAsGrid { it.gridValueToString() }
        val singleFields = gridMap.filter { it.value.size == 1 }.map{it.key to it.value.first()}.toMap()
        val borderFields = singleFields.filter{it.key.isBorder()}.values.toSet()
        val resultFields = singleFields.values.filterNot{it in borderFields}.groupingBy { it }.eachCount()
        return resultFields.values.max()
    }

    override fun resultPartTwo(): Any {
        val maxManhattanDistance = if (test) 32 else 10_000
        val gridMap = createMapPuzzleTwo()
//        gridMap.printAsGrid { item -> if (item < 32) "#" else "." }
        return gridMap.values.count { it < maxManhattanDistance }
    }

    private fun List<Point>.gridValueToString(): String {
        return if (this.size > 1) "." else { ('a'+ coordList.indexOf( this.first() )).toString() }
    }

    private fun Point.isBorder() : Boolean {
        return this.x == 0 || this.y == 0 || this.x == maxX || this.y == maxY
    }


    private fun createMapPuzzleOne(): Map<Point, List<Point>> {
        return (0..maxX+1).flatMap { x -> (0..maxY).map { y -> pos(x, y) } }
            .associateWith { coord -> coordList.groupBy { it.distanceTo(coord) }.minBy { it.key }.value }
    }

    private fun createMapPuzzleTwo(): Map<Point, Int> {
        return (0..maxX).flatMap { x -> (0..maxY).map { y -> pos(x, y) } }
            .associateWith { coord -> coordList.sumOf { it.distanceTo(coord) } }
    }

//    private fun Map<GridPos, List<GridPos>>.print() {
//        val maxX = this.keys.maxOf{ it.x }
//        val maxY = this.keys.maxOf{ it.y }
//        for (y in 0..maxY) {
//            for (x in 0..maxX) {
//                if (this[GridPos(x,y)]!!.size > 1) {
//                    print (".")
//                } else {
//                    val index = coordList.indexOf( this[GridPos(x,y)]!!.first() )
//                    print('a'+index)
//                }
//            }
//            println()
//        }
//    }

    private fun Map<Point, Int>.print2() {
        val maxX = this.keys.maxOf{ it.x }
        val maxY = this.keys.maxOf{ it.y }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                if (this[pos(x,y)]!! < 32) {
                    print ("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }
}


