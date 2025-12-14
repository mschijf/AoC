package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day1516(test=false).showResult()
}

class Day1516(test: Boolean) : PuzzleSolverAbstract(test) {

    private val tickerTapeOutput = "" +
            "children: 3\n" +
            "cats: 7\n" +
            "samoyeds: 2\n" +
            "pomeranians: 3\n" +
            "akitas: 0\n" +
            "vizslas: 0\n" +
            "goldfish: 5\n" +
            "trees: 3\n" +
            "cars: 2\n" +
            "perfumes: 1"

    private val tickerTapeRequirements = tickerTapeOutput.split("\n").associate { it.substringBefore(":") to it.substringAfter(": ").toInt() }
    private val auntList = inputLines.map{ Aunt.of(it) }

    override fun resultPartOne(): Any {
        return auntList.filter{ it.match(tickerTapeRequirements) }
    }

    override fun resultPartTwo(): Any {
        return auntList.filter{ it.matchPart2(tickerTapeRequirements) }
    }
}


data class Aunt(val name: String, val compoundsMap: Map<String, Int>) {

    fun match(tickerTapeRequirements: Map<String, Int>)=
        compoundsMap.all { entry -> entry.value == tickerTapeRequirements[entry.key]!! }

    fun matchPart2(tickerTapeRequirements: Map<String, Int>) =
        compoundsMap.all { entry ->
            when (entry.key) {
                "cats", "trees" -> entry.value > tickerTapeRequirements[entry.key]!!
                "pomeranians", "goldfish" -> entry.value < tickerTapeRequirements[entry.key]!!
                else -> entry.value == tickerTapeRequirements[entry.key]!!
            }
    }


    companion object {
        fun of(rawInput: String) : Aunt {
            val name = rawInput.substringBefore(":")
            val compounds = rawInput.substringAfter(": ")
                .split(", ")
                .associate{ it.substringBefore(": ") to it.substringAfter(": ").toInt() }
            return Aunt(name, compounds)
        }
    }
}


