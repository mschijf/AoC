package adventofcode.year2020.december23

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day23(test=false).showResult()
}

class Day23(test: Boolean) : PuzzleSolverAbstract(test) {


    override fun resultPartOne(): String {
        val cupList =  CupLinkedList(inputLines.first())

//        println(cupList)
        repeat(100) { moveNr ->
            cupList.move()
//            println("${moveNr+1} : $cupList")
        }
        return cupList.labelsAfterOne()
    }

    override fun resultPartTwo(): String {
        val cupList =  CupLinkedList(inputLines.first(), extraMillion = true)
        repeat(10_000_000) {
            cupList.move()
        }
        return cupList.twoCupsProductAfterCupOne().toString()
    }

}



