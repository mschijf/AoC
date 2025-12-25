package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import tool.primarytype.isOpeningBracket
import tool.primarytype.matchingBracket

fun main() {
    Day10(test=false).showResult()
}

class Day10(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    override fun resultPartOne(): Any {
        return inputLines.sumOf { it.getCorruptedCharOrNull()?.errorValue()?:0 }
    }

    override fun resultPartTwo(): Any {
        val resultList = inputLines
            .map { line ->
                line
                    .getMissingSequence()
                    .fold(0L) { acc, ch -> acc * 5L + ch.missingValue()} }
            .filter { it > 0L }
            .sorted()
        return resultList[resultList.size / 2 ]
    }

    private fun String.getCorruptedCharOrNull(): Char? {
        val stack = ArrayDeque<Char>()
        this.forEach { ch ->
            if (ch.isOpeningBracket()) {
                stack.add(ch.matchingBracket())
            } else {
                val expected = stack.removeLast()
                if (expected != ch)
                    return ch
            }
        }
        return null
    }

    private fun String.getMissingSequence(): List<Char> {
        val stack = ArrayDeque<Char>()
        this.forEach { ch ->
            if (ch.isOpeningBracket()) {
                stack.add(ch.matchingBracket())
            } else {
                val expected = stack.removeLast()
                if (expected != ch)
                    return emptyList()
            }
        }
        return stack.reversed()
    }

    private fun Char.errorValue() : Int =
        when(this) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> throw Exception("Impossible")
        }

    private fun Char.missingValue() : Long =
        when(this) {
            ')' -> 1
            ']' -> 2
            '}' -> 3
            '>' -> 4
            else -> throw Exception("Impossible")
        }
}


