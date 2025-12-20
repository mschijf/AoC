package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day06(test=false).showResult()
}

class Day06(test: Boolean) : PuzzleSolverAbstract(test) {

    private val origin = inputLines.first().split("\\s".toRegex()).map{it.trim().toInt()}

    override fun resultPartOne(): Any {
        var x = origin
        val alreadyGenerated = mutableSetOf<List<Int>>()
        while (x !in alreadyGenerated) {
            alreadyGenerated += x
            x = x.redistribitionCycle()
        }
        return alreadyGenerated.size
    }

    override fun resultPartTwo(): Any {
        var x = origin
        val alreadyGenerated = mutableListOf<List<Int>>()
        while (x !in alreadyGenerated) {
            alreadyGenerated += x
            x = x.redistribitionCycle()
        }
        return alreadyGenerated.size - alreadyGenerated.indexOfFirst { it == x }
    }

    private fun List<Int>.redistribitionCycle(): List<Int> {
        val (indexOfMax, max) = this.withIndex().maxBy { it.value }

        val new = this.toMutableList()
        new[indexOfMax] = 0
        for (i in new.indices) new[i] += max/new.size
        var i = (indexOfMax+1) % new.size
        repeat(max % new.size) {
            new[i] ++
            i = (i + 1) % new.size
        }

        return new
    }
}


