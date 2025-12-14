package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day1517(test=false).showResult()
}

class Day1517(test: Boolean) : PuzzleSolverAbstract(test) {

    private val containerList = inputLines.map{it.toInt()}.sorted()
    private val litres = if (test) 25 else 150

    override fun resultPartOne(): Any {
        return getAllContainerConfigs(litres, containerList).size
    }

    override fun resultPartTwo(): Any {
        return getAllContainerConfigs(litres, containerList)
            .map{it.size}
            .groupingBy { it }
            .eachCount()
            .minBy { it.key }
            .value
    }


    private fun getAllContainerConfigs(toFill: Int, containerList: List<Int>, containerConfig: List<Int> = emptyList()): List<List<Int>> {
        return if (toFill == 0) {
            listOf(containerConfig)
        } else if (toFill < 0) {
            emptyList()
        } else {
            containerList.foldIndexed(emptyList()) { index, acc, container ->
                acc + getAllContainerConfigs(
                    toFill = toFill - container,
                    containerList = containerList.drop(index + 1),
                    containerConfig = containerConfig + container)
            }
        }
    }
}


