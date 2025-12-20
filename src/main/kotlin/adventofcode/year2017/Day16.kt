package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import java.lang.Exception

fun main() {
    Day16(test=false).showResult()
}

class Day16(test: Boolean) : PuzzleSolverAbstract(test) {

    private val danceSequence = inputLines.first().split(",")
    private val programCount = if (test) 5 else 16

    private val start = List(programCount){ 'a' + it }

    override fun resultPartOne(): Any {
        var progamSequence = start
        danceSequence.forEach { danceString -> progamSequence = progamSequence.dance(danceString) }
        return progamSequence.joinToString("")
    }

    /**
     * 1. loop until repetition
     * 2. calculate cyclength of the repetition and how many cycles you can skip
     * 3. determine te new programSequence
     */
    override fun resultPartTwo(): Any {
        var count = 0
        val cache = mutableListOf<List<Char>>()
        var progamSequence = start
        while (progamSequence !in cache) {
            cache.add(progamSequence)
            danceSequence.forEach { danceString -> progamSequence = progamSequence.dance(danceString) }
            count++
        }

        val indexOfFirstOccurence = cache.indexOf(progamSequence)
        val indexOfFirstRepetition = count
        val cycleLength = indexOfFirstRepetition - indexOfFirstOccurence

        progamSequence = start
        repeat(indexOfFirstOccurence) {
            danceSequence.forEach { danceString -> progamSequence = progamSequence.dance(danceString) }
        }
        repeat((1_000_000_000 - indexOfFirstOccurence) % cycleLength ) {
            danceSequence.forEach { danceString -> progamSequence = progamSequence.dance(danceString) }
        }
        return progamSequence.joinToString("")
    }

    private fun List<Char>.dance(danceType: String): List<Char> {
        return when(danceType.first()) {
            's' -> this.spin(danceType.drop(1).toInt())
            'x' -> this.exchange(danceType.drop(1).substringBefore("/").toInt(), danceType.substringAfter("/").toInt())
            'p' -> this.partner(danceType[1], danceType[3])
            else -> throw Exception("unknown dance")
        }
    }

    private fun List<Char>.spin(number: Int) =
        this.takeLast(number) + this.take(this.size - number)

    private fun List<Char>.exchange(pos1: Int, pos2: Int): List<Char> {
        val result = this.toMutableList()
        val tmp = result[pos1]
        result[pos1] = result[pos2]
        result[pos2] = tmp
        return result
    }

    private fun List<Char>.partner(program1: Char, program2: Char) =
        this.exchange( this.indexOf(program1), this.indexOf(program2) )

}



