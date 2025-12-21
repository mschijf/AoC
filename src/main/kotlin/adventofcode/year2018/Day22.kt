package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.coordinate.twodimensional.printAsGrid
import java.util.*

fun main() {
    Day22(test=false).showResult()
}

class Day22(test: Boolean) : PuzzleSolverAbstract(test) {
    private val depth = inputLines.first().substringAfter("depth: ").toInt()
    private val target = pos(inputLines.last().substringAfter("target: "))
    private val mouth = pos(0,0)

    private val geologicalIndex = createGeologicalIndexMap()

    override fun resultPartOne(): Any {
        //geologicalIndex.print()
        return geologicalIndex.riskLevel()
    }

    override fun resultPartTwo(): Any {
        val x = solve()
        //geologicalIndex.print()
        return x
    }


    //1086 --> too high
    //1078 --> too high
    //1054 --> Good!!

    /**
     * Voor de solver maken we gebruik van een paar technieken:
     * 1. we doen een best-first search en daarbij maken we gebruik van
     * 2. een priorityqueue, die de meest veelbelovende vooraan zet
     * 3. een visited map, die check of een bepaalde cell al eerder is bezocht (met dezelfde gear)!
     * 4. Voor de bestfirts search maken we gebruk van een onderschatter: de manhattan-distance
     *    Dit zorgt er vor dat we bijtijds stoppen als we ver uit het grid gaan
     *
     * In de priority queue stoppen we vier gegevens:
     *    de Pos van de cell, de gear die we hebben (samengevoegd in een Pair)
     *    de afgelegde afstand (in minutes)
     *    de minimale nog af te leggen afstand (manhattan distance)
     *
     * Note: de priorityqueue implementatie (van Java) zorgt voor een vertraging, maar al met al nog steeds binnen de seconde
     *       het loont de moeite om in dit geval een eigen priority queue implementatie te schrijven
     *
     */
    private fun solve(): Int {
        val compareByMinutes: Comparator<Triple<Pair<Point, Gear>, Int, Int>> = compareBy { it.second + it.third }
        var nodesVisited = 0L
        val visited = mutableMapOf<Pair<Point, Gear>, Int>()
        var bestValue = Int.MAX_VALUE
        val priorityQueue = PriorityQueue(compareByMinutes)
        priorityQueue.add(Triple(Pair(mouth, Gear.TORCH), 0, mouth.distanceTo(target)))
        while (priorityQueue.isNotEmpty()) {
            nodesVisited++
            val (visitedKey, minutesPassed, manhattanDistance) = priorityQueue.remove()
            val (current, gear) = visitedKey
            if (minutesPassed >= visited.getOrDefault(visitedKey, Int.MAX_VALUE))
                continue
            if (minutesPassed + manhattanDistance >= bestValue)
                continue
            if (current == target) {
                val targetValue = minutesPassed + if (gear == Gear.TORCH) 0 else 7
                if (targetValue < bestValue) {
                    bestValue = targetValue
                    visited[visitedKey] = bestValue
                }
            } else {
                visited[visitedKey] = minutesPassed
            }
            current.neighbors()
                .filter { it.x >= 0 && it.y >= 0 }
                .forEach { neighbor ->
                    geologicalIndex.addRowOrCol(neighbor)
                    val type = geologicalIndex[neighbor]!!.type()
                    if (gear.isLegalFor(type)) {
                        priorityQueue.add(Triple(Pair(neighbor, gear), minutesPassed + 1, neighbor.distanceTo(target)))
                    } else {
                        val newGear = usableGear(geologicalIndex[current]!!.type(), geologicalIndex[neighbor]!!.type())
                        priorityQueue.add(Triple(Pair(neighbor, newGear), minutesPassed + 7 + 1, neighbor.distanceTo(target)))
                    }
                }
        }
        println("Nodes visited: $nodesVisited")
        return bestValue
    }


    private fun Gear.isLegalFor(type: Int): Boolean {
        return when(type) {
            0 -> this == Gear.TORCH || this == Gear.CLIMBING
            1 -> this == Gear.CLIMBING || this == Gear.NEITHER
            2 -> this == Gear.TORCH || this == Gear.NEITHER
            else -> throw Exception("huuuhuhh?")

        }
    }
    private fun usableGear(type1: Int, type2: Int): Gear {
        when (type1) {
            0 -> return if (type2 == 1) Gear.CLIMBING else Gear.TORCH
            1 -> return if (type2 == 0) Gear.CLIMBING else Gear.NEITHER
            2 -> return if (type2 == 0) Gear.TORCH else Gear.NEITHER
        }
        throw Exception("impossible combi")
    }

    private fun MutableMap<Point, Int>.addRowOrCol(pos: Point) {
        if (this.containsKey(pos))
            return
        if (this.containsKey(pos.up())) {
            val maxX = geologicalIndex.keys.maxOf { it.x }
            geologicalIndex[pos(0, pos.y)] = pos.y * 48271
            (1..maxX).forEach { x ->
                val current = pos(x, pos.y)
                this[current] = this[current.left()]!!.erosionLevel() * this[current.up()]!!.erosionLevel()
            }
        } else {
            val maxY = geologicalIndex.keys.maxOf { it.y }
            geologicalIndex[pos(pos.x, 0)] = pos.x * 16807
            (1..maxY).forEach { y ->
                val current = pos(pos.x, y)
                this[current] = this[current.left()]!!.erosionLevel() * this[current.up()]!!.erosionLevel()
            }

        }
    }

    private fun createGeologicalIndexMap(): MutableMap<Point, Int> {
        val geologicalIndex = mutableMapOf<Point, Int>()
        geologicalIndex[mouth] = 0
        (1 .. target.x).forEach{x -> geologicalIndex[pos(x,0)] = x * 16807}
        (1 .. target.y).forEach { y -> geologicalIndex[pos(0, y)] = y * 48271}

        (1 .. target.y).forEach {y ->
            (1 .. target.x).forEach {x ->
                val current = pos(x,y)
                geologicalIndex[current] = geologicalIndex[current.left()]!!.erosionLevel() * geologicalIndex[current.up()]!!.erosionLevel()
            }
        }
        geologicalIndex[target] = 0
        return geologicalIndex
    }

    private fun Map<Point, Int>.riskLevel(): Int {
        return this.filter{it.key.x <= target.x && it.key.y <= target.y}.values.sumOf { it.type() }
    }

    private fun Map<Point, Int>.print() {
        this.printAsGrid("?") { it.typeChar().toString() }
    }

    private fun Int.erosionLevel() = (this + depth) % 20183
    private fun Int.type() = this.erosionLevel() % 3
    private fun Int.typeChar() = when(this.erosionLevel() % 3) {
        0 -> '.' //rocky
        1 -> '=' //wet
        2 -> '|' //narrow
        else -> '?'
    }

}

enum class Gear {
    TORCH, CLIMBING, NEITHER;
}