package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day14(test=true).showResult()
}

class Day14(test: Boolean) : PuzzleSolverAbstract(test) {


    override fun resultPartOne(): Any {
        val puzzleInput = 74501
        val scoreList = mutableListOf(3, 7)
        var elf1 = 0
        var elf2 = 1

        while (scoreList.size < puzzleInput + 10) {
            val newRecipe = scoreList[elf1] + scoreList[elf2]
            if (newRecipe < 10) {
                scoreList.add(newRecipe)
            } else {
                scoreList.add(newRecipe / 10)
                scoreList.add(newRecipe % 10)
            }
            elf1 = (elf1 + 1 + scoreList[elf1]) % scoreList.size
            elf2 = (elf2 + 1 + scoreList[elf2]) % scoreList.size
        }
        return scoreList.takeLast(10).joinToString("")
    }



    override fun resultPartTwo(): Any {
//        TGinsberg solution:
//        val stopList = "074501".map { it.toString().toInt() }.toList()
//        return recipes { it.endsWith(stopList) }.size - stopList.size


        val puzzleInput = "074501"
        val maxArraySize = 500_000_000
        val scoreList = IntArray(maxArraySize)
        scoreList[0] = 3
        scoreList[1] = 7
        var recipeCount = 2
        var elf1 = 0
        var elf2 = 1
        while (recipeCount < maxArraySize) {
            val newRecipe = scoreList[elf1] + scoreList[elf2]
            if (newRecipe < 10) {
                scoreList[recipeCount++] = newRecipe
                if (scoreList.endsWith(recipeCount, puzzleInput))
                    return recipeCount - puzzleInput.length
            } else {
                scoreList[recipeCount++] = newRecipe / 10
                if (scoreList.endsWith(recipeCount, puzzleInput))
                    return recipeCount - puzzleInput.length
                scoreList[recipeCount++] = newRecipe % 10
                if (scoreList.endsWith(recipeCount, puzzleInput))
                    return recipeCount - puzzleInput.length
            }
            elf1 = (elf1 + 1 + scoreList[elf1]) % recipeCount
            elf2 = (elf2 + 1 + scoreList[elf2]) % recipeCount
        }
        return "not found within $maxArraySize recipes"
    }

    private fun IntArray.endsWith(size: Int, matchValue: String): Boolean {
        if (size < matchValue.length)
            return false

        var x = 0
        for (i in size-matchValue.length until size)
            x = 10*x + this[i]
        return x == matchValue.toInt()
    }

    private fun recipes(stopCondition: (List<Int>) -> Boolean): List<Int> {
        val history = mutableListOf(3, 7)
        var elf1 = 0
        var elf2 = 1
        var stop = false

        while (!stop) {
            val nextValue = history[elf1] + history[elf2]
            nextValue.asDigits().forEach {
                if (!stop) {
                    history.add(it)
                    stop = stopCondition(history)
                }
            }
            elf1 = (elf1 + history[elf1] + 1) % history.size
            elf2 = (elf2 + history[elf2] + 1) % history.size
        }
        return history
    }

    private fun Int.asDigits(): List<Int> =
        this.toString().map { it.toString().toInt() }

    private fun List<Int>.endsWith(other: List<Int>): Boolean =
        if (this.size < other.size) false
        else this.slice(this.size - other.size until this.size) == other
}




