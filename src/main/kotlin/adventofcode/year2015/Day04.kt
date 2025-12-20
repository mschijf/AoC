package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import tool.mylambdas.toMD5Hexadecimal

fun main() {
    Day04(test=false).showResult()
}

class Day04(test: Boolean) : PuzzleSolverAbstract(test) {

    private val secretKey = if (test) "abcdef" else "ckczppom"

    override fun resultPartOne(): Any {
        return generateSequence(1) {it + 1}.first{ "$secretKey$it".toMD5Hexadecimal().startsWith("00000") }
    }

    override fun resultPartTwo(): Any {
        return generateSequence(1) {it + 1}.first{ "$secretKey$it".toMD5Hexadecimal().startsWith("000000") }
    }
}


