package adventofcode.year2018.day19

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day19(test=false).showResult()
}

class Day19(test: Boolean) : PuzzleSolverAbstract(test) {

    private val ipRegister = inputLines.first().split(" ").last().toInt()
    private val operationList = inputLines.drop(1).map{ Operation.from(it) }

    override fun resultPartOne(): Any {
        val dev = RegisterDevice(ipRegister, mutableListOf(0,0,0,0,0,0))
        dev.executeOperationList(operationList)

        return dev
    }

    // this needs a lot of debugging. Finding out what the program does.
    // it is actually sufficient to rewrite the instruaction set to find out what it does.
    // you can see it works towards a huge number, 10551373
    // according to https://todd.ginsberg.com/post/advent-of-code/2018/day19/ he says:
    // "   you’ll discover that early on one of the registers gets set with a huge number.
    //     The rest of the program loops around, calculating the sum of even divisors of this huge number."
    //
    override fun resultPartTwo(): Any {
//        val dev = RegisterDevice(ipRegister, mutableListOf(1,0,0,0,0,0))
//        dev.executeOperationList(operationList, debug = true, maxInstructions = 1000)

        val dev2 = RegisterDevice(ipRegister, mutableListOf(10551373, 10551373, 2, 10551373, 0, 3))
        dev2.executeOperationList(operationList, debug = false, maxInstructions = 1000, ipStart=3)

//        println( "not solved...but answer: 10551373.factors().sum() = ${10551373.sumOfProperDivisors() + 10551373}")
        return "not solved...but answer: 10551373.factors().sum() = ${10551373.factors().sum()}"
    }

    private fun Int.factors(): List<Int> =
        (1..this).mapNotNull { n ->
            if(this % n == 0) n else null
        }
}


