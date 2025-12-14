package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import tool.mylambdas.hasOnlyDigits
import java.lang.Exception

fun main() {
    Day1507(test = false).showResult()
}

class Day1507(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return Program(inputLines).valueOf("a")
    }

    override fun resultPartTwo(): Any {
        val overrideValueB = resultPartOne().toString()
        return Program(inputLines, "$overrideValueB -> b").valueOf("a")
    }
}

class Program(statementList: List<String>, override: String? = null) {
    private val statementMap =
        statementList.map { Statement.of(it) }.associateBy { it.output } +
                if (override != null) {
                    listOf(Statement.of(override)).associateBy { it.output }
                } else {
                    emptyMap()
                }

    private val register = mutableMapOf<String, UShort>()

    //
    // recursive function. parameter 'word' can be a registerId or a number
    // to find a value for given registerId, you have to execute the corresponding statement (can be found in the statement Map)
    // the value of the operands of the statement, can recursively be found by calling this valueOf function.
    // finally, store the result in the registerMap.
    //

    fun valueOf(word: String): UShort {
        if (word.hasOnlyDigits()) {
            return word.toUShort()
        }
        if (register.contains(word)) {
            return register[word]!!
        }

        val statement = statementMap[word]!!
        val result = when (statement.operator) {
            "SET" -> valueOf(statement.operand1)
            "NOT" -> valueOf(statement.operand1).inv()
            "OR" -> valueOf(statement.operand1) or valueOf(statement.operand2!!)
            "AND" -> valueOf(statement.operand1) and valueOf(statement.operand2!!)
            "LSHIFT" -> (valueOf(statement.operand1).toUInt() shl valueOf(statement.operand2!!).toInt()).toUShort()
            "RSHIFT" -> (valueOf(statement.operand1).toUInt() shr valueOf(statement.operand2!!).toInt()).toUShort()
            else -> throw Exception("Unknown statement: $statement")
        }
        register[statement.output] = result
        return result
    }
}

data class Statement(val operator: String, val operand1: String, val operand2: String?, val output: String) {
    companion object {
        fun of(raw: String): Statement {
            val wordList = raw.split("\\s".toRegex())
            val (operator, operand1, operand2) = when (wordList.size) {
                3 -> Triple("SET", wordList[0], null)
                4 -> Triple("NOT", wordList[1], null)
                else -> Triple(wordList[1], wordList[0], wordList[2])
            }
            return Statement(operator, operand1, operand2, wordList.last())
        }
    }

}