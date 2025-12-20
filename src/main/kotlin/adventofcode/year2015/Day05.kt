package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day05(test=false).showResult()
}

class Day05(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return inputLines.count { it.isNice() }
    }

    override fun resultPartTwo(): Any {
        return inputLines.count { it.isNicePartTwo() }
    }

    private fun String.isNice() =
        this.doesNotContainOneOf(listOf("ab", "cd", "pq", "xy")) &&
                this.atLeastThreeVowels() &&
                this.atLeastOneDoubleLetter()

    private fun String.atLeastThreeVowels() =
        this.count { ch -> ch in "aeiou" } >= 3

    private fun String.atLeastOneDoubleLetter() =
        (1..this.length-1).any { i -> this[i] == this[i-1] }

    private fun String.doesNotContainOneOf(forbidden: List<String>) =
        forbidden.none{this.contains(it)}

    private fun String.isNicePartTwo() =
        this.atLeastTwoDoubleLetter()  && this.atLeastOneRepeater()

    private fun String.atLeastTwoDoubleLetter() =
        (1..this.length-1).any { i -> this.substring(i+1).contains("${this[i-1]}${this[i]}")}

    private fun String.atLeastOneRepeater() =
        (2..this.length-1).any { i -> this[i] == this[i-2] }



}


