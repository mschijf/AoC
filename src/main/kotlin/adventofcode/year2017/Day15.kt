package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day15(test=false).showResult()
}

class Day15(test: Boolean) : PuzzleSolverAbstract(test) {

    private val startA = if (test) 65L else 722L
    private val startB = if (test) 8921L else 354L

    private val factorA = 16807L
    private val factorB = 48271L

    private val divider = 2147483647L

    private val lowerBitsMask = (1L shl 16) - 1


    override fun resultPartOne(): Any {
        return generator(startA, factorA)
            .zip(generator(startB, factorB))
            .take(40_000_000)
            .count{ (it.first and lowerBitsMask) == (it.second and lowerBitsMask) }
    }

    override fun resultPartTwo(): Any {
        return generator(startA, factorA).filter{number -> number % 4 == 0L}
            .zip(generator(startB, factorB).filter{number -> number % 8 == 0L})
            .take(5_000_000)
            .count{ (it.first and lowerBitsMask) == (it.second and lowerBitsMask) }
    }

    private fun generator(start: Long, factor: Long): Sequence<Long> =
        generateSequence((start*factor) % divider){ (it*factor) % divider}

}


