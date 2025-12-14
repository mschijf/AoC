package adventofcode.year2020.december20

import java.lang.Exception
import kotlin.math.sqrt

typealias WIPArrangement = MutableList<MutableList<TileConfig?>>

class SquareArrangement(tileList: List<Tile>) {
    private val arrangementList = getArrangementList(tileList)

    fun getFirstArrangement(): Arrangement {
        return arrangementList[0]
    }

    fun getArrangementWithSeaMonsters(): Arrangement {
        arrangementList.forEach { arrangement ->
            val roughness = Image(arrangement).getRoughNess()
            if (roughness >= 0)
                return arrangement
        }
        return arrangementList[0]
    }

    private fun getArrangementList(tileList: List<Tile>): List<Arrangement> {
        val arrangementWidth = sqrt(tileList.size.toDouble() + 0.01).toInt()
        val emptyArrangement = MutableList(arrangementWidth){ MutableList<TileConfig?>(arrangementWidth) {null} }
        return solver(emptyArrangement, tileList.toSet(), 0, 0)
    }

    private fun solver(wipArrangement: WIPArrangement, tilesLeft: Set<Tile>, newRow: Int, newCol:Int): List<Arrangement> {
        if (newRow >= wipArrangement.size) {
            if (tilesLeft.isNotEmpty())
                throw Exception("That should not be possible")
            return listOf(Arrangement(wipArrangement))
        }

        val possibleTileSet = getPossibleTileSet(wipArrangement, tilesLeft, newRow, newCol)

        val allSolutions = mutableListOf<Arrangement>()
        possibleTileSet.forEach {aTileConfig ->
            wipArrangement[newRow][newCol] = aTileConfig
            val nextCol = (newCol+1) % wipArrangement.size
            val nextRow = if (nextCol == 0) newRow+1 else newRow
            val solution = solver(wipArrangement, tilesLeft.minus(aTileConfig.tile), nextRow, nextCol)
            allSolutions.addAll(solution)
            wipArrangement[newRow][newCol] = null
        }
        return allSolutions
    }

    private fun getPossibleTileSet(arrangement: WIPArrangement, tilesLeft: Set<Tile>, newRow: Int, newCol:Int): Set<TileConfig> {
        return if (newRow > 0 && newCol > 0) {
            arrangement[newRow-1][newCol]!!.bottomMatch intersect arrangement[newRow][newCol-1]!!.rightMatch
        } else if (newRow > 0) {
            arrangement[newRow-1][newCol]!!.bottomMatch
        } else if (newCol > 0) {
            arrangement[newRow][newCol-1]!!.rightMatch
        } else {
            tilesLeft.map { tile -> tile.tileConfigList }.flatten().toSet()
        }
    }
}