package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day1510(test=true).showResult()
}

class Day1510(test: Boolean) : PuzzleSolverAbstract(test) {

    private val puzzleInput = "1113122113"

    override fun resultPartOne(): Any {
        return (1..40).fold(puzzleInput){ acc, _ -> acc.lookAndSay()}.length
    }

    override fun resultPartTwo(): Any {
        return (1..50).fold(puzzleInput){ acc, _ -> acc.lookAndSay()}.length
    }


    private fun String.lookAndSay() : String {
        val result = StringBuilder()
        var prev = this[0]
        var count = 0
        for (i in this.indices) {
            if (this[i] == prev) {
                count++
            } else {
                result.append(count).append(prev)
                count = 1
                prev = this[i]
            }
        }
        result.append(count).append(prev)
        return result.toString()
    }
}



