package adventofcode.year2019

import adventofcode.PuzzleSolverAbstract
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day17ToddGinsberg(test=false).showResult()
}

class Day17ToddGinsberg(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val scaffold = mapScaffold()
        scaffold.print()
        return scaffold
            .filter { it.value == '#' }
            .keys
            .filter { it.neighbors().all { neighbor -> scaffold[neighbor] == '#' } }
            .map { it.x * it.y }
            .sum()
            .toString()
    }

    private suspend fun takePicture(computer: IntCodeProgramCR): Map<Point, Char> =
        computer.output.consumeAsFlow()
            .map { it.toInt().toChar() }
            .toList()
            .joinToString("")
            .lines()
            .mapIndexed { y, row ->
                row.mapIndexed { x, c -> pos(x, y) to c }
            }
            .flatten()
            .toMap()

    private fun mapScaffold(): Map<Point, Char> = runBlocking {
        val computer = IntCodeProgramCR(inputLines().first().split(",").map { it.toLong() })
        launch {
            computer.runProgram()
        }
        takePicture(computer)
    }

    private fun <T> Map<Point, T>.print() {
        val maxX = this.keys.maxBy { it.x }.x
        val maxY = this.keys.maxBy { it.y }.y

        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                print(this.getOrDefault(pos(x, y), ' '))
            }
            println()
        }
    }
}


