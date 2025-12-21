package adventofcode.year2018.december04

import adventofcode.PuzzleSolverAbstract
import java.lang.Exception

fun main() {
    Day04(test=false).showResult()
}

class Day04(test: Boolean) : PuzzleSolverAbstract(test) {

    private val guardMap = parseInput(inputLines)

    override fun resultPartOne(): Any {
        val lazyGuard = guardMap.values.maxBy{it.overallSleepingTime()}
//        println(lazyGuard)
//        println(lazyGuard.id)
//        println(lazyGuard.overallSleepingTime())
//        println(lazyGuard.mostSleptMinute())
        return lazyGuard.id * lazyGuard.mostSleptMinute()
    }

    override fun resultPartTwo(): Any {
        val lazyGuard = guardMap.values.maxBy { it.mostSleptMinuteCount() }
//        println(lazyGuard)
//        println(lazyGuard.id)
//        println(lazyGuard.mostSleptMinute())
        return lazyGuard.id * lazyGuard.mostSleptMinute()
    }



    //[1518-11-01 00:00] Guard #10 begins shift
    //[1518-11-01 00:05] falls asleep
    //[1518-11-01 00:25] wakes up

    private fun parseInput(inputLines: List<String>): Map<Int, Guard> {
        val resultMap = mutableMapOf<Int, Guard>()

        var currentGuardId = -1
        var startTime = MyTime(0,0,0,0,0)
        inputLines.sorted().forEach {s ->
            val myTime = MyTime(
                year= s.substring(1, 5).toInt(),
                month= s.substring(6, 8).toInt(),
                day= s.substring(9, 11).toInt(),
                hour= s.substring(12, 14).toInt(),
                minute= s.substring(15, 17).toInt()
            )
            if (s.endsWith("begins shift")) {
                currentGuardId = s.substringAfter("Guard #").substringBefore(" begins shift").toInt()
            } else if (s.endsWith("falls asleep")) {
                startTime = myTime
            } else if (s.endsWith("wakes up")) {
                val guard = resultMap.getOrPut(currentGuardId){Guard(currentGuardId)}
                guard.addSleepingPeriod(startTime, myTime)
            } else {
                throw Exception("Unknown string")
            }
        }
        return resultMap
    }
}



