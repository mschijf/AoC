package adventofcode.year2020.december12

import adventofcode.PuzzleSolverAbstract
import kotlin.math.absoluteValue

fun main() {
    Day12(test=false).showResult()
}

class Day12(test: Boolean) : PuzzleSolverAbstract(test) {
    private val instructionList = inputLines.map { Action(it) }

    override fun resultPartOne(): String {
        val ferryPosition = FerryPosition()
        instructionList.forEach { ferryPosition.doAction(it) }

        return ferryPosition.manhattanDistance().toString()
    }

    override fun resultPartTwo(): String {
        val ferryPosition = FerryPositionPartTwo()
        instructionList.forEach { ferryPosition.doAction(it) }

        return ferryPosition.manhattanDistance().toString()
    }
}

class FerryPosition {
    private var eastPos = 0
    private var northPos = 0
    private var facing = Direction.EAST

    fun doAction(action: Action) {
        if (action.rotateRightDegrees != null) {
            facing = facing.rotateRight(action.rotateRightDegrees)
        } else if (action.moveUnits != null) {
            if (action.isForward) {
                moveUnits(facing, action.moveUnits)
            } else {
                moveUnits(action.moveDirection!!, action.moveUnits)
            }
        } else {
            throw java.lang.Exception ("Unexpected action")
        }
    }

    private fun moveUnits(moveDirection: Direction, moveUnits: Int) {
        when (moveDirection) {
            Direction.EAST -> eastPos += moveUnits
            Direction.WEST -> eastPos -= moveUnits
            Direction.NORTH -> northPos += moveUnits
            Direction.SOUTH -> northPos -= moveUnits
        }
    }

    fun manhattanDistance() = eastPos.absoluteValue + northPos.absoluteValue

}

class FerryPositionPartTwo {
    private var eastPosFerry = 0
    private var northPosFerry = 0

    private var eastPosWaypoint = 10
    private var northPoWaypoint = 1

    fun doAction(action: Action) {
        if (action.rotateRightDegrees != null) {
            rotateWayPoint(action.rotateRightDegrees)
        } else if (action.moveUnits != null) {
            if (action.isForward) {
                moveShip(action.moveUnits)
            } else {
                moveWayPoint(action.moveDirection!!, action.moveUnits)
            }
        } else {
            throw java.lang.Exception ("Unexpected action")
        }
    }

    private fun moveShip(moveFactor: Int) {
        eastPosFerry += eastPosWaypoint * moveFactor
        northPosFerry += northPoWaypoint * moveFactor
    }

    private fun moveWayPoint(moveDirection: Direction, moveUnits: Int) {
        when (moveDirection) {
            Direction.EAST -> eastPosWaypoint += moveUnits
            Direction.WEST -> eastPosWaypoint -= moveUnits
            Direction.NORTH -> northPoWaypoint += moveUnits
            Direction.SOUTH -> northPoWaypoint -= moveUnits
        }
    }

    private fun rotateWayPoint(degrees: Int) {
        val tmpEast = eastPosWaypoint
        val tmpNorth = northPoWaypoint
        when (degrees) {
            90 -> {eastPosWaypoint = tmpNorth; northPoWaypoint = -tmpEast }
            180 -> {eastPosWaypoint = -tmpEast; northPoWaypoint = -tmpNorth }
            270 -> {eastPosWaypoint = -tmpNorth; northPoWaypoint = tmpEast }
            else -> throw Exception("$degrees is unknown degrees")
        }
    }

    fun manhattanDistance() = eastPosFerry.absoluteValue + northPosFerry.absoluteValue

}


class Action(actionStr: String) {
    val rotateRightDegrees = if (actionStr[0] == 'R') actionStr.drop(1).toInt() else if (actionStr[0] == 'L') 360 - actionStr.drop(1).toInt() else null
    val isForward = (actionStr[0] == 'F')
    val moveDirection = when(actionStr[0]) {
        'E' -> Direction.EAST
        'W' -> Direction.WEST
        'S' -> Direction.SOUTH
        'N' -> Direction.NORTH
        else -> null
    }
    val moveUnits = if (actionStr[0] in "FEWSN") actionStr.drop(1).toInt() else null
}

enum class Direction {
    EAST {
        override fun rotateRight(degrees: Int): Direction {
            return when(degrees) {
                90 -> SOUTH
                180 -> WEST
                270 -> NORTH
                0, 360 -> EAST
                else -> throw Exception("unexpected degrees")
            }
        }
         },
    SOUTH {
        override fun rotateRight(degrees: Int): Direction {
            return when(degrees) {
                90 -> WEST
                180 -> NORTH
                270 -> EAST
                0, 360 -> SOUTH
                else -> throw Exception("unexpected degrees")
            }
        }
          },
    WEST {
        override fun rotateRight(degrees: Int): Direction {
            return when(degrees) {
                90 -> NORTH
                180 -> EAST
                270 -> SOUTH
                0, 360 -> WEST
                else -> throw Exception("unexpected degrees")
            }
        }
         },
    NORTH {
        override fun rotateRight(degrees: Int): Direction {
            return when(degrees) {
                90 -> EAST
                180 -> SOUTH
                270 -> WEST
                0, 360 -> NORTH
                else -> throw Exception("unexpected degrees")
            }
        }
    };

    abstract fun rotateRight(degrees: Int): Direction

}


