package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day14(test = false).showResult()
}

class Day14(test: Boolean) : PuzzleSolverAbstract(test, puzzleName = "TBD", hasInputFile = true) {
    private val polymerTemplate = inputLines.first()
    private val insertionRuleList =
        inputLines.drop(2)
            .associate { Pair(it.substringBefore(" -> "), it.substringAfter(" -> ")) }

    private val patternCount = polymerTemplate.toPatternCount()

    override fun resultPartOne(): Any {
        return solve(10)
    }

    override fun resultPartTwo(): Any {
        return solve(40)
    }

    private fun solve(nSteps: Int): Long {
        val result = (1..nSteps)
            .fold(patternCount) { acc, _ -> acc.processStep() }
            .charFrequency()
        return result.maxBy { it.value }.value - result.minBy { it.value }.value
    }

    private fun String.toPatternCount(): Map<String, Long> {
        val result = mutableMapOf<String, Long>()
        this.windowed(2).forEach { pattern ->
            result[pattern] = result.getOrDefault(pattern, 0) + 1
        }
        return result
    }

    private fun Map<String, Long>.processStep(): Map<String, Long> {
        val result = mutableMapOf<String, Long>()
        this.forEach { (pattern, count) ->
            val insertionChar = insertionRuleList[pattern]!!
            val key1 = pattern.first() + insertionChar
            result[key1] = result.getOrDefault(key1, 0) + count
            val key2 = insertionChar + pattern.last()
            result[key2] = result.getOrDefault(key2, 0) + count
        }
        return result
    }

    private fun Map<String, Long>.charFrequency(): Map<Char, Long> {
        val result = mutableMapOf<Char, Long>()
        this.keys.forEach { pattern ->
            result[pattern.first()] = result.getOrDefault(pattern.first(), 0) + this[pattern]!!
        }
        result[polymerTemplate.last()] = result.getOrDefault(polymerTemplate.last(), 0) + 1
        return result
    }
}


