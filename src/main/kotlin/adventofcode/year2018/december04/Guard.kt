package adventofcode.year2018.december04

class Guard (
    val id: Int) {
    private val sleepPeriodList = mutableListOf<MyPeriod>()

    fun addSleepingPeriod(start:MyTime, end:MyTime) {
        sleepPeriodList.add(MyPeriod(start, end))
    }

    fun overallSleepingTime() = sleepPeriodList.sumOf{it.minutesPassed()}

    fun mostSleptMinute(): Int {
        val minuteArray = IntArray(60) {0}
        sleepPeriodList.forEach {sleepPeriod ->
            sleepPeriod.minuteRange().forEach { minute -> minuteArray[minute]++ }
        }
        return minuteArray.indices.maxBy { minuteArray[it] }
    }

    fun mostSleptMinuteCount(): Int {
        val minuteArray = IntArray(60) {0}
        sleepPeriodList.forEach {sleepPeriod ->
            sleepPeriod.minuteRange().forEach { minute -> minuteArray[minute]++ }
        }
        return minuteArray.max()
    }

    override fun toString(): String {
        return "\n$id\n${sleepPeriodList.joinToString("\n")}"
    }
}

data class MyTime(val year: Int, val month: Int, val day: Int, val hour: Int, val minute: Int) {
}

data class MyPeriod(val start: MyTime, val end: MyTime) {
    fun minuteRange() = start.minute until end.minute
    fun minutesPassed() = end.minute - start.minute
}