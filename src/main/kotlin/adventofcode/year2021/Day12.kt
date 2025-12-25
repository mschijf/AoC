package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract
import kotlin.also

fun main() {
    Day12(test=false).showResult()
}

class Day12(test: Boolean) : PuzzleSolverAbstract(test, puzzleName="TBD", hasInputFile = true){

    private val caveMap = createCaveMap(inputLines)

    override fun resultPartOne(): Any {
        return caveMap.countPaths("start")
    }

    override fun resultPartTwo(): Any {
        return caveMap.countPaths2("start")
    }

    private fun Map<String, Cave>.countPaths(current: String,
                                             alreadyVisited: Set<String> = emptySet(),
                                             cache: MutableMap<Pair<String, Set<String>>, Int> = mutableMapOf()): Int {
        if (current == "end")
            return 1

        val key = Pair(current, alreadyVisited)
        if (cache.contains(key))
            return cache[key]!!

        return this[current]!!.connections().filter { !it.visitOnlyOnce() || it !in alreadyVisited }.sumOf { cn ->
            this.countPaths(cn, alreadyVisited + current, cache)
        }.also { cache[key] = it }
    }

    private fun Map<String, Cave>.countPaths2(current: String,
                                              alreadyVisited: Set<String> = emptySet(),
                                              singleOneVisitedTwice: String? = null,
                                              cache: MutableMap<Triple<String, String?, Set<String>>, Int> = mutableMapOf()): Int {
        if (current == "end")
            return 1

        if (current == "start" && current in alreadyVisited)
            return 0

        val key = Triple(current, singleOneVisitedTwice,alreadyVisited)
        if (cache.contains(key))
            return cache[key]!!

        return if (current.visitOnlyOnce()) {
            if (current in alreadyVisited)
                if (singleOneVisitedTwice != null)
                    0
                else
                    this[current]!!.connections().sumOf { cn ->
                        this.countPaths2(cn, alreadyVisited + current, current, cache)
                    }
            else
                this[current]!!.connections().sumOf { cn ->
                    this.countPaths2(cn, alreadyVisited + current, singleOneVisitedTwice, cache)
                }
        } else {
            this[current]!!.connections().sumOf { cn ->
                this.countPaths2(cn, alreadyVisited + current, singleOneVisitedTwice, cache)
            }
        }.also { cache[key] = it }

    }


    private fun String.visitOnlyOnce() = this.lowercase() == this


    private fun createCaveMap(connectLines: List<String>) : Map<String, Cave> {
        val caveMap = connectLines.flatMap { it.split("-") }.distinct().associateWith { Cave(it) }
        inputLines.forEach { cn ->
            val (from, to) = cn.split("-")
            caveMap[from]!!.addConnection(to)
            caveMap[to]!!.addConnection(from)
        }
        return caveMap
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Cave(val name: String) {
    private val connections = mutableSetOf<String>()

    fun addConnection(other: String) {
        connections.add(other)
    }

    fun connections() = connections.toList()

    override fun toString() = "$name : [${connections.joinToString (", ") }]"
}

