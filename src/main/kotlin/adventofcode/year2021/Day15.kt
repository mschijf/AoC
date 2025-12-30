package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import java.util.PriorityQueue

fun main() {
    Day15(test=false).showResult()
}

class Day15(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {


    private val riskLevelMap = inputLines.asGrid().mapValues { it.value.digitToInt() }

    override fun resultPartOne(): Any {
        return riskLevelMap.shortestPathPriorityQueue()
    }

    override fun resultPartTwo(): Any {
        return riskLevelMap.explodeFiveTimes().shortestPathPriorityQueue()
    }

    private fun Map<Point, Int>.shortestPathPriorityQueue():Int {
        val visited = mutableSetOf<Point>()
        val positionsToExamine = PriorityQueue<Pair<Point, Int>>{ p1, p2 -> p1.second - p2.second }
        val from = this.keys.minBy { p -> p.x + 1000 * p.y}
        val to = this.keys.maxBy { p -> p.x + 1000 * p.y}

        positionsToExamine.add(Pair(from, 0))
        while (positionsToExamine.isNotEmpty()) {
            val (current, distanceFromStart) = positionsToExamine.remove()
            if (current == to)
                return distanceFromStart

            if (current !in visited) {
                visited += current
                current.neighbors().filter { nb -> nb in this }.forEach { nextPosition ->
                    val distanceToNextPosition = distanceFromStart + this[nextPosition]!!
                    positionsToExamine.add(Pair(nextPosition, distanceToNextPosition))
                    //^^^ we might add more times a node in the priority queue, but we will always pick the one with the
                    //    lowest risk level first.
                    //    And by checking if we already have visited a node, we can safely do this trick.
                }
            }
        }
        return -1
    }


    private fun Map<Point, Int>.explodeFiveTimes(): Map<Point, Int> {
        val maxPosition = this.keys.maxBy { p -> p.x + 1000*p.y}
        val result = mutableMapOf<Point, Int>()
        for (rptX in 0..4) {
            for (rptY in 0..4) {
                for (x in 0..maxPosition.x) {
                    for (y in 0..maxPosition.x) {
                        result[pos(x + rptX*(maxPosition.x+1),y + rptY*(maxPosition.y+1))] = (this[pos(x,y)]!! + rptX + rptY -1) %9 + 1
                    }
                }
            }
        }
        return result
    }

    /**
     * below a different algorithm with an own kind of priority queue implementation.
     * we keep a map of points, and we ensure that each point only appears once.
     * if we want to add a new one, we only add the one with the lowest risk level. (or actually, we update the risk level for that node)
     */
//    private fun Map<Point, Int>.shortestPath():Int {
//        val visited = mutableSetOf<Point>()
//        val positionsToExamine = mutableMapOf<Point, Int>()
//        val from = this.keys.minBy { p -> p.x + 1000*p.y}
//        val to = this.keys.maxBy { p -> p.x + 1000*p.y}
//
//        positionsToExamine[from] = 0
//        while (positionsToExamine.isNotEmpty()) {
//            val (current, distanceFromStart) = positionsToExamine.minBy { it.value }
//            positionsToExamine.remove(current)
//            if (current == to)
//                return distanceFromStart
//
//            visited += current
//            current.neighbors().filter { nb -> nb in this && nb !in visited}.forEach { nextPosition ->
//                val distanceToNextPosition = distanceFromStart + this[nextPosition]!!
//                if (nextPosition !in positionsToExamine || distanceToNextPosition < positionsToExamine[nextPosition]!!) {
//                    positionsToExamine[nextPosition] = distanceToNextPosition
//                }
//            }
//        }
//        return -1
//    }
//
}

