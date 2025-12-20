package adventofcode.year2018.december15

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.coordinate.twodimensional.printAsGrid

fun main() {
    PuzzleSolver(test = false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val caveWalls = inputLines.flatMapIndexed { y: Int, s: String ->
        s.mapIndexed { x, c -> if (c == '#') pos(x,y) else null }
    }.filterNotNull().toSet()

    private val elfList = inputLines.flatMapIndexed { y: Int, s: String ->
        s.mapIndexed { x, c -> if (c == 'E') Elf(pos(x, y)) else null }
    }.filterNotNull().toSet()

    private val goblinList = inputLines.flatMapIndexed { y: Int, s: String ->
        s.mapIndexed { x, c -> if (c == 'G') Goblin (pos(x, y)) else null }
    }.filterNotNull().toSet()

    override fun resultPartOne(): Any {
        return doCombat()
    }

    override fun resultPartTwo(): Any {
        var elfPower = 3
        var result: String
        do {
            elfPower++
            elfList.forEach { it.reset(elfPower) }
            goblinList.forEach { it.reset() }
            result = doCombat()
        } while (elfList.count { it.isAlive() } != elfList.size)
        return "$elfPower: $result"
    }

    private fun doCombat(): String {
        var round = 0
        while (true) {
            if (doRound())
                round++
            else
                break
        }
        val sumAlive = (elfList + goblinList).filter { it.isAlive() }.sumOf { it.hitPoints }
        return "$round * $sumAlive = ${round*sumAlive}"
    }

    private fun doRound(): Boolean {
        val unitList = elfList + goblinList
        unitList.sorted().forEach { unit ->
            if (unit.isAlive())
                if (!unit.doAction())
                    return false
        }
        return true
    }

    private fun printCave() {
        val elfs = elfList.filter { it.isAlive() }.map{it.pos}
        val goblins = goblinList.filter { it.isAlive() }.map{it.pos}
        (elfs + goblins + caveWalls).printAsGrid { pos ->
            when (pos) {
                in elfs -> "E"
                in goblins -> "G"
                in caveWalls -> "#"
                else -> "."
            }
        }
    }


    private fun Unit.doAction(): Boolean {
        val elfs = elfList.filter { it.isAlive() }.map{it.pos}.toSet()
        val goblins = goblinList.filter { it.isAlive() }.map{it.pos}.toSet()
        val opponents = if (this is Elf) goblins else elfs

        if (opponents.isEmpty())
            return false
        if (this.pos.neighbors().intersect(opponents).isEmpty()) {
            // this one can move
            val blocked = (elfs + goblins + caveWalls)
            val inRange = opponents.flatMap { it.neighbors() }.filterNot { it in blocked }.toSet()
            val nearestInRange = inRange.nearestInRangeOption(this.pos, blocked)
            if (nearestInRange != null) {
                val nextStep = this.pos.nextStep(nearestInRange, blocked)
                this.moveTo(nextStep)
            }
        }

        val underAttack = this.pos.neighbors().intersect(opponents)
        if (underAttack.isNotEmpty()) {
            val unitsUnderAttack = underAttack.map{coord -> coord.toUnit()}
            val unitToAttack = unitsUnderAttack.minBy { 10_000_000*it.hitPoints + 1_000*it.pos.y + it.pos.x}
            unitToAttack.beAttacked(this.gunPower())
        }

        return true
    }

    private fun Point.toUnit(): Unit {
        return (goblinList + elfList).filter { it.isAlive() }.first { it.pos == this }
    }



    private fun Set<Point>.nearestInRangeOption(from: Point, blocked: Set<Point>): Point? {
        val result = mutableMapOf<Point, Int>().apply{ this[from] = 0 }

        var start = 0
        while (true) {
            val neighbours = result
                .filter { it.value == start }.keys.flatMap { it.neighbors() }
                .filterNot { it in blocked || it in result.keys }
            neighbours.forEach { result[it] = start + 1 }
            start++

            if (neighbours.isEmpty())
                return null

            val nearest = neighbours.intersect(this)
            if (nearest.isNotEmpty()) {
                return nearest.minBy { it.y * 1_000_000 + it.x}
            }
        }
    }

    private fun Point.nextStep(to: Point, blocked: Set<Point>): Point {
        val result = mutableMapOf<Point, Int>().apply{ this[to] = 0 }

        var start = 0
        var neighbours = result.keys.toList()
        while (true) {
            val nextStepOptions = this.neighbors().intersect(neighbours.toSet())
            if (nextStepOptions.isNotEmpty()) {
                return nextStepOptions.minBy { it.y * 1_000_000 + it.x}
            }

            neighbours = result
                .filter { it.value == start }.keys.flatMap { it.neighbors() }
                .filterNot { it in (blocked) || it in result.keys }
            neighbours.forEach { result[it] = start + 1 }
            start++

            if (neighbours.isEmpty())
                throw Exception("Can't find next step!!")

        }
    }
}


abstract class Unit(var pos: Point) : Comparable<Unit> {
    private val initialPos = pos
    override fun compareTo(other: Unit): Int {
        return compareValuesBy(this, other, { it.pos.y }, { it.pos.x })
    }

    override fun toString(): String {
        return "(${pos.x},${pos.y}) has $hitPoints hit points"
    }

    fun moveTo(newPos: Point) {
        pos = newPos
    }

    var hitPoints = 200
    fun beAttacked(byHitPoints: Int) {
        hitPoints -= byHitPoints
    }

    private var gunPower = 3
    fun gunPower() = gunPower
    fun isAlive() = hitPoints > 0

    fun reset(newGunPower: Int=3) {
        pos = initialPos
        hitPoints = 200
        gunPower = newGunPower
    }

}


class Goblin(pos: Point): Unit(pos) {
    override fun toString(): String {
        return "Goblin: " + super.toString()
    }
}

class Elf(pos: Point): Unit(pos) {
    override fun toString(): String {
        return "Elf: " + super.toString()
    }

}

