package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

fun main() {
    Day18(test=false).showResult()
}

class Day18(test: Boolean) : PuzzleSolverAbstract(test) {
    private val area = inputLines.flatMapIndexed { y: Int, s: String ->  s.mapIndexed { x, c ->  pos(x,y) to c}}.toMap()

    override fun resultPartOne(): Any {
//        area.printAsGrid { it.toString() }
        return area.valueAfterMinutes(10)
    }

    override fun resultPartTwo(): Any {
        val (first, cycle) = area.findRepetion()
        val start = (1_000_000_000 - (first)) % cycle
        return area.valueAfterMinutes(first+start)
    }

    private fun Map<Point, Char>.findRepetion(): Pair<Int, Int> {
        var newGeneration = this
        var minute = 0
        val knownSettings = mutableListOf(newGeneration.values.joinToString(""))
        while (true) {
            minute++
            newGeneration = newGeneration.waitMinute()
            val hashkey = newGeneration.values.joinToString("")
            if (hashkey in knownSettings) {
                val repeatedWhen = knownSettings.lastIndexOf(hashkey)
                val cycle = minute - repeatedWhen

                println("Repeat!! After $minute (${knownSettings.size}); Earlier $repeatedWhen, Cycle: $cycle")
                return Pair(repeatedWhen, cycle)
            }
            knownSettings.add(hashkey)
        }
    }

    private fun Map<Point, Char>.valueAfterMinutes(minutes: Int): Int {
        var newGeneration = this
        repeat (minutes) {
            newGeneration = newGeneration.waitMinute()
        }
        return newGeneration.values.count { it == '|' } * newGeneration.values.count { it == '#' }
    }


    private fun Map<Point, Char>.waitMinute() : Map<Point, Char> {
        return this.map { field ->
            field.key to
                    when (field.value) {
                        '.' -> if (this.countItem(field.key, '|') >= 3) '|' else '.'
                        '|' -> if (this.countItem(field.key, '#') >= 3) '#' else '|'
                        '#' -> if ((this.countItem(field.key, '#') >= 1) && (this.countItem(field.key, '|') >= 1)) '#' else '.'
                        else -> throw Exception("unexpected value")
                    }
        }.toMap()
    }

    private fun Map<Point, Char>.countItem(fromPos: Point, item: Char): Int {
//        return this.filterKeys { it in fromPos.allWindDirectionNeighbors() }.count{it.value == item}
        var count=0
        fromPos.allWindDirectionNeighbors().forEach {
            if (this[it] == item)
                count++
        }
        return count
    }
}


