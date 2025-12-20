package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import tool.mylambdas.substringBetween
import java.util.PriorityQueue

fun main() {
    Day09(test=true).showResult()
}

class Day09(test: Boolean) : PuzzleSolverAbstract(test) {

    private val citySet =
        inputLines.flatMap{ val (from, to, _) = it.toCityToCity(); listOf(from, to)}.toSet()

    private val distanceMap =
        inputLines.associate { val (from, to, distance) = it.toCityToCity(); Pair(from, to) to distance }

    override fun resultPartOne(): Any {
        return shortestRoute()
    }

    // BFS with priority queue with sorting by longest distance does not work
    // note: see for instance: https://hackernoon.com/shortest-and-longest-path-algorithms-job-interview-cheatsheet-2adc8e18869
    //       priority queue (form of dijkstrats implementation) doesn't work with negative values
    //       which is actually what we do when finding the longest value, when we sort descending
    //
    override fun resultPartTwo(): Any {
        return longestRecursive()
    }


    private fun shortestRoute(): Int {
        val compareByDistance: Comparator<Pair<List<String>, Int>> = compareBy{ it.second }
        val queue = PriorityQueue(compareByDistance).apply { this.addAll( citySet.map{aCity -> Pair(listOf(aCity), 0)} ) }

        while (queue.isNotEmpty()) {
            val (citiesVisited, milesWalked) = queue.remove()
            if (citiesVisited.size == citySet.size)
                return milesWalked

            val fromCity = citiesVisited.last()
            (citySet - citiesVisited.toSet()).forEach { toCity ->
                queue.add(Pair(citiesVisited+toCity, milesWalked + fromCity.distanceTo(toCity)))
            }
        }
        return -1
    }

    private fun longestRecursive(fromCity:String = "", citiesToVisit: Set<String> = citySet, distanceWalked: Int = 0): Int {
        if (citiesToVisit.isEmpty())
            return distanceWalked

        var max = Int.MIN_VALUE
        citiesToVisit.forEach {toCity ->
            val tmp = longestRecursive(toCity, citiesToVisit-toCity, distanceWalked+fromCity.distanceTo(toCity))
            if (tmp > max) {
                max = tmp
            }
        }
        return max
    }

    private fun String.distanceTo(toCity: String) =
        if (this.isEmpty() || toCity.isEmpty() || this == toCity) {
            0
        } else if (distanceMap.contains(Pair(this, toCity))) {
            distanceMap[Pair(this, toCity)]!!
        } else {
            distanceMap[Pair(toCity, this)]!!
        }

    private fun String.toCityToCity(): Triple<String, String, Int> {
        return Triple(
            this.substringBefore(" to"),
            this.substringBetween("to ", " ="),
            this.substringAfter("= ").toInt())
    }

}



