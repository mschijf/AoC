package adventofcode.year2019.december10

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.xyCoordinate
import tool.math.Fraction
import kotlin.math.absoluteValue

fun main() {
    Day10(test=false).showResult()
}

class Day10(test: Boolean) : PuzzleSolverAbstract(test) {
    private val asteroidPosList = inputLines
        .mapIndexed{y, row ->
            row.toList().mapIndexedNotNull { x, ch -> if (ch == '#') xyCoordinate(x,y) else null } }
        .flatten()

    override fun resultPartOne(): String {
        return asteroidPosList
            .maxOf{thisPos -> canDetectFromPos(thisPos) }
            .toString()
    }

    override fun resultPartTwo(): String {
        val bestPos = asteroidPosList.maxBy{thisPos -> canDetectFromPos(thisPos)}
        val beamList = sortedBeamList(bestPos)
        val asteroidPos = beamList[199][0]
        return (100 * asteroidPos.x + asteroidPos.y).toString()
    }


    private fun canDetectFromPos(pos: Point): Int {
        return asteroidPosList
            .filter{otherPos -> otherPos != pos}
            .map { otherPos -> pos.directionCoefficient(otherPos) }
            .toSet().size
    }

    private fun sortedBeamList(pos: Point): List<List<Point>> {
        val comp = BeamRaySortOrder()
        val beamMap = asteroidPosList
            .filter { otherPos -> otherPos != pos }
            .map { otherPos -> Pair(pos.directionCoefficient(otherPos), otherPos) }
            .groupBy( { it.first }, { it.second} )
            .toSortedMap(comp)
            .map { it.value.sortedBy { otherPos -> pos.manhattanDistance(otherPos) } }
        return beamMap
    }

}

class BeamRaySortOrder: Comparator<Fraction>{
    override fun compare(q1: Fraction?, q2: Fraction?): Int {
        return if (q1 == null || q2 == null || (q1 == q2)) {
            0
        } else if (beamRaySortValue(q1) < beamRaySortValue(q2))
            -1
        else
            1
    }

    private fun beamRaySortValue(quotient: Fraction): Double {
        val directionCoefficient = if (quotient.denominator == 0L)
            quotient.numerator.absoluteValue.toDouble() / 0.001
        else
            quotient.numerator.absoluteValue.toDouble() / quotient.denominator.absoluteValue.toDouble()

        if (quotient.numerator >= 0) {
            if (quotient.denominator < 0) {
                //quadrant upper right         dx > 0, dy < 0
                return -100_000_000 - 1/directionCoefficient
            } else {
                //quadrant lower right         dx > 0, dy >= 0
                return  -1_000_000 - directionCoefficient
            }
        } else {
            if (quotient.denominator > 0) {
                //quadrant lower left          dx <= 0, dy > 0
                return 1_000_000 - 1/directionCoefficient
            } else {
                //quadrant upper left          dx <= 0, dy <= 0
                return 100_000_000 - directionCoefficient
            }
        }
    }
}



fun Point.directionCoefficient(otherPos: Point) = Fraction(otherPos.x - x,otherPos.y - y)
fun Point.manhattanDistance(otherPos: Point) = this.distanceTo(otherPos)



