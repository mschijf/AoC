package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import kotlin.collections.first

fun main() {
    Day08(test=false).showResult()
}

class Day08(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    private val digitList = inputLines.map { line ->
        val patterns = line.split(" | ").first()
        val outputPatterns = line.split(" | ").last()
        Pair(patterns.split(" "), outputPatterns.split(" "))
    }

    override fun resultPartOne(): Any {
        return digitList.sumOf{ (input, output) ->
            output.count { pattern -> pattern.length in setOf(2,3,4,7) } }
    }

    override fun resultPartTwo(): Any {
        return digitList.sumOf { (inputPatterns, outputPatterns) ->
            inputPatterns
                .toNumberPatternMap()
                .getOutputValue(outputPatterns)
        }
    }

    private fun Map<Set<Char>, Int>.getOutputValue(outputList: List<String>) : Int {
        return outputList.fold(0) { acc, pattern -> 10*acc + this[pattern.toSet()]!!}
    }

    private fun List<String>.toNumberPatternMap(): Map<Set<Char>, Int> {
        val numberPatternList = MutableList<Set<Char>>(10) { emptySet() }
        numberPatternList[4] = this.first { it.length == 4 }.toSet()
        numberPatternList[1] = this.first { it.length == 2 }.toSet()
        numberPatternList[7] = this.first { it.length == 3 }.toSet()
        numberPatternList[8] = this.first { it.length == 7 }.toSet()

        numberPatternList[6] = this.first { it.length == 6 && !numberPatternList[1].all { ch -> ch in it}}.toSet()
        numberPatternList[9] = this.first { it.length == 6 && numberPatternList[4].all { ch -> ch in it}}.toSet()
        numberPatternList[0] = this.first { it.length == 6 && it.toSet() != numberPatternList[6] && it.toSet() != numberPatternList[9]}.toSet()

        numberPatternList[3] = this.first { it.length == 5 && numberPatternList[1].all { ch -> ch in it}}.toSet()
        numberPatternList[5] = this.first { it.length == 5 && it.all { ch -> ch in numberPatternList[6]}}.toSet()
        numberPatternList[2] = this.first { it.length == 5 && it.toSet() != numberPatternList[3] && it.toSet() != numberPatternList[5]}.toSet()

        return numberPatternList.withIndex().associate { it.value to it.index}
    }
}


