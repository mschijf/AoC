package adventofcode.year2020.december08

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val program = Program(inputLines)

    override fun resultPartOne(): String {
        program.execute()
        return program.accumulator.toString()
    }

    override fun resultPartTwo(): String {
        program.actionList.forEachIndexed { index, action ->
            if (action.operation != "acc") {
//                println(index)
                val hasEnded = program.executeWithChange(index)
                if (hasEnded)
                    return program.accumulator.toString()
            }
        }

        return "Program did not end"
    }
}


class Program(inputLines: List<String>) {
    var accumulator = 0L
        private set
    val actionList = inputLines.map { Action(it) }

    fun execute(): Boolean {
        accumulator = 0L
        val actionNumberSet = mutableSetOf<Int>()
        var actionIndex = 0
        while (actionIndex !in actionNumberSet && actionIndex < actionList.size) {
            actionNumberSet += actionIndex
            when (actionList[actionIndex].operation) {
                "acc" -> accumulator += actionList[actionIndex++].value
                "jmp" -> actionIndex += actionList[actionIndex].value
                "nop" -> actionIndex++
                else -> throw Exception ("unexpected operation")
            }
        }
        return actionIndex >= actionList.size
    }

    fun executeWithChange(changeOperationIndex: Int): Boolean {
        accumulator = 0L
        val actionNumberSet = mutableSetOf<Int>()
        var actionIndex = 0
        while (actionIndex !in actionNumberSet && actionIndex < actionList.size) {
            actionNumberSet += actionIndex
            when (actionList[actionIndex].operation) {
                "acc" -> accumulator += actionList[actionIndex++].value
                "jmp" ->
                    if (actionIndex != changeOperationIndex) {
                        actionIndex += actionList[actionIndex].value
                    } else {
                        // do nop
                        actionIndex++
                    }
                "nop" ->
                    if (actionIndex != changeOperationIndex) {
                        actionIndex++
                    } else {
                        // do jmp
                        actionIndex += actionList[actionIndex].value
                    }
                else -> throw Exception ("unexpected operation")
            }
        }
        return actionIndex >= actionList.size
    }

}

class Action(inputLine: String) {
    val operation = inputLine.substring(0, 3)
    val value = (if (inputLine[4] == '-') -1 else 1) * inputLine.substring(5).trim().toInt()
}


