package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.mylambdas.collectioncombination.mapCombinedItems
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day03(test=false).showResult()
}

class Day03(test: Boolean) : PuzzleSolverAbstract(test) {

    private val rectangleList = inputLines.map{Rectangle.fromString(it)}

    override fun resultPartOne(): Any {
        return rectangleList
            .mapCombinedItems { rectangle, rectangle2 -> rectangle.intersectionOrNull(rectangle2) }
            .fold(RectangleSet()) {acc, next -> acc.plus(next)}
            .area()

// Todd Ginsberg Solution
//
//        return rectangleList
//            .flatMap { it.asCoordinates() }  // List<Coordinate>
//            .groupingBy { it }
//            .eachCount()                     // Map<Coordinate, Int>
//            .count { it.value > 1 }
    }

    override fun resultPartTwo(): Any {
        var firstNonOverlapped = 0
        for (i in 0 until rectangleList.size) {
            var hasOverlap = false
            for (j in 0 until rectangleList.size) {
                if (i != j && rectangleList[i].intersectionOrNull(rectangleList[j]) != null) {
                    hasOverlap = true
                    break
                }
            }
            if (!hasOverlap) {
                firstNonOverlapped = i+1
                break
            }
        }
        return firstNonOverlapped
    }
}

//======================================================================================================================

data class Rectangle(
    val minX: Int,
    val maxX: Int,
    val minY: Int,
    val maxY: Int) {

    companion object {
        //
        // #1 @ 1,3: 4x4
        // #2 @ 3,1: 4x4
        // #3 @ 5,5: 2x2
        //
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

data class RectangleSet(
    private val rectangleList: List<Rectangle> = emptyList()) {

    fun area() = rectangleList.sumOf { rectangle -> rectangle.area() }

    fun plus(newRectangle: Rectangle?): RectangleSet {
        if (newRectangle == null)
            return this
        var leftOver = listOf(newRectangle)
        for (rectangle in rectangleList) {
            leftOver = leftOver.flatMap { leftOverRectangle -> leftOverRectangle.minus(rectangle) }
        }
        return RectangleSet(rectangleList + leftOver)
    }

    fun minus(rectangle: Rectangle?): RectangleSet {
        return RectangleSet(rectangleList.map { it.minus(rectangle) }.flatten())
    }
}


