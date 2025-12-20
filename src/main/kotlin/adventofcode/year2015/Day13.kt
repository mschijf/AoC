package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import tool.mylambdas.collectioncombination.makeAllPermutations
import java.lang.Math.floorMod

fun main() {
    Day13(test=false).showResult()
}

class Day13(test: Boolean) : PuzzleSolverAbstract(test) {

    private val guestMap = inputLines.associate {
        it.dropLast(1).split("\\s".toRegex()).run { Pair(this.first(), this.last()) to (if (this[2] == "lose") -1 else 1) * this[3].toInt() }
    }

    private val guestList = guestMap.keys.map {it.first}.distinct()

    override fun resultPartOne(): Any {
        return guestList.makeAllPermutations().maxOf { it.tableSittingValue() }
    }

    override fun resultPartTwo(): Any {
        return (guestList+"MySelf").makeAllPermutations().maxOf { it.tableSittingValue() }
    }

    private fun List<String>.tableSittingValue(): Int {
        var sum = 0
        this.forEachIndexed { index, name ->
            val leftIndex = floorMod(index - 1, this.size)
            val rightIndex = floorMod(index + 1, this.size)
            sum += guestMap.getOrDefault(Pair(name, this[leftIndex]), 0)
            sum += guestMap.getOrDefault(Pair(name, this[rightIndex]), 0)
        }
        return sum
    }

}


