package adventofcode.year2018.december16

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.splitByCondition

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val sampleList = inputLines
            .splitByCondition { it.isEmpty() }.filter { it.isNotEmpty() && it[0].startsWith("Before") }
            .map{Sample(it)}

        return sampleList.map{it.getPossibleInstructionList()}.count{it.size >= 3}
    }

    override fun resultPartTwo(): Any {
        val sampleList = inputLines
            .splitByCondition { it.isEmpty() }.filter { it.isNotEmpty() && it[0].startsWith("Before") }
            .map{Sample(it)}

        val instructions = inputLines
            .splitByCondition { it.isEmpty() }.filter { it.isNotEmpty() && !it[0].startsWith("Before") }

        val intToCodeMap = sampleList.determineIntToCode()

        val device = RegisterDevice(mutableListOf(0,0,0,0), intToCodeMap)
        instructions.flatten().forEach {instruction ->
            val params = instruction.split(" ").map{it.toInt()}
            device.instructionByOpcode(params[0], params[1], params[2], params[3])
        }
        return "$device ==> answer is first number (register 0)"
    }

    private fun List<Sample>.determineIntToCode(): Map<Int, OpCode> {
        val result = this.groupBy { it.opCodeNumber() }.map { it.key to it.value.resolve() }.toMap().toMutableMap()
//        println(result.values.map { it.size })
        while (result.values.count{it.size > 1} > 0) {
            val oneOption = result.values.filter { it.size == 1 }.flatten().toSet()
            result.forEach {
                if (it.value.size > 1) {
                    result[it.key] = it.value - oneOption
                }
            }
//            println(result.values.map { it.size })
        }
        return result.map { it.key to it.value.first() }.toMap()
    }

    private fun List<Sample>.resolve(): Set<OpCode> {
        var resultSet = OpCode.values().toSet()
        this.forEach {sample ->
            resultSet = resultSet.intersect(sample.getPossibleInstructionList().toSet())
        }
        return resultSet
    }

}

class Sample(inputLines: List<String>) {
    private val before = inputLines[0].substringAfter("Before: ").trim()
    private val instruction = inputLines[1].split(" ").map {it.toInt()}
    private val after = inputLines[2].substringAfter("After: ").trim()

    fun opCodeNumber() = instruction[0]

    fun getPossibleInstructionList(): List<OpCode> {
        val result = mutableListOf<OpCode>()
        OpCode.values().forEach { opcodeName ->
            val reg = RegisterDevice.from(before)
            reg.instructionByName(opcodeName, instruction[1], instruction[2], instruction[3])
            if (reg.toString() == after)
                result.add(opcodeName)
        }
        return result
    }
}

