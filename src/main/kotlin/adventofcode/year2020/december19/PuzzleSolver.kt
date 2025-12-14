package adventofcode.year2020.december19

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.hasOnlyDigits
import tool.mylambdas.splitByCondition

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val inputBlocks = inputLines.splitByCondition { it.isEmpty() }
        val regEx = RegEx(inputBlocks[0], false)
        val matchLines = inputBlocks[1]

        return matchLines.count{regEx.matches(it)}.toString()
    }

    override fun resultPartTwo(): String {
        val inputBlocks = inputLines.splitByCondition { it.isEmpty() }
        val regEx = RegEx(inputBlocks[0], true)
        val matchLines = inputBlocks[1]

        return matchLines.count{regEx.matches(it)}.toString()
    }
}

class RegEx(inputLines: List<String>, isPartTwo: Boolean) {

    private val regExLineList = inputLines
        .associate { it.substringBefore(": ").toInt() to it.substringAfter(": ") }
        .toMutableMap()

    init {
        // to replace rule 11, with 11: 42 31 | 42 11 31
        // parts 42 and 31 have to be repeated both the same time (> 0 times), for example
        //       42 42 42 31 31 31   or     42 31   but not    42 42 42 31 31
        // therefore, I made a regex like "42{1} 31{1} | 42{2} 31{2} | ..." etc.
        // I tried it with several counts, but after 3, the matching results did not change.
        // not a nice solution, but sufficient for this puzzle

        // unfortunately, recursive regex matching doesn't work in kotlin regex function, thereforem, we cannot use
        // below described technique:
        // The regexes a(?R)?z, a(?0)?z, and a\g<0>?z all match one or more letters a followed by exactly
        // the same number of letters z. Since these regexes are functionally identical,
        // we’ll use the syntax with R for recursion to see how this regex matches the string aaazzz.
        if (isPartTwo) {
            regExLineList[8] = "42 +"           //8: 42 | 42 8
            regExLineList[11] = List(4){"42 {${it+1}} 31 {${it+1}}"}.joinToString(" | ") //11: 42 31 | 42 11 31

        }
    }

    private val regexStr = makeRegex(regExLineList[0]!!)
        .replace(" ", "")
        .replace("\"","")
    private val regEx = ("^$regexStr$").toRegex()

    private fun makeRegex(start: String): String {
        val splitted = start.split(" ")
        if (splitted.none {it.hasOnlyDigits()})
            return start

        val result = splitted
                    .map { if(it.hasOnlyDigits()) "( ${regExLineList[it.toInt()]!!} )" else it}
                    .joinToString(" ")
        return makeRegex(result)
    }

    fun matches(input: String): Boolean {
        return regEx.matches(input)
    }

}


