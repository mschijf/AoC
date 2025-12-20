package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day1712(test=true).showResult()
}

class Day1712(test: Boolean) : PuzzleSolverAbstract(test) {

    private val pipes = inputLines.associate { it.toPair() }
    private val pipeGroups = pipes.connectPipeGroups()

    override fun resultPartOne(): Any {
        return pipeGroups.first{0 in it}.size
    }

    override fun resultPartTwo(): Any {
        return pipeGroups.size
    }

    private fun Map<Int, Set<Int>>.connectPipeGroups(): List<Set<Int>> {
        var result = listOf(emptySet<Int>())
        this.map{it.value + it.key}.forEach { pipesConnected ->
            result = result
                .partition { it.intersect(pipesConnected).isNotEmpty() }
                .let{listOf(it.first.flatten().toSet()+pipesConnected) + it.second}
        }
        return result.filterNot { it.isEmpty() }
    }

    private fun String.toPair(): Pair<Int, Set<Int>> =
        this
            .split(" <-> ")
            .let{ Pair(it[0].toInt(), it[1].split(", ").map{ nr -> nr.toInt()}.toSet()) }
}


