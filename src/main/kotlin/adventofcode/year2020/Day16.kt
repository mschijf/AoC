package adventofcode.year2020

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.splitByCondition

fun main() {
    Day16(test=false).showResult()
}

//15940 too low
class Day16(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val dataBlocks = inputLines.splitByCondition { it.isEmpty() }
        val dataNoteList = DataNoteList(dataBlocks[0])
        val yourTicket = Ticket(dataBlocks[1].last())
        val nearbyTicketList = dataBlocks[2].drop(1).map { Ticket(it) }

        val sum = nearbyTicketList.sumOf { it.invalidNumberList(dataNoteList).sum() }

        return sum.toString()
    }

    override fun resultPartTwo(): String {
        val dataBlocks = inputLines.splitByCondition { it.isEmpty() }
        val dataNoteList = DataNoteList(dataBlocks[0])
        val yourTicket = Ticket(dataBlocks[1].last())
        val nearbyTicketList = dataBlocks[2].drop(1).map { Ticket(it) }

        //filter valid tickets
        val validTickets = nearbyTicketList
            .filter{it.invalidNumberList(dataNoteList).isEmpty()}
            .map {it.numberList}
        //transpose them --> now each row has all the numbers corresponding to one field
        val transposed = transpose(validTickets)
        //determine the set of DataNotes (type) that corresponds to all numbers
        val typePerIndex = transposed.map { ticketNumberList -> dataNoteList.getDataNote(ticketNumberList)}

        // per index, we now have multiple valid fields. But you can filter some options, by first checking
        // options that only have one type (set with one element)
        // if we remove that from options that have bigger sets, we reduce some options
        // and keep repeating that. (and luckily it ends ;-)
        do {
            val singleSet = typePerIndex.filter { it.size == 1 }.flatten().toSet()
            for (type in typePerIndex) {
                if (type.size > 1) {
                    type.removeAll(singleSet)
                }
            }
        } while (typePerIndex.any { it.size > 1 })


        // now check your own ticket and find the corresponding field.
        // finally filter the 'departure' fields and multiply all values with each other
        val result = yourTicket.numberList
            .mapIndexed { index, number -> Pair(number, typePerIndex[index].first()) }
            .filter { it.second.startsWith("departure") }
            .map {it.first.toLong()}
            .reduce { acc, l -> acc*l }

        return result.toString()
    }

    private fun transpose(input: List<List<Int>>): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        for (col in input[0].indices) {
            val inner = mutableListOf<Int>()
            for (row in input.indices) {
                inner.add(input[row][col])
            }
            result.add(inner)
        }
        return result
    }
}

class DataNoteList(inputLines: List<String>) {
    private val list = inputLines.map { DataNote(it) }

    fun isValidNumber(aNumber: Int) = list.any{it.isValidNumber(aNumber)}

    fun getDataNote(numberList: List<Int>): MutableSet<String> {
        return list
            .map { dataNote -> Pair(dataNote, numberList.all { aNumber -> dataNote.isValidNumber(aNumber) }) }
            .filter { it.second }
            .map {it.first.name}
            .toMutableSet()
    }
}

class DataNote(inputLine: String) {
    val name = inputLine.substringBefore(": ")
    private val range1 = toRange(inputLine.substringAfter(": ").substringBefore(" or")
        .split("-").map{it.toInt()})
    private val range2 = toRange(inputLine.substringAfter(" or ")
        .split("-").map{it.toInt()})

    private fun toRange(list: List<Int>) = list[0].rangeTo(list[1])

    fun isValidNumber(aNumber: Int): Boolean {
        return aNumber in range1 || aNumber in range2
    }
}

class Ticket(inputLine: String) {
    val numberList = inputLine.split(",").map { it.toInt() }

    fun invalidNumberList(dataNotes: DataNoteList) = numberList.filter{ !dataNotes.isValidNumber(it) }
}


