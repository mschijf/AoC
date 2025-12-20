package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day05(test=false).showResult()
}

class Day05(test: Boolean) : PuzzleSolverAbstract(test) {


    override fun resultPartOne(): Any {
        val jumpOffsets = inputLines.map{it.toInt()}.toIntArray()
        var ip = 0
        var stepCount = 0
        while (ip in jumpOffsets.indices) {
            stepCount++
            val jumpValue = jumpOffsets[ip]
            jumpOffsets[ip]++
            ip += jumpValue
        }

        return stepCount
    }

    override fun resultPartTwo(): Any {
        val jumpOffsets = inputLines.map{it.toInt()}.toIntArray()
        var ip = 0
        var stepCount = 0
        while (ip in jumpOffsets.indices) {
            stepCount++
            val jumpValue = jumpOffsets[ip]
            jumpOffsets[ip] += if(jumpValue < 3) 1 else -1
            ip += jumpValue
        }

        return stepCount
    }
}


