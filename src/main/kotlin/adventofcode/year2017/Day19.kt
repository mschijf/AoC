package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day19(test = false).showResult()
}

class Day19(test: Boolean) : PuzzleSolverAbstract(test) {

    private val maze = inputLines
        .flatMapIndexed { y: Int, line: String -> line.mapIndexed { x, c -> pos(x, y) to c } }
        .toMap()
        .filterValues { it != ' ' }
    private val mazeCoordinates = maze.keys
    private val start = mazeCoordinates.first { it.y == 0 }

    override fun resultPartOne(): Any {
        return walkingOrder()
            .map { maze[it]!! }
            .filter { it in 'A'..'Z' }
            .joinToString("")
    }

    override fun resultPartTwo(): Any {
        return walkingOrder().size
    }

    /**
     * Hou het wandelpad bij, beginnend bij 'start'
     * Om de volgende stap te bepalen, doen we het volgende:
     *   Vanuit de  laatste stap, kijken we welke buren we in de 'maze' hebben (met uitzondering van de plek waar we vandaan komen)
     *   Als we meerdere buren hebben, dan kiezen we degene die overeenkomt met de richting die we aan het lopen waren
     *       (anders gezegd, die op dezelfde lijn ligt als de lijn tussen het vorige en huige punt)
     *   Hebben we maar één buur, dan kiezen we die.
     *   Hebben we geen buur, dan stoppen we met itereren.
     *   De zo bepaalde buur, is de volgende stap en die wordt aan het pad toegevoegd.
     */
    private fun walkingOrder(): List<Point> {
        val walkingOrder = mutableListOf(start)
        var previous = start
        while (true) {
            val current = walkingOrder.last()
            val nextCandidates = current.neighbors().filterNot{it == previous} intersect mazeCoordinates
            val next = nextCandidates.firstOrNull { it.inLine(current, previous) } ?: nextCandidates.firstOrNull() ?: break
            walkingOrder.add(next)
            previous = current
        }
        return walkingOrder
    }

    private fun Point.inLine(pos1: Point, pos2: Point) =
        this.directionToOrNull(pos1) == pos1.directionToOrNull(pos2)
}


