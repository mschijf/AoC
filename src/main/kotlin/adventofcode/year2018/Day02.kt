package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract
import java.lang.StringBuilder

fun main() {
    Day02(test=false).showResult()
}

class Day02(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val tmp = inputLines.map{line -> line.toList().groupingBy{ch -> ch}.eachCount()}
        val count2 = tmp.map{it.values.any { cnt -> cnt == 2 }}.count{it}
        val count3 = tmp.map{it.values.any { cnt -> cnt == 3 }}.count{it}
        return  count2 * count3
    }

    override fun resultPartTwo(): Any {
        val list = inputLines
        for (i in 0 until list.count()-1) {
            for (j in i+1 until list.count()) {
                if (list[i].differsOne(list[j])) {
                    return list[i].equalCharSerie(list[j])
                }
            }
        }
        return "NOOP"
    }

    private fun String.differsOne(other: String) : Boolean {
        var count = 0
        this.forEachIndexed { index, c ->
            if (c != other[index])
                count++
        }
        return count == 1
    }

    private fun String.equalCharSerie(other: String): String {
        var sb = StringBuilder()
        this.forEachIndexed { index, c ->
            if (c == other[index])
                sb.append(c)
        }
        return sb.toString()
    }

}


