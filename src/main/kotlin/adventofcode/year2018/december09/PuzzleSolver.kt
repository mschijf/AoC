package adventofcode.year2018.december09

import adventofcode.PuzzleSolverAbstract
import tool.collectionspecials.CircularLinkedList

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val numberOfPLayers = inputLines.first().split(" ").first().toInt()
    private val highestMarble = inputLines.first().split(" ").run{this[6]}.toLong()

    override fun resultPartOne(): Any {
        return solveForHighestMarble(highestMarble)
    }

    override fun resultPartTwo(): Any {
        return solveForHighestMarble(highestMarble*100)
    }

    private fun solveForHighestMarble(highestMarble: Long): Long {
        val playerScores = Array(numberOfPLayers){0L}
//        val circle = emptyCircularList<Long>()
        val circle = CircularLinkedList<Long>()
        circle.add(0)
        var currentNode = circle.firstIndex()

        var currentPlayer = 0
        for (i in 1..highestMarble) {
            if (i % 23 == 0L) {
                currentNode = currentNode.prev(6)
                val valueRemovedNode = circle[currentNode.prev()]
                circle.removeAt(currentNode.prev())
                playerScores[currentPlayer] += (i + valueRemovedNode)
            } else {
                circle.add(currentNode.next(2), i)
                currentNode = currentNode.next(2)
            }
            currentPlayer = (currentPlayer + 1) % numberOfPLayers
        }

        return playerScores.max()
    }
}


