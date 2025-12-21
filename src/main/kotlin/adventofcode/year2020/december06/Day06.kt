package adventofcode.year2020.december06

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.splitByCondition

fun main() {
    Day06(test=false).showResult()
}

class Day06(test: Boolean) : PuzzleSolverAbstract(test) {
    private val groupList = inputLines.splitByCondition { it.isEmpty() }.map { Group(it)}

    override fun resultPartOne(): String {
        return groupList.sumOf { it.distinctAnswers().count() }.toString()
    }

    override fun resultPartTwo(): String {
        return groupList.sumOf { it.sameAnswers().count() }.toString()
    }

}


class Group(
    private val answerList: List<String>) {

    fun distinctAnswers() = answerList.map {it.toSet()}.flatten().toSet()
    fun sameAnswers() = answerList.map {it.toSet()}.reduce {acc, next -> acc.intersect(next) }
}
