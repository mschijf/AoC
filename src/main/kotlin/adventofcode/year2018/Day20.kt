package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day20(test=false).showResult()
}

class Day20(test: Boolean) : PuzzleSolverAbstract(test) {

    private val startPos = pos(3,3)
    private val grid = inputLines.first().makeGrid(startPos)

    override fun resultPartOne(): Any {
        return grid.shortestPathToAll(startPos).values.max()
//        return grid.keys.map{ to -> grid.shortestPath(startPos, to) }.max()
    }

    override fun resultPartTwo(): Any {
        return grid.shortestPathToAll(startPos).values.count { it >= 1000 }
//        return grid.keys.map{ to -> grid.shortestPath(startPos, to) }.count { it >= 1000 }
    }

    private fun String.makeGrid(startPos: Point = pos(0,0)): Map<Point, Set<Point>> {
        val result = mutableMapOf<Point, MutableSet<Point>>()
        var currentPos = startPos
        val stack = ArrayDeque<Point>()
        this.forEach { ch ->
            when(ch) {
                '^', '('  -> stack.addLast(currentPos)
                '$', ')' -> currentPos = stack.removeLast()
                '|' -> currentPos = stack.last()
                else -> {
                    val next = currentPos.neighbor(ch)
                    result.connectRooms(currentPos, next)
                    currentPos = next
                }
            }
        }
        return result
    }

    private fun MutableMap<Point, MutableSet<Point>>.connectRooms(from: Point, to: Point) {
        this.getOrPut(from) { mutableSetOf() }.add(to)
        this.getOrPut(to) { mutableSetOf() }.add(from)
    }

    private fun Point.neighbor(ch: Char): Point {
        return when(ch) {
            'N' -> this.north()
            'S' -> this.south()
            'W' -> this.west()
            'E' -> this.east()
            else -> throw Exception("Surprising character")
        }
    }

//    private fun Map<Point, Set<Point>>.shortestPath(from: Pos, to: Pos): Int {
//        val queue = ArrayDeque<Pair<Point, Int>>()
//        val visited = mutableSetOf<Point>()
//        queue.addFirst(Pair(from,0))
//        while (queue.isNotEmpty()) {
//            val (current, stepsDone) = queue.removeLast()
//            if (current == to)
//                return stepsDone
//
//            this[current]!!.filterNot { it in visited }.forEach { queue.addLast(Pair(it, stepsDone+1)) }
//            visited.add(current)
//        }
//        return -1
//    }

    private fun Map<Point, Set<Point>>.shortestPathToAll(from: Point): Map<Point, Int> {
        val result = mutableMapOf<Point, Int>()
        val queue = ArrayDeque<Pair<Point, Int>>()
        val visited = mutableSetOf<Point>()
        queue.addFirst(Pair(from,0))
        while (queue.isNotEmpty()) {
            val (current, stepsDone) = queue.removeLast()
            result[current] = stepsDone
            this[current]!!.filterNot { it in visited }.forEach { queue.addLast(Pair(it, stepsDone+1)) }
            visited.add(current)
        }
        return result
    }


}


