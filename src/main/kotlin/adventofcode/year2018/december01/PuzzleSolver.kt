package adventofcode.year2018.december01

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return inputLines.map{it.toInt()}.sum()
    }

    override fun resultPartTwo(): Any {
        val frequencySet = mutableSetOf<Int>()
        val frequenceChangeList = inputLines.map{it.toInt()}
        var sum = 0
        var index = 0
        while (sum !in frequencySet) {
            frequencySet += sum
            sum += frequenceChangeList[index % frequenceChangeList.size]
            index++
        }
        return sum
    }
}


