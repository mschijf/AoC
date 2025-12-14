package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import kotlin.math.min

fun main() {
    Day1514(test=false).showResult()
}

class Day1514(test: Boolean) : PuzzleSolverAbstract(test) {

    private val reindeerList = inputLines.map{Reindeer.of(it)}
    private val flyForSeconds = if (test) 1000 else 2503

    override fun resultPartOne(): Any {
        return reindeerList.maxOf { it.distanceFlownAfterSeconds(flyForSeconds) }
    }

    override fun resultPartTwo(): Any {
        val scoreList = mutableMapOf<Reindeer, Int>()
        (1..flyForSeconds).forEach{secondsPassed ->
            val leadingReindeers = reindeerList.groupBy { it.distanceFlownAfterSeconds(secondsPassed) }.maxBy { it.key }.value
            leadingReindeers.forEach {reindeer ->
                scoreList[reindeer] = scoreList.getOrDefault(reindeer, 0) + 1
            }
        }
        return scoreList.values.max()
    }
}

data class Reindeer(val speed: Int, val flyTime: Int, val restTime: Int) {

    fun distanceFlownAfterSeconds(seconds: Int) =
        (seconds / (flyTime+restTime)) * flyTime * speed + min(seconds % (flyTime+restTime), flyTime) * speed

    companion object {
        //Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
        fun of(rawInput: String) =
            rawInput.split("\\s".toRegex()).run{
                Reindeer(
                    speed =  this[3].toInt(),
                    flyTime = this[6].toInt(),
                    restTime = this[13].toInt()
                )
            }
    }
}


