package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point

fun main() {
    Day09(test=true).showResult()
}

class Day09(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    private val caveMap = inputLines.asGrid().mapValues { (key, ch) -> ch.digitToInt() }

    override fun resultPartOne(): Any {
        return caveMap.keys.filter { it.isLowest() }.sumOf { lowPoint -> lowPoint.riskValue()}
    }

    override fun resultPartTwo(): Any {
        return caveMap.keys.filter { it.isLowest() }
            .map { it.basinSize() }
            .sortedDescending()
            .take (3)
            .fold (1) { acc, basinSize -> acc * basinSize }
    }

    private fun Point.isLowest(): Boolean =
        this.neighbors().filter { nb -> nb.inCave() }.all{ nb -> caveMap[nb]!! > caveMap[this]!!}

    private fun Point.inCave(): Boolean =
        this in caveMap.keys

    private fun Point.riskValue() =
        caveMap[this]!! + 1

    private fun Point.basinSize(basin: MutableSet<Point> = mutableSetOf()): Int {
        if (this in basin)
            return 0

        basin += this
        return 1 + this.neighbors()
            .filter { nb -> nb.inCave() && caveMap[nb]!! < 9 }
            .sumOf { nb -> nb.basinSize(basin) }
    }
}


