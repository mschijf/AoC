package adventofcode.year2019.december18

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import java.util.*


//
// Oplossing alogoritmisch correct, maar performance technisch niet goed.
// Met hulp van Todd Ginsberg' https://todd.ginsberg.com/post/advent-of-code/2019/day18/ zaken verbetert
//
// Voor helderheid twee solve alogoritmes. Verschil is dat bij de tweede een set van startpunten wordt meegegeven
// en bij de eerste maar één punt.

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val maze = Maze.from(inputLines)
        return maze.solve1().toString()
    }

    override fun resultPartTwo(): String {
        val maze = Maze.from(inputLines("example_part2", "input_part2"))
        return maze.solve2().toString()
    }

}

class Maze(maze: Map<Point, Char>) {
    private val doors = maze.filterValues { it.isUpperCase() }
    private val doorKeys = maze.filterValues { it.isLowerCase() }
    private val openSpaces = maze.filterValues { it != '#' }.map{it.key}.toSet()
    private val entrance = maze.filterValues { it == '@' }.keys

    companion object {
        fun from(inputLines: List<String>): Maze {
            return Maze(inputLines
                .mapIndexed{y, row -> row.mapIndexed {x, cell -> pos(x,y) to cell}}
                .flatten()
                .toMap())
        }
    }

    private val cache = HashMap<Pair<Point, Set<Char>>, Int>()
    fun solve1(currentPos: Point = entrance.first(), keysPicked :Set<Char> = emptySet()): Int {
        if (keysPicked.size == doorKeys.size) {
            return 0
        }

        val cacheKey = Pair(currentPos, keysPicked)
        val cacheValue = cache[cacheKey]?:-1
        if (cacheValue >= 0) {
            return cacheValue
        }

        val keysToCatch = determineKeysToCatchFrom(currentPos, keysPicked)
        val bestDistanceFromHere = keysToCatch.minOfOrNull { entry -> entry.distance + solve1(entry.to,keysPicked+entry.key)} ?: 0

        cache[cacheKey] = bestDistanceFromHere
        return bestDistanceFromHere
    }

    private val cache2 = HashMap<Pair<Set<Point>, Set<Char>>, Int>()
    fun solve2(currentPosSet: Set<Point> = entrance, keysPicked :Set<Char> = emptySet()): Int {
        if (keysPicked.size == doorKeys.size) {
            return 0
        }

        val cacheKey = Pair(currentPosSet, keysPicked)
        val cacheValue = cache2[cacheKey]?:-1
        if (cacheValue >= 0) {
            return cacheValue
        }

        val keysToCatch = determineAllKeys(currentPosSet, keysPicked)
        val bestDistanceFromHere = keysToCatch.minOfOrNull { entry ->
            entry.distance + solve2(currentPosSet-entry.from+entry.to,keysPicked+entry.key)
        } ?: 0

        cache2[cacheKey] = bestDistanceFromHere
        return bestDistanceFromHere
    }


    private fun determineAllKeys(from: Set<Point>, keysPicked: Set<Char>): List<Move> {
        return from.map { point -> determineKeysToCatchFrom(point, keysPicked)}.flatten()
    }

    private fun determineKeysToCatchFrom(aPos: Point, keysPicked:Set<Char>): List<Move> {
        val result = mutableListOf<Move>()
        val queue = ArrayDeque<Pair<Point, Int>>()

        val visited = mutableSetOf<Point>()
        queue.add(Pair(aPos, 0))
        while (queue.isNotEmpty()) {
            val currentPos = queue.pop()
            visited.add(currentPos.first)
            val posList = currentPos.first.neighbors().filter { it in openSpaces }.filterNot { it in visited }
            posList.forEach {
                val door = doors[it]
                val key = doorKeys[it]
                if (door == null || door.lowercaseChar() in keysPicked) {
                    if (key != null && key !in keysPicked) {
                        result.add(Move(aPos, it, key, currentPos.second + 1))
                    } else {
                        queue.add(Pair(it, currentPos.second + 1))
                    }
                }
            }
        }
        return result
    }

    data class Move(val from: Point, val to: Point, val key: Char, val distance: Int)
}
