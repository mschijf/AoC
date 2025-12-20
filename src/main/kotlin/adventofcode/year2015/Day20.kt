package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day20(test=false).showResult()
}

/**
 * Net als met priemgetallen bepalen, kan je hier beter een zeef constructie gebruiken, ipv voor een huisnummer uitrekenen
 * hoeveel pakjes het huis krijgt. Het laatste is wel mooier qua code (want in één a twee regels te vangen), maar wel veel langzamer
 *
 */

class Day20(test: Boolean) : PuzzleSolverAbstract(test) {
    private val puzzleInput = if (test) 150 else 34_000_000

    override fun resultPartOne(): Any {
        return deliverPresents(10).indexOfFirst { it >= puzzleInput }
    }

    override fun resultPartTwo(): Any {
        return deliverPresents( 11, 50).indexOfFirst { it >= puzzleInput }
    }

    private fun deliverPresents(presentsPerHouse: Int, maxHouses: Int = -1): IntArray {
        val houseList = IntArray(houseArraySize)
        for (elfNr in 1..< houseList.size) {
            houseList.deliverPresentsBy(elfNr, presentsPerHouse, maxHouses)
        }
        return houseList
    }

    private fun IntArray.deliverPresentsBy(elfNumber: Int, presentsPerHouse: Int, maxHouses: Int) {
        var count = maxHouses
        for (houseNr in elfNumber..< this.size step elfNumber) {
            this[houseNr] += elfNumber * presentsPerHouse
            if (count-- == 0)
                return
        }
    }

    companion object {
        const val houseArraySize: Int = 1_000_000
    }

    // als je het aantal pakjes per huis wilt berekenen zonder zeef-constructie
    //        return generateSequence(1){it+1}.first { it.houseToPresent() >= puzzleInput }
//    private fun Int.houseToPresent(): Int {
//        return (1..this).filter{this % it == 0}.sum()*10
//    }

}


