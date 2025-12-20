package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day02(test=false).showResult()
}

class Day02(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val squares = inputLines.map{dim -> dim.squares()}
        return squares.sumOf{present -> present.sum() + present.min()}
    }

    override fun resultPartTwo(): Any {
        val ribbons = inputLines.map{dim -> dim.dimensions().sorted()}.map{2*(it[0] + it[1])}
        val volumes = inputLines.map{dim -> dim.volume()}
        return ribbons.sum() + volumes.sum()
    }

    private fun String.dimensions(): List<Int> {
        return this.split("x").map{it.toInt()}
    }

    private fun String.squares(): List<Int> {
        val (l,w,h) = this.dimensions()
        return listOf(l*w, l*w, w*h, w*h, l*h, l*h)
    }

    private fun String.volume(): Int {
        val (l,w,h) = this.dimensions()
        return l*w*h
    }


}


