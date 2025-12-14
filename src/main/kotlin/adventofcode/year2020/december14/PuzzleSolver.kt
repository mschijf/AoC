package adventofcode.year2020.december14

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val program = Program(inputLines)
        program.executeStatements()
        return program.memSum().toString()
    }

    override fun resultPartTwo(): String {
        val program = ProgramPart2(inputLines)
        program.executeStatements()
        return program.memSum().toString()
    }
}

class Program(inputLines: List<String>) {
    private val statementList = inputLines.map{ if (it.startsWith("mask")) MaskStatement(it) else MemStatement(it) }
    private var maskSet = 0UL
    private var maskClear = 0UL
    private val mem = mutableMapOf<Int, ULong>()

    fun executeStatements() {
        statementList.forEach { it.execute() }
    }

    fun memSum(): ULong {
        return mem.values.sum()
    }


    abstract inner class Statement {
        abstract fun execute()
    }

    inner class MaskStatement(inputLine: String): Statement() {
        private val newMaskSet = inputLine.substringAfter("mask = ").replace('X', '0').toULong(2)
        private val newMaskClear = 0UL.inv() and inputLine.substringAfter("mask = ").replace('X', '1').toULong(2)

        override fun execute() {
            maskSet = newMaskSet
            maskClear = newMaskClear
        }
    }

    inner class MemStatement(inputLine: String): Statement() {
        private val index = inputLine.substringAfter("mem[").substringBefore("]").toInt()
        private val newValue =  inputLine.substringAfter("] = ").toULong()

        override fun execute() {
            mem[index] = (newValue or maskSet) and maskClear
        }
    }

}

class ProgramPart2(inputLines: List<String>) {
    private val statementList = inputLines.map{ if (it.startsWith("mask")) MaskStatement(it) else MemStatement(it) }
    private var mask = ""
    private val mem = mutableMapOf<ULong, ULong>()

    fun executeStatements() {
        statementList.forEach { it.execute() }
    }

    fun memSum(): ULong {
        return mem.values.sum()
    }


    abstract inner class Statement {
        abstract fun execute()
    }

    inner class MaskStatement(inputLine: String): Statement() {
        private val newMask = inputLine.substringAfter("mask = ")

        override fun execute() {
            mask = newMask
        }
    }

    inner class MemStatement(inputLine: String): Statement() {
        private val index = inputLine.substringAfter("mem[").substringBefore("]").toULong()
        private val newValue =  inputLine.substringAfter("] = ").toULong()

        override fun execute() {
            val mergedIndex = mask.mapIndexed {i, ch -> if ( (ch == '0') && (1UL shl (mask.length-1-i) and index) > 0UL) '1' else ch }.joinToString("")
            val maskedIndexList = createMasks(mergedIndex)

            maskedIndexList.forEach { newIndex ->
                mem[newIndex] = newValue
            }
        }

        private fun createMasks(maskString: String): List<ULong> {
            if (!maskString.contains('X')) {
                return listOf(maskString.toULong(2))
            } else {
                return createMasks(maskString.replaceFirst('X', '1')) +
                        createMasks(maskString.replaceFirst('X', '0'))
            }
        }

    }

}



