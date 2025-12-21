package adventofcode.year2020

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day03(test=true).showResult()
}

class Day03(test: Boolean) : PuzzleSolverAbstract(test) {

    private val forest = Grid(inputLines.map {it.toList()})

    override fun resultPartOne(): String {
        return countTrees(_root_ide_package_.adventofcode.year2020.GridPos(3, 1)).toString()
    }

    override fun resultPartTwo(): String {
        var result = 1L
        val slopeList = listOf(
            GridPos(1, 1),
            GridPos(3, 1),
            GridPos(5, 1),
            GridPos(7, 1),
            GridPos(1, 2)
        )
        for (slope in slopeList) {
            result *= countTrees(slope)
        }
        return result.toString()
    }


    private fun countTrees(slope: GridPos): Int {
        var pos = GridPos(0, 0)
        var countTree = 0
        while (forest.containsGridPos(pos)) {
            if (forest[pos] == '#')
                countTree++
            pos = (pos + slope).modX(forest.maxX)
        }
//        println("$slope $countTree")
        return countTree
    }
}

class Grid<T> (
    private val input: List<List<T>>){

    val maxY = input.size
    val maxX = input[0].size

    fun containsGridPos(pos:GridPos) = pos.x in 0 until maxX && pos.y in 0 until maxY

    operator fun get(pos: GridPos) = input[pos.y][pos.x]
}

data class GridPos(val x: Int, val y: Int) {
    operator fun plus(pos: GridPos) = GridPos(x + pos.x, y + pos.y)
    operator fun rem(denominator: GridPos) = GridPos(x % denominator.x,y % denominator.y)
    fun modX(denominatorX: Int) = GridPos(x % denominatorX, y)
    fun modY(denominatorY: Int) = GridPos(x, y % denominatorY)
}


