package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point

fun main() {
    Day11(test=false).showResult()
}

class Day11(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    private val octopusGrid = inputLines.asGrid().mapValues { it.value.digitToInt() }

    override fun resultPartOne(): Any {
        val evolvingGrid = octopusGrid.toMutableMap()
        return (1..100).sumOf {
            evolvingGrid.doStep()
        }
    }

    override fun resultPartTwo(): Any {
        val evolvingGrid = octopusGrid.toMutableMap()
        var count = 0
        while (evolvingGrid.values.any { it != 0 }) {
            evolvingGrid.doStep()
            count++
        }
        return count
    }

    private fun MutableMap<Point, Int>.doStep(): Int {
        val alreadyFlashed = mutableSetOf<Point>()
        val working = this
        var increasers = this.keys.toList()
        while  (increasers.isNotEmpty()) {
            increasers.forEach { key -> working[key] = working[key]!! + 1 }
            val flashSet = working.keys.filter { working[it]!! > 9 && it !in alreadyFlashed }
            alreadyFlashed += flashSet
            increasers = flashSet.flatMap { it.allWindDirectionNeighbors() }.filter { it in working.keys }.toList()
        }
        alreadyFlashed.forEach { f -> working[f] = 0 }
        return alreadyFlashed.size
    }
}


