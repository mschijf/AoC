package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import kotlin.math.absoluteValue

fun main() {
    Day07(test=false).showResult()
}

class Day07(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    private val distanceList = inputLines.first().split(",").map {it.toInt()}
    private val minHor = distanceList.min()
    private val maxHor = distanceList.max()

    override fun resultPartOne(): Any {
        return (minHor .. maxHor).minOf { distanceList.fuelCosts(it)}
    }

    override fun resultPartTwo(): Any {
        return (minHor .. maxHor).minOf { distanceList.increasingFuelCosts(it)}
    }
}

private fun List<Int>.fuelCosts(horPosition: Int): Int {
    return this.sumOf { (it - horPosition).absoluteValue }
}

private fun List<Int>.increasingFuelCosts(horPosition: Int): Int {
    return this.sumOf { (it - horPosition).absoluteValue.sumOfFirstNaturalNumbers() }
}

private fun Int.sumOfFirstNaturalNumbers() : Int {
    return this * (this+1) / 2
}

