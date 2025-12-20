package adventofcode.year2021.december21

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val playerPos = inputLines.map {it.substringAfter("starting position: ").toInt() - 1}.toIntArray()
        val playerScore = IntArray(playerPos.size) {0}
        val dice = Dice()

        var playerToMove = 0
        while (playerScore.none {sc -> sc >= 1000}) {
            playerPos[playerToMove] = (playerPos[playerToMove] + dice.getTotalValueOfNextThrow(3)) % 10
            playerScore[playerToMove] = playerScore[playerToMove] + (playerPos[playerToMove] + 1)
            playerToMove = 1 - playerToMove
        }

        return (playerScore[playerToMove] * dice.hasRolled).toString()
    }

    private val playerWins = LongArray(2) {0}

    // uitgerekende voorkennis: 3x gooien met een dobbelsteen met 3 waarden levert 7 verschillende sommen op, van 3..9
    // 3 komt 1 x voor, 4 komt 3 x voor, 5 komt 6 x voor, etc. Zou ook geprogrammeerd kunnen worden, maar dit is makkelijker.
    private val diceToUniverse = arrayOf(0,0,0,1,3,6,7,6,3,1,0,0,0)

    override fun resultPartTwo(): String {

        val playerPos = inputLines.map {it.substringAfter("starting position: ").toInt() - 1}.toIntArray()
        val playerScore = IntArray(playerPos.size) {0}

        solver (0, playerPos, playerScore, 1L)

//        println(playerWins.toList())
        return playerWins.max().toString()
    }

    private fun solver(playerToMove: Int, playerPos: IntArray, playerScore: IntArray, universesCovered: Long) {
        if (playerScore[1-playerToMove] >= 21) {
            playerWins[1-playerToMove] += universesCovered
            return
        }

        for (totalDiceValue in 3 .. 9) {
            playerPos[playerToMove] = (playerPos[playerToMove] + totalDiceValue) % 10
            playerScore[playerToMove] = playerScore[playerToMove] + (playerPos[playerToMove] + 1)

            solver(1-playerToMove, playerPos, playerScore, universesCovered * diceToUniverse[totalDiceValue])

            playerScore[playerToMove] = playerScore[playerToMove] - (playerPos[playerToMove] + 1)
            playerPos[playerToMove] = (playerPos[playerToMove] - totalDiceValue + 10) % 10
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Dice {
    var hasRolled = 0

    fun getTotalValueOfNextThrow(diceThrows: Int): Int {
        var sum = 0
        for (i in hasRolled until hasRolled+diceThrows) {
            sum += (hasRolled % 100) + 1
            hasRolled = (hasRolled + 1)
        }
        return sum
    }
}
