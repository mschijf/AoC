package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import adventofcode2017.knotHashEncode
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.mylambdas.intersects
import java.math.BigInteger

fun main() {
    Day1714(test=false).showResult()
}

class Day1714(test: Boolean) : PuzzleSolverAbstract(test) {

    private val keyString = if (test) "flqrgnkx" else "hxtvlmkl"

    private val usedSquares = (0..127)
        .flatMap{row->"$keyString-$row".knotHashEncode().hexToBin().mapIndexed{col, ch -> if (ch == '1') pos(col, row) else null}}
        .filterNotNull()

    override fun resultPartOne(): Any {
        return usedSquares.size
    }

    override fun resultPartTwo(): Any {
        var regions = listOf(emptySet<Point>())
        usedSquares.forEach { square ->
            val neighborSet = square.neighbors().toSet()
            regions = regions
                .partition { region -> region.intersects(neighborSet) }
                .run { listOf(this.first.flatten().toSet() + square) + this.second }
        }
        return regions.filterNot { it.isEmpty() }.size
    }

    /**
     * Little trick to create voorloop nullen:
     *  add a '1' in front of the string, before transforming it to a bit string.
     *  this one will lead to a binary '1' as first char in the whole bit string.
     *  and when we remove the first char from the beginning, we have the right bit string, including voorloop nullen
     */
    private fun String.hexToBin(): String {
        return BigInteger("1$this", 16).toString(2).format().drop(1)
    }
}


