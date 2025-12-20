package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.splitByCondition

fun main() {
    Day1725(test=false).showResult()
}

class Day1725(test: Boolean) : PuzzleSolverAbstract(test) {

    private val stateList = inputLines
        .splitByCondition { it.isEmpty() }.drop(1).map{State.of(it)}
        .associateBy { it.stateName }
    private val startState = inputLines[0]
        .substringAfter("Begin in state ").substringBefore(".")
    private val executeSteps = inputLines[1]
        .substringAfter("Perform a diagnostic checksum after ").substringBefore(" steps.").toInt()


    override fun resultPartOne(): Any {
        val turingTape = mutableMapOf<Int, Int>()
        var pos = 0
        var currentState = startState
        repeat(executeSteps) {
            val action = stateList[currentState]!!.doStep(turingTape.getOrDefault(pos, 0))
            turingTape[pos] = action.writeValue
            pos += if (action.moveTo == MoveDirection.LEFT) -1 else 1
            currentState = action.nextStateName
        }
        return turingTape.values.sum()
    }
}

data class State(val stateName: String, val actionOn0: Action, val actionOn1: Action) {
    companion object {
        fun of (rawInput: List<String>): State {
            return State (
                stateName=rawInput[0].substringAfter("In state ").substringBefore(":"),
                actionOn0 = Action.of(rawInput.subList(2,5)),
                actionOn1 = Action.of(rawInput.subList(6,9))
            )
        }
    }

    fun doStep(forBitValue: Int) =
        if (forBitValue == 0) actionOn0 else actionOn1

}

data class Action(val writeValue: Int, val moveTo: MoveDirection, val nextStateName: String) {
    companion object {
        fun of (rawInput: List<String>): Action {
            return Action(
                writeValue=rawInput[0].substringAfter("Write the value ").substringBefore(".").toInt(),
                moveTo=MoveDirection.valueOf(rawInput[1].substringAfter("Move one slot to the ").substringBefore(".").uppercase()),
                nextStateName=rawInput[2].substringAfter("Continue with state ").substringBefore(".")
            )
        }
    }
}

enum class MoveDirection {
    LEFT, RIGHT
}