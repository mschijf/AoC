package adventofcode.year2020.december24

import adventofcode.PuzzleSolverAbstract
import java.lang.Exception

fun main() {
    Day24(test=false).showResult()
}

class Day24(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val blackSet = initialBlackSet()
        return blackSet.count().toString()
    }

    override fun resultPartTwo(): String {
        var blackSet = initialBlackSet()//inputLines.map { DirectionSeries(it) }.map { it.finalTile() }.toSet()

        repeat(100) {
            val blackRemainsBlack = blackSet
                .filter { hex -> !isBlackFlipsToWhite(hex, blackSet) }
            val whiteBecomesBlack = blackSet
                .map { hex -> hex.getNeighbours().filter { nb -> !blackSet.contains(nb) } }
                .flatten()
                .distinct()
                .filter { hex -> isWhiteFlipsToBlack(hex, blackSet) }

            blackSet = blackRemainsBlack.toSet() + whiteBecomesBlack.toSet()
//            println("Day ${it+1}: ${blackSet.count()}")
        }

        return blackSet.count().toString()
    }

    private fun isBlackFlipsToWhite(hex: Hexagon, blackSet: Set<Hexagon>): Boolean {
        val blackNeighbours = hex.getNeighbours().count{it in blackSet}
        return blackNeighbours == 0 || blackNeighbours > 2
    }

    private fun isWhiteFlipsToBlack(hex: Hexagon, blackSet: Set<Hexagon>): Boolean {
        val blackNeighbours = hex.getNeighbours().count{it in blackSet}
        return blackNeighbours == 2
    }

    private fun initialBlackSet(): Set<Hexagon> {
        val directionSeriesList = inputLines.map { DirectionSeries(it) }
        val blackSet = mutableSetOf<Hexagon>()
        directionSeriesList.map{it.finalTile()}.forEach { hexagon ->
            if (hexagon in blackSet) {
                blackSet.remove(hexagon)
            } else {
                blackSet.add(hexagon)
            }
        }
        return blackSet
    }
}

class DirectionSeries(input:String) {
    private var list = mutableListOf<HexagonDirection>()
    init {
        var i = 0
        while (i < input.length) {
            val dir = when (input[i]) {
                'e' -> HexagonDirection.EAST
                'w' -> HexagonDirection.WEST
                'n' -> if (input[i+1] == 'e') HexagonDirection.NORTHEAST else HexagonDirection.NORTHWEST
                's' -> if (input[i+1] == 'e') HexagonDirection.SOUTHEAST else HexagonDirection.SOUTHWEST
                else -> throw Exception("unxepcetd input direction")
            }
            list.add(dir)
            i += if (dir == HexagonDirection.EAST || dir == HexagonDirection.WEST) 1 else 2
        }
    }

    fun finalTile(): Hexagon {
        var hex = Hexagon(0,0,0)
        list.forEach { dir -> hex = hex.moveTo(dir) }
        return hex
    }
}


