package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.collectioncombination.*

fun main() {
    Day04(test=false).showResult()
}

class Day04(test: Boolean) : PuzzleSolverAbstract(test) {

    private val passPhraseList = inputLines.map{it.split("\\s".toRegex())}

    override fun resultPartOne(): Any {
        return passPhraseList.count { passPhrase -> passPhrase.asCombinedItemsSequence().none{it.first == it.second} }
    }

    override fun resultPartTwo(): Any {
        return passPhraseList.count { passPhrase -> passPhrase.asCombinedItemsSequence().none{it.first.isAnagramOf(it.second)} }
    }

    private fun String.isAnagramOf(other: String) =
        this.toCharArray().sorted() == other.toCharArray().sorted()

}


