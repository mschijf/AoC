package adventofcode.year2018.december03

import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import kotlin.math.max
import kotlin.math.min

//
// #1 @ 1,3: 4x4
// #2 @ 3,1: 4x4
// #3 @ 5,5: 2x2
//

data class Rectangle(
    val minX: Int,
    val maxX: Int,
    val minY: Int,
    val maxY: Int) {

    companion object {
        fun fromString(s: String): Rectangle {
            val width = s.substringAfter(": ").substringBefore("x").toInt()
            val height = s.substringAfter("x").toInt()
            val minX = s.substringAfter(" @ ").substringBefore(",").toInt()
            val minY = s.substringAfter(",").substringBefore(":").toInt()
            return Rectangle(
                minX = minX,
                minY = minY,
                maxX = minX + width - 1,
                maxY = minY + height - 1)
        }
    }

    fun asCoordinates(): List<Point> =
        (minX .. maxX).flatMap { x ->
            (minY .. maxY).map { y ->
                pos(x, y)
            }
        }

    fun area() = (1 + maxX - minX) * (1 + maxY - minY)

    fun intersectionOrNull(other: Rectangle?): Rectangle? {
        if (other == null)
            return null

        val iMinX = max(minX, other.minX)
        val iMaxX = min(maxX, other.maxX)
        val iMinY = max(minY, other.minY)
        val iMaxY = min(maxY, other.maxY)
        return if (iMinX <= iMaxX && iMinY <= iMaxY)
            Rectangle(iMinX, iMaxX, iMinY, iMaxY)
        else
            null
    }

    fun plus(other: Rectangle?): List<Rectangle> {
        if (other == null)
            return listOf(this)

        val intersection = intersectionOrNull(other) ?: return listOf(this, other)

        return this.minus(intersection) + other.minus(intersection) + listOf(intersection)
    }

    fun minus(other: Rectangle?): List<Rectangle> {
        if (other == null)
            return listOf(this)

        val intersection = intersectionOrNull(other) ?: return listOf(this)

        var minX = minX
        var maxX = maxX

        val result = mutableListOf<Rectangle>()
        if (minX < intersection.minX) {
            result.add(Rectangle(minX, intersection.minX-1, minY, maxY))
            minX = intersection.minX
        }
        if (intersection.maxX < maxX) {
            result.add(Rectangle(intersection.maxX+1, maxX, minY, maxY))
            maxX = intersection.maxX
        }
        if (minY < intersection.minY) {
            result.add(Rectangle(minX, maxX, minY, intersection.minY-1))
        }
        if (intersection.maxY < maxY) {
            result.add(Rectangle(minX, maxX, intersection.maxY+1, maxY))
        }
        return result
    }



}