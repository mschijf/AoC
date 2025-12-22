package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day06(test = false).showResult()
}

class Day06(test: Boolean) : PuzzleSolverAbstract(test, puzzleName = "TBD", hasInputFile = true) {

    private val lanternFishCount = inputLines.first().split(",").map { it.toInt() }.groupingBy { it }.eachCount()

    override fun resultPartOne(): Any {
        var current = (0..8).associate { Pair(it, lanternFishCount.getOrDefault(it, 0).toLong()) }
        repeat(80) {
            current = current.processDay()
        }
        return current.values.sum()
    }

    override fun resultPartTwo(): Any {
        var current = (0..8).associate { Pair(it, lanternFishCount.getOrDefault(it, 0).toLong()) }
        repeat(256) {
            current = current.processDay()
        }
        return current.values.sum()
    }
}

private fun Map<Int, Long>.processDay(): Map<Int, Long> {
    return this.map { (timer, count) ->
        when (timer) {
            0 -> Pair(8, count)
            7 -> Pair(6, count + this.getOrDefault(0, 0L))
            else -> Pair(timer - 1, count)
        }
    }.toMap()
}

