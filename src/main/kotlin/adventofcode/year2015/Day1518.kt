package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day1518(test=false).showResult()
}

class Day1518(test: Boolean) : PuzzleSolverAbstract(test) {
    private val grid = inputLines.flatMapIndexed{y, row -> row.mapIndexed {x, char -> pos(x,y) to (char == '#')  }}.toMap()

    override fun resultPartOne(): Any {
        var myGrid = grid
        repeat(if (test) 4 else 100) {
            myGrid = myGrid.nextGen()
        }
        return myGrid.values.count { it }
    }

    override fun resultPartTwo(): Any {
        val maxX = grid.keys.maxOf { it.x }
        val maxY = grid.keys.maxOf { it.y }
        val cornersOn = mapOf(pos(0,0) to true, pos(maxX,0) to true, pos(0,maxY) to true, pos(maxX,maxY) to true )

        var myGrid = grid + cornersOn
        repeat(if (test) 5 else 100) {
            myGrid = myGrid.nextGen() + cornersOn
        }
        return myGrid.values.count { it }
    }


    private fun Map<Point, Boolean>.nextGen() : Map<Point, Boolean> {
        return this.mapValues {
            if (it.value) {
                it.key.allWindDirectionNeighbors().count { nb -> this.getOrDefault(nb, false) } in 2..3
            } else {
                it.key.allWindDirectionNeighbors().count { nb -> this.getOrDefault(nb, false) } == 3
            }
        }
    }
}


