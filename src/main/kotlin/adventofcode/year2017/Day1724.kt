package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day1724(test = false).showResult()
}

class Day1724(test: Boolean) : PuzzleSolverAbstract(test) {
    private val componentSet =
        inputLines.mapIndexed { idx, line -> line.split("/").let { Component(idx, it[0].toInt(), it[1].toInt()) } }.toSet()

    override fun resultPartOne(): Any {
        return componentSet.solveStrongest(0)
    }

    override fun resultPartTwo(): Any {
        return componentSet.solveLongest(0).strength
    }

    private fun Set<Component>.solveStrongest(openConnection: Int): Int {
        return this.findCandidates(openConnection)
            .maxOfOrNull { candidate ->
                candidate.strength() + (this-candidate).solveStrongest(candidate.otherPort(openConnection))
            } ?: 0
    }

    private fun Set<Component>.solveLongest(openConnection: Int): Info {
        return this.findCandidates(openConnection)
            .maxOfOrNull { cand ->
                (this - cand).solveLongest(cand.otherPort(openConnection)).let { Info(it.length+1, it.strength+cand.strength()) }
            } ?: Info(0,0)
    }

    private fun Set<Component>.findCandidates(openConnection: Int): List<Component> {
        return this.filter{it.hasPortType(openConnection)}
    }
}

data class Component(private val id: Int, val port1: Int, val port2: Int) {

    fun hasPortType(aPort: Int) =
        (port1 == aPort || port2 == aPort)

    fun otherPort(aPort: Int) =
        when (aPort) {
            port1 -> port2
            port2 -> port1
            else -> throw Exception("Oh oh")
        }

    fun strength() = port1 + port2
}

data class Info(val length: Int, val strength: Int): Comparable<Info> {
    override fun compareTo(other: Info): Int {
        return when {
            (this == other) -> 0
            (this.length > other.length || this.length == other.length && this.strength > other.strength) -> 1
            else -> -1
        }
    }
}