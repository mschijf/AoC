package adventofcode.year2020.december22

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.splitByCondition

fun main() {
    Day22(test=false).showResult()
}

class Day22(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val playerList = inputLines.splitByCondition { it.isEmpty() }.map { Player(it) }

        val winner = playGame(playerList)

        return playerList[winner].cards
            .mapIndexed { index, crd -> (playerList[winner].cards.size - index) * crd}
            .sum()
            .toString()
    }

    override fun resultPartTwo(): String {
        val playerList = inputLines.splitByCondition { it.isEmpty() }.map { Player(it) }

        val winner = recursiveCombat(playerList, 1)

        return playerList[winner].cards
            .mapIndexed { index, crd -> (playerList[winner].cards.size - index) * crd}
            .sum()
            .toString()
    }

    private fun playGame(playerList: List<Player>): Int {
        while (playerList.none { pl -> pl.cards.isEmpty() }) {
            val cards = playerList.map{it.cards.removeAt(0) }
            val winner = if (cards[0] > cards[1]) 0 else 1
            playerList[winner].cards.add(cards[winner])
            playerList[winner].cards.add(cards[1-winner])
        }
        return if (playerList[0].cards.isNotEmpty()) 0 else 1
    }

    private fun recursiveCombat(playerList: List<Player>, gameNr: Int): Int {
        val historySet = mutableSetOf<String>()
        while (playerList.none { pl -> pl.cards.isEmpty() }) {
            val hashValue = playerList[0].hashString() + playerList[1].hashString()
            if (hashValue in historySet) {
                return 0 //config has happened before in this game. Winner is player 1 (and is called here 0)
            }
            historySet.add(hashValue)

            val cards = playerList.map{it.cards.removeAt(0) }
            val winner = if (playerList[0].cards.size >= cards[0] && playerList[1].cards.size >= cards[1]) {
                recursiveCombat(playerList.mapIndexed{index, pl -> pl.clone(cards[index])}, gameNr+1)
            } else {
                if (cards[0] > cards[1]) 0 else 1
            }

            playerList[winner].cards.add(cards[winner])
            playerList[winner].cards.add(cards[1-winner])
        }
        return if (playerList[0].cards.isNotEmpty()) 0 else 1
    }
}

class Player(inputLines: List<String>) {
    val cards = inputLines.drop(1).map { it.toInt() }.toMutableList()

    fun clone(numberOfCards: Int): Player {
        return Player(listOf("dummy") + cards.subList(0, numberOfCards).map { it.toString() })
    }

    fun hashString(): String {
        return cards.toString()
    }
}
