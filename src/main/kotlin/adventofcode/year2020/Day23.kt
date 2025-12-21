package adventofcode.year2020

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

//---------------------------------------------------------------------------------------------------------------------

class CupList(input: String) {
    private val cups = input.toList().map {it.digitToInt()}.toMutableList()
    private var currentIndex = 0

    fun addUpToOneMillion() {
        val start = cups.size
        for (v in start .. 1_000_000) {
            cups.add(v)
        }
    }

    fun move() {
        val current = cups[currentIndex]
        val pickUp = listOf(cups[(currentIndex+1) % cups.size], cups[(currentIndex+2) % cups.size], cups[(currentIndex+3) % cups.size])
        val destinationIndex = findDestinationIndex(current, pickUp)
        val destination = cups[destinationIndex]

        cups.remove(pickUp[0])
        cups.remove(pickUp[1])
        cups.remove(pickUp[2])

        val afterIndex = cups.indexOf(destination)
        cups.add(afterIndex+1, pickUp[2])
        cups.add(afterIndex+1, pickUp[1])
        cups.add(afterIndex+1, pickUp[0])

        currentIndex = (cups.indexOf(current)+1) % cups.size
    }

    private fun findDestinationIndex(current: Int, pickUp: List<Int>): Int {
        val lowestValue = cups.min()
        val highestValue = cups.max()

        var destinationValue = if (current == lowestValue) highestValue else current - 1

        var i = (currentIndex + 1) % cups.size
        while (true) {
            if (cups[i] == destinationValue) {
                if (cups[i] in pickUp) {
                    destinationValue = if (destinationValue == lowestValue) highestValue else destinationValue - 1
                } else {
                    return i
                }
            }
            i = (i+1) % cups.size
        }
    }

    fun labelsAfterOne(): String {
        var result = ""
        var index = cups.indexOf(1)
        repeat(cups.size){
            result = result + cups[index]
            index = (index + 1) % cups.size
        }
        return result.drop(1)
    }

    override fun toString() = cups.joinToString(" ") { if (it == cups[currentIndex]) "($it)" else "$it" }
}

//---------------------------------------------------------------------------------------------------------------------

class CupLinkedList(input: String, extraMillion: Boolean=false) {

    private var current: Cup
    private val cupListByNumber = List(if (extraMillion) 1_000_000+1 else input.length+1) {Cup(it)}

    init {
        current = cupListByNumber[input.first().digitToInt()]
        var loop = current
        input.drop(1).forEach {ch ->
            loop.nextCup = cupListByNumber[ch.digitToInt()]
            loop = cupListByNumber[ch.digitToInt()]
        }
        if (extraMillion) {
            val start = input.length+1
            for (v in start..1_000_000) {
                loop.nextCup = cupListByNumber[v]
                loop = cupListByNumber[v]
            }
        }
        loop.nextCup = current
    }

    inner class Cup(val number: Int){
        var nextCup: Cup? = null
    }

    fun move() {

        //determine pickup nodes
        val pickUpBegin = current.nextCup!!
        val pickUpMid = pickUpBegin.nextCup!!
        val pickUpEnd = pickUpMid.nextCup!!

        val destination = findDestination(setOf(pickUpBegin.number, pickUpMid.number, pickUpEnd.number))

        // remove cups
        current.nextCup = pickUpEnd.nextCup

        // add after destination
        pickUpEnd.nextCup = destination.nextCup
        destination.nextCup = pickUpBegin

        current = current.nextCup!!
    }

    private fun findDestination(pickupValueSet: Set<Int>): Cup {
        val lowestValue = 1
        val highestValue = cupListByNumber.size-1

        var destinationValue = if (current.number == lowestValue) highestValue else current.number - 1
        while (destinationValue in pickupValueSet) {
            destinationValue = if (destinationValue == lowestValue) highestValue else destinationValue - 1
        }

        return cupListByNumber[destinationValue]
    }

    fun labelsAfterOne(): String {
        var result = ""
        var loop = cupListByNumber[1].nextCup!!
        while (loop != cupListByNumber[1]) {
            result = "$result${loop.number}"
            loop = loop.nextCup!!
        }
        return result
    }

    fun twoCupsProductAfterCupOne(): Long {
        val firstCup = cupListByNumber[1].nextCup!!
        val secondCup = firstCup.nextCup!!
        return firstCup.number * secondCup.number.toLong()
    }

    override fun toString(): String {
        var result = "(${current.number})"
        var loop = current.nextCup!!
        while (loop != current) {
            result = "$result ${loop.number}"
            loop = loop.nextCup!!
        }
        return result
    }
}



