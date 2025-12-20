package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Direction
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day1722(test=false).showResult()
}

class Day1722(test: Boolean) : PuzzleSolverAbstract(test) {

    private val gridMap = mutableMapOf<Point, InfectionType>()

    private var currentPos = pos(inputLines.size/2, inputLines.first().length/2)
    private var currentDir = Direction.UP

    private fun initialState() {
        gridMap.clear()
        gridMap += inputLines
            .flatMapIndexed{y, row -> row.mapIndexed { x, cell -> if (cell == '#') pos(x,y) else null }}
            .filterNotNull()
            .associateWith { InfectionType.INFECTED  }

        currentPos = pos(inputLines.size/2, inputLines.first().length/2)
        currentDir = Direction.UP
    }

    override fun resultPartOne(): Any {
        initialState()
        return (1..10_000).sumOf { burstPartOne() }
    }

    override fun resultPartTwo(): Any {
        initialState()
        return (1..10_000_000).sumOf { burstPartTwo() }
    }

    private fun burstPartOne(): Int {
        var infectionsCaused = 0
        when (gridMap.getOrDefault(currentPos, InfectionType.CLEAN)) {
            InfectionType.INFECTED -> {
                currentDir = currentDir.rotateRight()
                gridMap[currentPos] = InfectionType.CLEAN
            }
            else -> {
                currentDir = currentDir.rotateLeft()
                gridMap[currentPos] = InfectionType.INFECTED
                infectionsCaused = 1
            }
        }
        currentPos = currentPos.moveOneStep(currentDir)
        return infectionsCaused
    }

    private fun burstPartTwo(): Int {
        var infectionsCaused = 0
        when (gridMap.getOrDefault(currentPos, InfectionType.CLEAN)) {
            InfectionType.WEAKENED -> {
                currentDir = currentDir
                gridMap[currentPos] = InfectionType.INFECTED
                infectionsCaused = 1
            }
            InfectionType.INFECTED -> {
                currentDir = currentDir.rotateRight()
                gridMap[currentPos] = InfectionType.FLAGGED
            }
            InfectionType.FLAGGED -> {
                currentDir = currentDir.opposite()
                gridMap[currentPos] = InfectionType.CLEAN
            }
            else -> {
                currentDir = currentDir.rotateLeft()
                gridMap[currentPos] = InfectionType.WEAKENED
            }
        }
        currentPos = currentPos.moveOneStep(currentDir)
        return infectionsCaused
    }
}

enum class InfectionType {
    CLEAN, WEAKENED, INFECTED, FLAGGED
}
