package adventofcode.year2020.december11

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day11(test=false).showResult()
}

class Day11(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val seatGrid = SeatGrid(inputLines.map { it.toList() }, 4, true)
        do {
            val noChange = seatGrid.doRound()
        } while (!noChange)
        return seatGrid.occupiedSeats().toString()
    }

    override fun resultPartTwo(): String {
        val seatGrid = SeatGrid(inputLines.map { it.toList() }, 5, false)
        do {
            val noChange = seatGrid.doRound()
        } while (!noChange)
        return seatGrid.occupiedSeats().toString()
    }
}

class SeatGrid(
    private var grid: List<List<Char>>,
    private val leaveAtNeighbourCount: Int,
    private val lookOneStep: Boolean) {

    fun doRound(): Boolean {
        val newGrid = grid.mapIndexed{ row, wholeRow -> wholeRow.mapIndexed {col, seat -> newOccupation(row, col, seat)}}
        val noChange = (grid == newGrid)
        grid = newGrid
        return noChange
    }

    private fun newOccupation(row: Int, col: Int, seatValue: Char): Char {
        if (seatValue == '.')
            return '.'

        if (seatValue == 'L') {
            return if (countOccupiedNeighbours(row, col) == 0) '#' else 'L'
        } else {
            return if (countOccupiedNeighbours(row, col) >= leaveAtNeighbourCount) 'L' else '#'
        }
    }

    private fun isOccupied(row: Int, col: Int, dir: Pair<Int, Int>): Boolean {
        var cRow = row + dir.first
        var cCol = col + dir.second
        while (cRow in grid.indices && cCol in grid[0].indices) {
            if (grid[cRow][cCol] != '.')
                return grid[cRow][cCol] == '#'
            if (lookOneStep)
                break
            cRow += dir.first
            cCol += dir.second
        }
        return false
    }

    private fun countOccupiedNeighbours(row: Int, col: Int): Int {
        val dirList = listOf(Pair(-1,-1), Pair(-1,0), Pair(-1,1), Pair(0,-1), Pair(0,1), Pair(1,-1), Pair(1,0), Pair(1,1))
        return dirList.count { isOccupied(row, col, it) }
    }
//
//    fun print() {
//        for (row in grid.indices) {
//            for (col in grid[row].indices) {
//                print(grid[row][col])
//            }
//            println()
//        }
//    }

    fun occupiedSeats() = grid.sumOf { row -> row.count { cell -> cell == '#' } }
}


