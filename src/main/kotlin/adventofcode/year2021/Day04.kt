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
        return bingoNumbers.firstNotNullOf { bingoNumber ->
                bingoCards.firstOrNull { it.hasBingo(bingoNumber) }
            }
            .score()
    }

    override fun resultPartTwo(): Any {
        val nonBingoCards = bingoCards.toMutableList()
        bingoNumbers.forEach { bingoNumber ->
            val winningBingoCards = nonBingoCards.filter { it.hasBingo(bingoNumber) }
            if (winningBingoCards.isNotEmpty()) {
                if (nonBingoCards.size == 1)
                    return nonBingoCards.first().score()
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

    private val markedNumberSet = mutableSetOf<Int>()
    private var lastNumber = -1

    fun hasBingo(bingoNumber: Int): Boolean {
        lastNumber = bingoNumber
        markedNumberSet += bingoNumber
        val rowBingo = card.any{ row -> row.all { number -> number in markedNumberSet}}
        val colBingo = (0..card.size-1).any { col -> (0..card.size-1).all { row -> card[row][col] in markedNumberSet } }
        return rowBingo || colBingo
    }

    fun score() : Int {
        return card.flatMap{ it }.filterNot { number -> number in markedNumberSet}.sum() * lastNumber
    }
}