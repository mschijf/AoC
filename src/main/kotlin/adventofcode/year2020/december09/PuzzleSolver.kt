package adventofcode.year2020.december09

import adventofcode.PuzzleSolverAbstract
import java.lang.Exception

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val xmasData = inputLines.map {it.toLong()}
    private val preamble = if (test) 5 else 25

    override fun resultPartOne(): String {
        return findInvalidNumber().toString()
    }

    override fun resultPartTwo(): String {
        val requestedResult = findInvalidNumber()
        for (i in 0 until xmasData.size-1) {
            val range = sumOfRange(i, requestedResult)
            if (range.isNotEmpty()) {
                return (range.min() + range.max()).toString()
            }
        }
        return "Not Found"
    }

    private fun findInvalidNumber(): Long {
        xmasData.forEachIndexed { index, l ->
            if (index >= preamble) {
                if (l !in sumSet(index-preamble, index-1))
                    return l
            }
        }
        throw Exception("Invalid number not found")
    }

    private fun sumSet(fromIndex: Int, toIndex: Int): Set<Long> {
        val result = mutableSetOf<Long>()
        for (i in fromIndex until toIndex) {
            for (j in i+1 .. toIndex) {
                result.add(xmasData[i] + xmasData[j])
            }
        }
        return result
    }

    private fun sumOfRange(fromIndex: Int, requestedResult: Long): List<Long> {
        var sum = 0L
        var i = fromIndex
        while (i < xmasData.size && sum < requestedResult) {
            sum += xmasData[i++]
        }
        if (sum != requestedResult)
            return emptyList()
        return xmasData.subList(fromIndex, i)
    }

}


