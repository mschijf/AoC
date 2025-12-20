package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.mylambdas.substringBetween
import java.lang.Exception
import kotlin.math.max

fun main() {
    Day06(test=false).showResult()
}

class Day06(test: Boolean) : PuzzleSolverAbstract(test) {

    //turn off 660,55 through 986,197
    private val actionList = inputLines.map{ it.toTriple() }

    override fun resultPartOne(): Any {
        val grid = Array(1000){  BooleanArray(1000) { false}  }
        actionList.forEach { action -> grid.doAction(action) }
        return grid.sumOf { row -> row.count { cell -> cell } }
    }

    override fun resultPartTwo(): Any {
        val grid = Array(1000){  IntArray(1000) { 0 }  }
        actionList.forEach { action -> grid.doAction(action) }
        return grid.sumOf { row -> row.sum() }
    }


    private fun Array<IntArray>.doAction(action: Triple<Point, Point, Action>) {
        for (y in action.first.y .. action.second.y)
            for (x in action.first.x .. action.second.x)
                when (action.third) {
                    Action.ON -> this[y][x]++
                    Action.OFF -> this[y][x] = max(this[y][x]-1, 0)
                    Action.TOGGLE -> this[y][x]+=2
                }
    }

    private fun Array<BooleanArray>.doAction(action: Triple<Point, Point, Action>) {
        for (y in action.first.y .. action.second.y)
            for (x in action.first.x .. action.second.x)
                when (action.third) {
                    Action.ON -> this[y][x] = true
                    Action.OFF -> this[y][x] = false
                    Action.TOGGLE -> this[y][x] = !this[y][x]
                }
    }

    private fun String.toTriple(): Triple<Point, Point, Action> {
        return if (this.startsWith("turn off")) {
            Triple(
                pos(this.substringBetween("off ", " through")),
                pos(this.substringAfter("through ")),
                Action.OFF
            )
        } else if (this.startsWith("turn on")) {
            Triple(
                pos(this.substringBetween("on ", " through")),
                pos(this.substringAfter("through ")),
                Action.ON
            )
        } else if (this.startsWith("toggle")) {
            Triple(
                pos(this.substringBetween("toggle ", " through")),
                pos(this.substringAfter("through ")),
                Action.TOGGLE
            )
        } else {
            throw Exception("strange...")
        }
    }
}

enum class Action {
    ON, OFF, TOGGLE
}


