package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day1707(test=false).showResult()
}

class Day1707(test: Boolean) : PuzzleSolverAbstract(test) {

    private val programList = createProgramMap().values

    private fun createProgramMap(): Map<String, ProgramDay07> {
        val programMap = inputLines
            .map{ProgramDay07(name = it.substringBefore(" ("), weight = it.substringAfter("(").substringBefore(")").toInt() )}
            .associateBy { it.name }
        inputLines
            .filter { it.contains("->") }
            .map{Pair(it.substringBefore(" ("), it.substringAfter("-> ").split(", "))}
            .map{Pair(programMap[it.first]!!, it.second.map { childName -> programMap[childName]!! })}
            .forEach {
                it.first.addChildren(it.second)
            }
        return programMap
    }

    override fun resultPartOne(): Any {
        return programList.findRoot().name
    }

    override fun resultPartTwo(): Any {
        return programList.findRoot().findOutProgramOfBalanceOrNull()?.run{"${first.name} --> $second"}?:"Not found"
    }

    private fun Collection<ProgramDay07>.findRoot() = this.first { !it.hasParent() }
}


class ProgramDay07(
    val name: String,
    val weight: Int) {

    private var parent: ProgramDay07? = null
    private val children = mutableListOf<ProgramDay07>()

    fun findOutProgramOfBalanceOrNull(): Pair<ProgramDay07, Int>? {
        if (isInBalance())
            return null
        val firstChild = children.first()
        val otherWeightChildren = children.filterNot { it.totalWeight() == firstChild.totalWeight() }
        val needToChange = if (otherWeightChildren.size == 1) otherWeightChildren.first() else children.first()

        val fix = needToChange.findOutProgramOfBalanceOrNull()
        return if (fix != null) {
            fix
        } else {
            val newWeight = needToChange.weight - (needToChange.totalWeight() - children.first { it != needToChange }.totalWeight())
            Pair(needToChange, newWeight)
        }
    }

    private fun isInBalance(): Boolean {
        return if (children.isEmpty()) {
            true
        } else {
            val aWeight = children.first().totalWeight()
            children.all { it.totalWeight() == aWeight }
        }
    }

    private var totalWeight: Int? = null
    private fun totalWeight(): Int {
        if (totalWeight == null)
            totalWeight = weight + if (children.isEmpty()) 0 else children.sumOf { it.totalWeight() }
        return totalWeight!!
    }

    fun hasParent() = parent != null
    fun addChildren(programList: List<ProgramDay07>) {
        children.addAll(programList)
        children.forEach { it.parent = this }
    }

    override fun toString(): String {
        return "$name ($weight)"
    }
}


