package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day1525(test=true).showResult()
}

class Day1525(test: Boolean) : PuzzleSolverAbstract(test) {

    //Enter the code at row 2947, column 3029.

    override fun resultPartOne(): Any {
        //note: cantorIndex is zero-based, so we have to substract 1 from col and row.
        return determineCode(cantorIndex(2947-1,3029-1))
    }

    /*
     * this is something I know as a 'cantor walk'.
     * You can calculate the 'cantor number' based on the row and column.
     * This is most easy if you have the row and columns zer0-based
     *
     * I leave it to the reader to find the formula yourself
     */

    private fun cantorIndex (row: Int, col: Int): Int {
        return (row * (row+1) / 2) +
                ((row+col+1) * (row+col+2) / 2) -
                ((row+1) * (row+2) / 2)
    }

    private fun determineCode(index: Int): Int {
        var code = 20151125L
        repeat(index) {
            code = (code * 252533L) % 33554393L
        }
        return code.toInt()
    }
}

/*
 *      0   1   2   3   4   5   6   7
 *   +-------------------------------
 * 0 |  0   2   5   9  14  20  17
 * 1 |  1   4   8  13  19  26
 * 2 |  3   7  12  18  25
 * 3 |  6  11  17  25
 * 4 | 10  16  23
 * 5 | 15  22
 * 6 | 21
 * 7 |
 *
 * - you can find out that the first column in a row can be calculated by r(r+1)/2
 * - you can also see that the increment between two values in a row is always the same as the previous increment
 *   the same row, one column earlier
 * - and you can see the first incremnet (between the 0th and 1st valuein a row) is the same as row+1
 *
 * this can be formlaized in the formula that you can find under the name 'cantorIndex'
 *
 *
 *
 */
