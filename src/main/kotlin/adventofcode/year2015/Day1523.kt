package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import java.lang.Exception

fun main() {
    Day1523(test=false).showResult()
}

class Day1523(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val program = ProgramDay23(programLines = inputLines, registers = mutableMapOf('a' to 0, 'b' to 0))
        program.run()
        return program.getRegisterValues()
    }

    override fun resultPartTwo(): Any {
        val program = ProgramDay23(programLines = inputLines, registers = mutableMapOf('a' to 1, 'b' to 0))
        program.run()
        return program.getRegisterValues()
    }
}

class ProgramDay23(
    val programLines: List<String>,
    private val registers: MutableMap<Char, Long> = mutableMapOf('a' to 0, 'b' to 0)) {

    fun run(): Boolean {
        var ip = 0
        while (ip in programLines.indices) {
            val statement = programLines[ip]
            when (statement.operator()) {
                "hlf" -> {
                    registers[statement.operand1().asName()] = statement.operand1().asValue() / 2
                    ip++
                }

                "tpl" -> {
                    registers[statement.operand1().asName()] = statement.operand1().asValue() * 3
                    ip++
                }

                "inc" -> {
                    registers[statement.operand1().asName()] = statement.operand1().asValue() + 1
                    ip++
                }


                "jmp" -> {
                    ip += statement.operand1().asValue().toInt()
                }
                "jie" -> {
                    ip += if (statement.operand1().asValue() % 2 == 0L) statement.operand2().asValue().toInt() else 1
                }
                "jio" -> {
                    ip += if (statement.operand1().asValue() == 1L) statement.operand2().asValue().toInt() else 1
                }

                else -> throw Exception("Unknown operator")
            }
        }
        return false
    }

    private fun String.operator() =
        this.substringBefore(" ")

    private fun String.operand1() =
        this.substringAfter(" ").substringBefore(", ")

    private fun String.operand2() =
        this.substringAfterLast(", ")

    private fun String.asValue() =
        if (this.first() in registers.keys) registers[this.first()]!! else this.toLong()

    private fun String.asName() =
        if (this.first() in registers.keys) this.first() else throw Exception("Unknown register name")

    fun getRegisterValues() =
        registers.toMap()

}
