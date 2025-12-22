package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.splitByCondition

fun main() {
    Day04(test=false).showResult()
}

class Day04(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true) {

    val bingoNumbers = inputLines.first().trim().split(",").map {it.toInt()}
    val bingoCards = inputLines.drop(2).splitByCondition { it.isEmpty() }.map { BingoCard.of(it) }

    override fun resultPartOne(): Any {
        val markedNumberSet = mutableSetOf<Int>()

        bingoNumbers.forEach { bingoNumber ->
            markedNumberSet += bingoNumber
            val winningBingoCard = bingoCards.firstOrNull { it.hasBingo(markedNumberSet) }
            if (winningBingoCard != null) {
                return winningBingoCard.score(bingoNumber, markedNumberSet)
            }
        }
        return "NOT FOUND"
    }

    override fun resultPartTwo(): Any {
        val markedNumberSet = mutableSetOf<Int>()
        val nonBingoCards = bingoCards.toMutableList()
        bingoNumbers.forEach { bingoNumber ->
            markedNumberSet += bingoNumber
            val winningBingoCards = nonBingoCards.filter { it.hasBingo(markedNumberSet) }
            if (winningBingoCards.isNotEmpty()) {
                if (nonBingoCards.size == 1)
                    return nonBingoCards.first().score(bingoNumber, markedNumberSet)
                nonBingoCards.removeAll(winningBingoCards)
            }
        }
        return "NOT FOUND"
    }
}

class BingoCard(val card: List<List<Int>>) {
    companion object {
        fun of(raw: List<String>) : BingoCard {
            return BingoCard(
                raw.map{ line -> line.trim().split("\\s+".toRegex()).map { it.toInt()}}
            )
        }
    }

    fun hasBingo(markedNumberSet: Set<Int>): Boolean {
        val rowBingo = card.any{ row -> row.all { number -> number in markedNumberSet}}
        val colBingo = (0..card.size-1).any { col -> (0..card.size-1).all { row -> card[row][col] in markedNumberSet } }
        return rowBingo || colBingo
    }

    fun score(lastNumber: Int, markedNumberSet: Set<Int>) : Int {
        return card.flatMap{ it }.filterNot { number -> number in markedNumberSet}.sum() * lastNumber
    }
}