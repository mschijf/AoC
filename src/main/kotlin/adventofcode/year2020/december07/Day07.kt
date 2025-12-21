package adventofcode.year2020.december07

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day07(test=false).showResult()
}

class Day07(test: Boolean) : PuzzleSolverAbstract(test) {


    override fun resultPartOne(): String {
        val bagList = inputLines.map { Bag(it) }
        bagList.forEach { it.setBagStructure(bagList) }

        return bagList.count{it.containsBag("shiny gold")}.toString()
    }

    override fun resultPartTwo(): String {
        val bagList = inputLines.map { Bag(it) }
        bagList.forEach { it.setBagStructure(bagList) }

        return bagList.first{it.name =="shiny gold"}.countBags().toString()
    }

}

class Bag (inputLine: String) {
    val name: String
    private val bagNameList = mutableListOf<String>()
    private val bagCountList = mutableListOf<Long>()
    private val bagList = mutableListOf<Bag>()

    init {
        val normalizedInp = inputLine.replace("bags", "bag")
        name = normalizedInp.substringBefore(" bag contain").trim()
        if (!normalizedInp.contains("contain no other bag")) {
            var tmp = normalizedInp.substringAfter("bag contain ", "")
            while (tmp.contains(", ")) {
                val tmpBag = tmp.substringBefore(", ")
                bagCountList.add(tmpBag.substringBefore(" ", "0").toLong())
                bagNameList.add(tmpBag.substringAfter(" ").substringBefore("bag").trim())
                tmp = tmp.substringAfter(", ")
            }
            bagCountList.add(tmp.substringBefore(" ", "0").toLong())
            bagNameList.add(tmp.substringAfter(" ").substringBefore("bag").trim())
        }
    }

    fun setBagStructure(totalBagList: List<Bag>) {
        bagNameList.forEach{localBagName ->
            bagList.add(totalBagList.first {it.name == localBagName})
        }
    }

    fun containsBag(name: String): Boolean {
        if (bagNameList.contains(name))
            return true
        return bagList.any {it.containsBag(name)}
    }

    fun countBags(): Long {
        return bagCountList.sum() + bagList.mapIndexed {index, bag -> bagCountList[index] * bag.countBags()}.sum()
    }

}