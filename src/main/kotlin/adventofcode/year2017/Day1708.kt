package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import kotlin.math.max

fun main() {
    Day1708(test=false).showResult()
}

class Day1708(test: Boolean) : PuzzleSolverAbstract(test) {

    val variableMap = mutableMapOf<String,Int>()
    val instructionList = inputLines.map{line -> parse(line)}

    override fun resultPartOne(): Any {
        instructionList.forEach {
            it.execute()
        }
        return variableMap.values.max()
    }

    override fun resultPartTwo(): Any {
        //reset variables
        variableMap.keys.forEach { variableMap[it] = 0 }

        var overallMax = variableMap.values.max()
        instructionList.forEach {
            it.execute()
            overallMax = max(overallMax, variableMap.values.max())
        }
        return overallMax
    }

    inner class Instruction(private val variable: String, private val operator: String, private val amount: Int, val condition: Condition) {
        fun execute() {
            if (condition.isTrue()) {
                when (operator) {
                    "inc" -> variableMap[variable] = variableMap.getOrDefault(variable, 0) + amount
                    "dec" -> variableMap[variable] = variableMap.getOrDefault(variable, 0) - amount
                    else -> throw Exception("unexpected operator")
                }
            }
        }
    }

    private fun parse(rawInput: String): Instruction {
        // rawInput:  b   inc   5    if    a    >    1
        //            ^    ^    ^    ^     ^    ^    ^
        //groups:     0    1    2    3     4    5    6
        //
        val groups = rawInput.split("\\s+".toRegex()).toList()
        return Instruction(groups[0], groups[1], groups[2].toInt(), Condition(groups[4], groups[5], groups[6].toInt()))
    }

    inner class Condition(private val variable: String, private val comparator: String, private val comparedWith: Int) {
        fun isTrue() : Boolean {
            val value = variableMap.getOrDefault(variable, 0)
                return when (comparator) {
                    "==" -> value == comparedWith
                    "!=" -> value != comparedWith
                    ">=" -> value >= comparedWith
                    "<=" -> value <= comparedWith
                    "<" -> value < comparedWith
                    ">" -> value > comparedWith
                    else -> throw Exception("Unexpected comparator: $comparator")
                }
        }

    }

}




