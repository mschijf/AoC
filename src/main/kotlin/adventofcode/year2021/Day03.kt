package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import kotlin.collections.first

fun main() {
    Day03(test=false).showResult()
}

class Day03(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    val bitNumberList = inputLines

    override fun resultPartOne(): Any {
        return bitNumberList.gammaRate() * bitNumberList.epsilonRate()
    }

    override fun resultPartTwo(): Any {
        return bitNumberList.getCO2ScrubberRating() * bitNumberList.getOxygenGeneratorRating()
    }
}

private fun List<String>.epsilonRate() : Int {
    return (0..this.first().length-1)
        .map { index -> this.leastCommonValue(index) }
        .joinToString("").toInt(2)
}

private fun List<String>.gammaRate() : Int {
    return (0..this.first().length-1)
        .map { index -> this.mostCommonValue(index) }
        .joinToString("").toInt(2)
}

private fun List<String>.mostCommonValue(idx: Int): Char {
    val count1 = this.count { it[idx] == '1'}
    val count0 = this.count() - count1
    return if (count1 >= count0) '1' else '0'
}

private fun List<String>.leastCommonValue(idx: Int): Char {
    val count1 = this.count { it[idx] == '1'}
    val count0 = this.count() - count1
    return if (count0 <= count1) '0' else '1'
}

private fun List<String>.keepNumbers(idx: Int, bitValue: Char): List<String> {
    return this.filter { bitString -> bitString[idx] == bitValue }
}

private fun List<String>.getOxygenGeneratorRating(): Int {
    var current = this
    var idx = 0
    while (current.size > 1) {
        current = current.keepNumbers(idx, current.mostCommonValue(idx))
        idx ++
    }
    return current.first().toInt(2)
}

private fun List<String>.getCO2ScrubberRating(): Int {
    var current = this
    var idx = 0
    while (current.size > 1) {
        current = current.keepNumbers(idx, current.leastCommonValue(idx))
        idx ++
    }
    return current.first().toInt(2)
}

