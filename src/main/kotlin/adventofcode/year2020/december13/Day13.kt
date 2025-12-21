package adventofcode.year2020.december13

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day13(test=false).showResult()
}

class Day13(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val time = inputLines.first().toInt()
        val busIds = inputLines.last()
            .split(",")
            .filter {it != "x"}
            .map{it.toInt()}

        val beste =  busIds
            .map { busTime -> Pair(busTime, (time / busTime + 1) * busTime - time) }
            .minBy { it.second }

        return (beste.first * beste.second).toString()
    }

    override fun resultPartTwo(): String {

        val busIds = inputLines.last()
            .split(",")
            .withIndex()
            .filter {it.value != "x"}
            .map{Pair(it.value.toLong(), it.index)}

        return solve(busIds).toString()
    }

    //
    // idea: we start finding the first occurence of the second bus matching the first bus.
    // for instance, we have buses 3 and 5 and 11, then we look for a moment where bus 5 (+1) arrives at same time as bus 3
    // this is at time 6 (2*3, 1*5)
    // now we continue iterating over 3*5 until we found a timestamp that matches 3, 5+1 and 11+2
    // we iterate over the product of the first two, because we know that we only will find a next time stamp, that still
    // will meet the first criterium (after 3*5 it will be again in the same situation) and also meets the second.
    // and this we will continue for all buses
    //

    private fun solve(list: List<Pair<Long, Int>>): Long {
        var time = 0L
        var iterationStep = 1L
        for (nextIndex in 1 until list.size) {
            iterationStep *= list[nextIndex - 1].first
            while ((time + list[nextIndex].second) % list[nextIndex].first != 0L) {
                time += iterationStep
            }
        }
        return time
    }

}


