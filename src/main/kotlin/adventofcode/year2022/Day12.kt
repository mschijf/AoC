package adventofcode.year2022

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import java.util.*

fun main() {
    Day12(test=false).showResult()
}

class Day12(test: Boolean) : PuzzleSolverAbstract(test) {

    private val grid = inputLines.map{it.toList()}

    override fun resultPartOne(): String {
        val stepCount = doSolve(findStartPos())
        return stepCount.toString()
    }

    override fun resultPartTwo(): String {
        return getStartPosList()
            .map{startPos -> doSolve(startPos)}
            .filter { stepCount -> stepCount > 0 }
            .min()
            .toString()
    }

    private fun doSolve(startPos: Point): Int {
        val visitedAfterStepsTaken = List(grid.size) { row -> MutableList(grid[row].size) {0} }
        val stepQueue : Queue<Point> = LinkedList()
        stepQueue.add(startPos)
        while (stepQueue.isNotEmpty()) {
            val current = stepQueue.remove()
            val stepsDone = visitedAfterStepsTaken[current.y][current.x]
            if (grid[current.y][current.x] == 'E') {
                return stepsDone
            } else {
                current
                    .neighBoursInGrid(grid)
                    .filter {neighbour -> isLegalStep(current, neighbour) && visitedAfterStepsTaken[neighbour.y][neighbour.x] <= 0}
                    .forEach { neighbour ->
                        stepQueue.add(neighbour)
                        visitedAfterStepsTaken[neighbour.y][neighbour.x] = stepsDone + 1
                    }
            }
        }
        return -1
    }

    private fun findStartPos(): Point {
        for (row in grid.indices)
            for (col in grid[row].indices)
                if (grid[row][col] == 'S')
                    return pos(col, row)
        return (pos(0, 0))
    }

    private fun getStartPosList(): List<Point> {
        return grid
            .mapIndexed{ rowIndex, row -> row.mapIndexed { colIndex, col -> Pair(pos(colIndex, rowIndex), col)}}
            .flatten()
            .filter {it.second == 'a' || it.second == 'S'}
            .map {it.first}
    }

    private fun isLegalStep(fromPos: Point, toPos: Point): Boolean {
        return (letterValue(grid[toPos.y][toPos.x]) - letterValue(grid[fromPos.y][fromPos.x]) <= 1)
    }

    private fun letterValue(letter: Char) = (if (letter == 'S') 'a' else if (letter == 'E') 'z' else letter) - 'a'

}

//----------------------------------------------------------------------------------------------------------------------

private fun Point.neighBoursInGrid(grid: List<List<Any>>): List<Point> {
    val result = mutableListOf<Point>()
    if (up().isOnGrid(grid)) result.add(up())
    if (down().isOnGrid(grid)) result.add(down())
    if (left().isOnGrid(grid)) result.add(left())
    if (right().isOnGrid(grid)) result.add(right())
    return result
}

private fun Point.isOnGrid(grid: List<List<Any>>) = (y >= 0 && y < grid.size && x >= 0 && x < grid[y].size)
