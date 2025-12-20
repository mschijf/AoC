package adventofcode.year2018.december12

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val initialState = inputLines.first().substringAfter("initial state: ")
    private val notes = inputLines
        .drop(2)
        .filter { it.endsWith("#") }
        .map { it.take(5) }
        .toSet()


    override fun resultPartOne(): Any {

        var generation = initialState
        var startIndex = 0
        repeat(20) {
            generation = ".....$generation.....".windowed(5).joinToString("") { if (it in notes) "#" else "." }
            startIndex -= 3
        }
        return generation.mapIndexed { index, c -> if (c == '#') index + startIndex else 0 }.sum()
    }

    //50_000_000_000

    // There must be something repetitive if you need to trun it 50billion times.
    // So, I found out by exploring. What did I do?

    // I run it a few times on test (100 times) to see that there was a pattern in the sum:
    //          after 87 generations, it added up 20 more for every next generation
    // Running it with real puzzle data (1000 times), you can see a constant adding of 65
    // That I used for the answer

//    override fun resultPartTwo(): Any {
//
//        var generation = initialState
//        var startIndex = 0
//        if (test) {
//            repeat(100) {
//                generation = "...$generation...".windowed(5).joinToString("") { if (it in notes) "#" else "." }
//                startIndex -= 1
//            }
//            val after100 = generation.mapIndexed { index, c -> if (c == '#') index + startIndex else 0 }.sum()
//            return after100 + 20L * (50_000_000_000 - 100)
//        } else {
//            repeat(1000) {
//                generation = "...$generation...".windowed(5).joinToString("") { if (it in notes) "#" else "." }
//                startIndex -= 1
//                println(generation.sumOfPots(startIndex.toLong()))
//            }
//            val after1000 = generation.mapIndexed { index, c -> if (c == '#') index + startIndex else 0 }.sum()
//            return after1000 + 65L * (50_000_000_000 - 1000)
//        }
//    }

    override fun resultPartTwo(): Any {
        return niceSolution()
    }

    // Nice algorithm: check if the new generation is the previous one, but a little shifted
    // check also if the diff between two generations remain equal. Then we have our repetition
    private fun niceSolution(): Long {
        var generation = initialState
        var startIndex = 0L
        var previousDiff = 0L
        var previousValue = generation.sumOfPots(startIndex)

        var genCount = 0
        while (true) {
            val prevGeneration = generation
            generation = ".....$prevGeneration.....".windowed(5).joinToString("") { if (it in notes) "#" else "." }
            startIndex-=3
            genCount++
            val lastValue = generation.sumOfPots(startIndex)
            if (!generation.contains(prevGeneration) || previousDiff != lastValue - previousValue) {
                previousDiff = lastValue - previousValue
                previousValue = lastValue
            } else {
                return lastValue + previousDiff * (50_000_000_000 - genCount)
            }
        }
    }

    private fun String.sumOfPots(startIndex: Long) = this.mapIndexed { index, c -> if (c == '#') index + startIndex else 0L }.sum()

}


