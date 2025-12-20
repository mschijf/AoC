package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.hexagon.Hexagon
import tool.coordinate.hexagon.HexagonDirection

fun main() {
    Day1711(test=false).showResult()
}

class Day1711(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return inputLines.first()
            .split(",")
            .fold(Hexagon.origin){ acc, s -> acc.moveOneStep(HexagonDirection.of(s))}
            .distanceTo(Hexagon.origin)
    }

    override fun resultPartTwo(): Any {
        return inputLines.first()
            .split(",")
            .scan(Hexagon.origin) {acc, s -> acc.moveOneStep(HexagonDirection.of(s))}
            .map{Hexagon.origin.distanceTo(it)}
            .max()
    }
}


