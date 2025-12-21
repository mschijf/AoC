package adventofcode.year2018.december03

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.collectioncombination.mapCombinedItems

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


