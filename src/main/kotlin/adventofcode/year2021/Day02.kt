package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Direction
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day02(test=false).showResult()
}

class Day02(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    override fun resultPartOne(): Any {
        val finalPos = inputLines
            .fold (SubmarinePosition(0,0)){ pos, action,  -> pos.followAction(action) }
        return finalPos.horizontal * finalPos.depth
    }

    override fun resultPartTwo(): Any {
        val finalPos = inputLines
            .fold (SubmarinePosition(0,0)){ pos, action,  -> pos.followActionPart2(action) }
        return finalPos.horizontal * finalPos.depth
    }
}

data class SubmarinePosition (val horizontal: Long, val depth: Long, val aim: Long = 0) {
    fun followAction(actionInput: String) : SubmarinePosition {
        val (action, steps) = actionInput.split(" ")
        return when (action) {
            "forward" -> SubmarinePosition(horizontal + steps.toLong(), depth, aim)
            "down" -> SubmarinePosition(horizontal, depth + steps.toLong(), aim)
            "up" -> SubmarinePosition(horizontal, depth - steps.toLong(), aim)
            else -> throw Exception("")
        }
    }

    fun followActionPart2(actionInput: String) : SubmarinePosition {
        val (action, steps) = actionInput.split(" ")
        return when (action) {
            "forward" -> SubmarinePosition(horizontal + steps.toLong(), depth + steps.toLong() * aim, aim)
            "down" -> SubmarinePosition(horizontal, depth, aim + steps.toLong())
            "up" -> SubmarinePosition(horizontal, depth, aim - steps.toLong())
            else -> throw Exception("")
        }
    }

}


