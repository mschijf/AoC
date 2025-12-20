package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import tool.collectionspecials.CircularLinkedList

fun main() {
    Day1717(test=false).showResult()
}

/**
 * We kunnen het met de circular list doen, maar dat gaat in deel 2 te lang duren en misschien ook nog te weinig geheugen
 * Je kan, bij de tweede, ook bijhouden wanneer er iets op positie 1 wordt toegevoegd
 */
class Day1717(test: Boolean) : PuzzleSolverAbstract(test) {

    private val stepSize = if (test) 3 else 369

    override fun resultPartOne(): Any {
        val circularList = CircularLinkedList<Int>()
        circularList.add(0)
        var pointer = circularList.firstIndex() //start at first
        (1..2017).forEach {
            pointer = pointer.next(stepSize) //walk stepsize steps
            circularList.add(pointer.next(1), it)  // add after the current pointer (= before the next pointer)
            pointer = pointer.next(1) // set pointer to the added item.
        }
        return circularList[pointer.next(1)]
    }

    override fun resultPartTwo(): Any {
        var current = 0
        var after0 = -1
        (1..50_000_000).forEach { newNumber ->
            current = 1 + ((current+stepSize) % newNumber)
            if (current == 1) {
                after0 = newNumber
            }
        }
        return after0
    }

}


