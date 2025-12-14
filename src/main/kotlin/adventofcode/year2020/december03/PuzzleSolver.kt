package adventofcode.year2020.december03

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=true).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val forest = Grid(inputLines.map {it.toList()})

    override fun resultPartOne(): String {
        return countTrees(Pos(3,1)).toString()
    }

    override fun resultPartTwo(): String {
        var result = 1L
        val slopeList = listOf(Pos(1,1), Pos(3,1), Pos(5,1), Pos(7,1), Pos(1,2))
        for (slope in slopeList) {
            result *= countTrees(slope)
        }
        return result.toString()
    }


    private fun countTrees(slope: Pos): Int {
        var pos = Pos(0,0)
        var countTree = 0
        while (forest.containsPos(pos)) {
            if (forest[pos] == '#')
                countTree++
            pos = (pos + slope).modX(forest.maxX)
        }
        println("$slope $countTree")
        return countTree
    }
}

class Grid<T> (
    private val input: List<List<T>>){

    val maxY = input.size
    val maxX = input[0].size

    fun containsPos(pos:Pos) = pos.x in 0 until maxX && pos.y in 0 until maxY

    operator fun get(pos: Pos) = input[pos.y][pos.x]
}

data class Pos(val x: Int, val y: Int) {
    operator fun plus(pos: Pos) = Pos(x+pos.x, y+pos.y)
    operator fun rem(denominator: Pos) = Pos(x % denominator.x,y % denominator.y)
    fun modX(denominatorX: Int) = Pos(x % denominatorX, y)
    fun modY(denominatorY: Int) = Pos(x, y % denominatorY)
}


