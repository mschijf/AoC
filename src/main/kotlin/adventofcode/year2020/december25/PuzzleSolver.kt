package adventofcode.year2020.december25

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val cardPublicKey = inputLines.first().toLong()
        val doorPublicKey = inputLines.last().toLong()

        val cardLoopSize = findLoopSize(cardPublicKey, 7L)
        val doorLoopSize = findLoopSize(doorPublicKey, 7L)

        val privateKey1 = findPrivateKey(cardPublicKey, doorLoopSize)
        val privateKey2 = findPrivateKey(doorPublicKey, cardLoopSize)

        return privateKey2.toString()
    }

    private fun findLoopSize(publicKey:Long, subjectNumber: Long): Int {
        var loopSize = 0
        var value = 1L
        while (value != publicKey) {
            value = (value * subjectNumber) % 20201227L
            loopSize++
        }
        return loopSize
    }

    private fun findPrivateKey(subjectNumber: Long, loopSize: Int): Long {
        var value = 1L
        repeat(loopSize) {
            value = (value * subjectNumber) % 20201227L
        }
        return value
    }

}


