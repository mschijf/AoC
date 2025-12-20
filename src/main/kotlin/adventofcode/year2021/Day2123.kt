package adventofcode.year2021

import kotlin.math.absoluteValue
import kotlin.math.min

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day2123(test=true).showResult()
}

class Day2123(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val board = Board(inputLines)
        val result = board.solve(0, 1_000_000_000)
        return result.toString()
    }

//    Test input:
//    Result part 1: 12521 (after 0.096 sec)
//    Result part 2: 44169 (after 73.782 sec)

//    Real input:
//    Result part 1: 18051 (after 0.537 sec)
//    Result part 2: 50245 (after 7.641 sec)

    override fun resultPartTwo(): String {
        val extraLines = listOf("  #D#C#B#A#", "  #D#B#A#C#")
        val board = Board(inputLines.subList(0,3) + extraLines + inputLines.subList(3,5))

        val result = board.solve(0, 1_000_000_000)

        return result.toString()
    }

}

//----------------------------------------------------------------------------------------------------------------------

class Board(inputLines: List<String>) {
    private val board = inputLines.mapIndexed{row, str -> str.mapIndexed{col, ch -> PosInfo(row, col, ch)} }

    val valuePerLetter = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
    private fun generateMoves() = board.flatten().filter { it.hasLetter() }.map{ it.generateMoves() }.flatten()

    private fun isEndPosition():Boolean {
        for (letter in 'A' .. 'D') {
            val letterCol = 2*(letter-'A') + 3
            for (row in 2 .. board.size - 2)
                if ( !board[row][letterCol].hasLetter(letter) )
                    return false
        }
        return true
    }

    private fun minimalValueTillEnd(): Int {
        var total = 0
        for (posInfo in board.flatten().filter { it.hasLetter() }) {
            total += posInfo.fastwayHome()
        }
        return total
    }

    private var countNodes = 0L
    fun solve(cost: Int, minimalSoFar: Int): Int {
        countNodes++
        if (isEndPosition()) {
            return cost
        }

        if (cost+minimalValueTillEnd() >= minimalSoFar) {
            return 1_000_000_000
        }

        var minimalValue = minimalSoFar
        val moves = generateMoves()
        for (move in moves) {
            move.fromPos.moveLetterTo(move.toPos)
            minimalValue = min(minimalValue, solve(cost+move.cost, minimalValue))
            move.toPos.moveLetterTo(move.fromPos)
        }
        return minimalValue
    }


    inner class PosInfo(
        private val row: Int,
        private val col: Int,
        private var letter: Char) {

        fun moveLetterTo(to: PosInfo) {
            if (letter !in "ABCD")
                throw Exception("Hee, geen letter die we willen verplaatsen....")
            to.letter = this.letter
            this.letter = '.'
        }


        fun distance(to: PosInfo): Int {
            return (this.row - to.row).absoluteValue + (this.col - to.col).absoluteValue
        }


        private fun isWall() = letter == '#'
        private fun isGround() = letter in ".ABCD"
        private fun isFree() = letter == '.'
        private fun isOutside() = letter == ' '
        private fun isPillar() = isGround() && row > 1
        private fun isHallway() = isGround() && row == 1
        private fun isBlocked() =  isWall() || isOutside() || hasLetter()

        fun hasOtherLetter(ch: Char) =  hasLetter() && letter != ch
        fun hasLetter(ch: Char) =  letter == ch
        fun hasLetter() =  letter in "ABCD"
        fun getLetter() = letter


        fun generateMoves(): List<Move> {
            if (!hasLetter())
                return emptyList()

            if (isHallway() && ownPillarReachable() && !otherLetterInOwnPillar())
                return listOf(Move(this, board[lowestEmptyFieldInOwnPillar()][ownPillarCol()]))

            if (isPillar() && !isOwnPillar() && hallwayReachable())
                return allReachableHallwayPlaces()

            if (isOwnPillar() && otherLetterBelow() && hallwayReachable())
                return allReachableHallwayPlaces()

            return emptyList()
        }

        private fun allReachableHallwayPlaces(): List<Move> {
            val result = mutableListOf<Move>()
            for (walk in col+1 .. 11) {
                if (board[1][walk].isBlocked())
                    break
                if (!board[2][walk].isPillar() )
                    result.add(Move(this, board[1][walk]))
            }
            for (walk in col-1 downTo  1) {
                if (board[1][walk].isBlocked())
                    break
                if (!board[2][walk].isPillar() )
                    result.add(Move(this, board[1][walk]))
            }
            return result
        }

        private fun ownPillarReachable(): Boolean {
            if (col < ownPillarCol()) {
                for (walk in col+1..ownPillarCol())
                    if (board[row][walk].isBlocked())
                        return false
            } else {
                for (walk in col-1 downTo ownPillarCol())
                    if (board[row][walk].isBlocked())
                        return false
            }
            return board[2][ownPillarCol()].isFree()
        }

        private fun hallwayReachable(): Boolean {
            return board[row-1][col].isFree()
        }

        private fun isOwnPillar(): Boolean {
            return isPillar() && col == ownPillarCol()
        }

        private fun otherLetterInOwnPillar(): Boolean {
            for (row in 2 .. board.size - 2)
                if (board[row][ownPillarCol()].hasOtherLetter(letter))
                    return true
            return false
        }

        private fun otherLetterBelow(): Boolean {
            for (row in this.row+1 .. board.size - 2)
                if (board[row][col].hasOtherLetter(letter))
                    return true
            return false
        }

        private fun lowestEmptyFieldInOwnPillar(): Int {
            for (row in board.size - 2 downTo 2)
                if (board[row][ownPillarCol()].isFree())
                    return row
            throw Exception("KAN NIET")
        }

        private fun ownPillarCol() = 2*(letter-'A') + 3

        fun fastwayHome(): Int {
            val letterValue = valuePerLetter[letter]!!

            var toBeFilled = 0
            for (row in board.size - 2 downTo 3)
                if (board[row][ownPillarCol()].letter != letter)
                    toBeFilled++
            val extra = (toBeFilled * (toBeFilled + 1) / 2 ) * letterValue / (toBeFilled + 1)

            if (isOwnPillar())
                return if (otherLetterBelow()) (row-1 + 3)* letterValue + extra else 0
            if (isHallway())
                return ((col - ownPillarCol()).absoluteValue + 1) * letterValue + extra
            return ((col - ownPillarCol()).absoluteValue + (row-1) + 1) * letterValue + extra
        }
    }

    inner class Move(val fromPos: PosInfo, val toPos: PosInfo) {
        val cost = (fromPos.distance(toPos) * valuePerLetter[fromPos.getLetter()]!!)
    }

}


