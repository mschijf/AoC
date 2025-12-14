package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import java.lang.Exception

fun main() {
    Day1508(test=false).showResult()
}

class Day1508(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return inputLines.sumOf{it.length - it.removeSurrounding("\"").myCount()}
    }

    //
    // Tel alleen per woord de speciale symbolen, i.e. de '\' en de '"'.
    // deze woorden in de resltaatstring voorafggeaan door een escape '\' en de levert tov oorspronkeljke woord een extra letter op
    // Tel tot slot daar twee omsluitende quotes bij op '"'
    //
    override fun resultPartTwo(): Any {
        return inputLines.sumOf{word -> word.count {ch -> ch == '\"' || ch == '\\'} + 2 }
    }

    private fun String.myCount(): Int {
        var count = 0
        var i=0
        while (i < this.length) {
            count++
            if (this[i] == '\\') {
                when (this[i+1]) {
                    '\\', '"' -> i+=2
                    'x' -> i+=4
                    else -> throw Exception("slash with surprising follower")
                }
            } else {
                i++
            }
        }
        return count
    }

}


