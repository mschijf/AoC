package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.*

fun main() {
    Day21(test=false).showResult()
}


class Day21(test: Boolean) : PuzzleSolverAbstract(test) {

    private val ruleMap = inputLines.flatMap{it.toListOfRulesForAllVariants()}.associate { rule -> rule.first to rule.second }
    private val start = Grid(Square.of(".#./..#/###").points, 3)

    override fun resultPartOne(): Any {
        var current = start
//        current.print()

        repeat(5) {
            current = current.enhance(ruleMap)
//            current.print()
        }

        return current.points.size
    }

    override fun resultPartTwo(): Any {
        var current = start
        repeat(18) {
            current = current.enhance(ruleMap)
        }
        return current.points.size
    }

    private fun Grid.print() {
        Pair(pos(0,0), pos(this.size-1, this.size-1)).printGrid { if (it in this.points) "#" else "." }
        println()
    }

    private fun String.toListOfRulesForAllVariants(): List<Pair<Square, Square>> {
        val left = Square.of(this.substringBefore(" => "))
        val right = Square.of(this.substringAfter(" => "))
        return left.allVariants().map{Pair(it, right)}
    }

}


data class Square(val points: List<Point>, val size: Int) {

    private val hashValue = (1 shl (16+size)) +  points.sumOf {p -> 1 shl (size*p.y + p.x) }

    private fun rotateRight() = Square(points.map { pos(-it.y, it.x).plusXY(size-1,0) }, size)
    private fun flip() = Square(points.map{ pos(size - 1 - it.x, it.y) }, size)

    fun allVariants() =
        listOf(
            this,
            this.rotateRight(), this.rotateRight().rotateRight(), this.rotateRight().rotateRight().rotateRight(),
            this.flip(),
            this.flip().rotateRight(), this.flip().rotateRight().rotateRight(), this.flip().rotateRight().rotateRight().rotateRight()
        )

    override fun equals(other: Any?): Boolean {
        return if (other is Square)
            (hashValue == other.hashValue)
        else
            super.equals(other)
    }

    override fun hashCode() = hashValue

    companion object {
        fun of(rawInput: String): Square {
            val rows = rawInput.split("/")
            return Square(
                rows.flatMapIndexed { y, s -> s.mapIndexed { x, c -> if (c == '#') pos(x, y) else null }.filterNotNull() },
                rows.size)
        }
    }
}

data class Grid(val points: List<Point>, val size: Int) {

    fun enhance(rules: Map<Square, Square>): Grid {
        val squareSize = if (size % 2 == 0) 2 else 3

        //create a map with key the 'big coordinate' and as value the individual Points belonging to that big coordinate
        val pointGroups = allKeysMapEmpty(squareSize) + points.groupBy { pos(it.x/squareSize, it.y/squareSize) }

        //transpose each set of points to normalized (i.e. move towards (0,0) ) and turn it into a square
        val squares = pointGroups.mapValues { v -> Square(v.value.transpose(-squareSize * v.key.x, -squareSize * v.key.y), squareSize) }

        //look for each square in the rules and find the enhanced square
        val enhanced = squares.mapValues { v -> rules[v.value]!! }

        //transpose each point back to the relative big coordinate position
        val transposeBack = enhanced.flatMap { entry -> entry.value.points.transpose(entry.value.size*entry.key.x, entry.value.size*entry.key.y)}

        //return as grid
        return Grid(transposeBack, if (size % 2 == 0) 3*size/2 else 4*size/3 )
    }

    private fun allKeysMapEmpty(squareSize: Int) =
        posRange(pos(0,0), pos(size/squareSize-1, size/squareSize-1)).associateWith { emptyList<Point>() }

    private fun List<Point>.transpose(dx: Int, dy: Int) =
        this.map { point -> point.plusXY(dx, dy) }

}