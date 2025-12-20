package adventofcode.year2021

import java.lang.Long.max

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day18(test=false).showResult()
}

class Day18(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        return inputLines
            .map {SnailFishNumber.fromString(it)}
            .reduce { acc, snailFishNumber -> acc.plus(snailFishNumber) }
            .magnitude()
            .toString()
    }

    override fun resultPartTwo(): String {
        val snailFishNumberList = inputLines
        var maxMagnitude = -1L
        for (snailNumber1 in snailFishNumberList) {
            for (snailNumber2 in snailFishNumberList) {
                if (snailNumber1 != snailNumber2) {
                    val item1 = SnailFishNumber.fromString(snailNumber1)
                    val item2 = SnailFishNumber.fromString(snailNumber2)
                    val sum = item1.plus(item2)
                    maxMagnitude = max(maxMagnitude, sum.magnitude())
                }
            }
        }
        return maxMagnitude.toString()
    }
}



fun CharSequence.splitLeveled(levelUp: Char, levelDown: Char, delimiter: Char): List<String> {
    val result = mutableListOf <String>()
    var countLevel = 0
    var startNewSplitBlock = 0
    this.forEachIndexed{ index, ch ->
        if (ch == levelUp) {
            countLevel++
        } else if (ch == levelDown) {
            countLevel--
        } else if (ch == delimiter && countLevel == 0) {
            result.add(this.substring(startNewSplitBlock, index))
            startNewSplitBlock = index+1
        }
    }
    if (startNewSplitBlock < this.length)
        result.add(this.substring(startNewSplitBlock))
    return result
}


//----------------------------------------------------------------------------------------------------------------------

abstract class SnailFishNumber{

    companion object {
        fun fromString(inputString: String): SnailFishNumber {
            return if (inputString.startsWith('[')) {
                val r = inputString.substring(1, inputString.length - 1).splitLeveled('[', ']', ',')
                SnailFishNumberPair(fromString(r[0]), fromString(r[1]))
            } else {
                SnailFishNumberRegular(inputString.toInt())
            }
        }
    }

    fun plus(other: SnailFishNumber): SnailFishNumber {
        val result = SnailFishNumberPair(this, other) as SnailFishNumber
        do {
            val hasReduced = result.reduce() 
        } while (hasReduced)
        return result
    }

    abstract fun findToBeExplodedPair(nested: Int): SnailFishNumberPair?
    abstract fun findToBeSplittedNumber(): SnailFishNumberRegular?

    abstract fun getParent(ofNode: SnailFishNumber): SnailFishNumberPair?

    private fun reduce(): Boolean {
        val nodeToExplode = findToBeExplodedPair(0)
        if (nodeToExplode != null) {
            doExplode(nodeToExplode)
            return true
        }

        val nodeToSplit = findToBeSplittedNumber()
        if (nodeToSplit != null) {
            doSplit(nodeToSplit)
            return true
        }

        return false
    }

    private fun doSplit(nodeToSplit: SnailFishNumberRegular) {
        val newPair = SnailFishNumberPair(
            SnailFishNumberRegular(nodeToSplit.value / 2),
            SnailFishNumberRegular(nodeToSplit.value - nodeToSplit.value / 2)
        )
        val parent = getParent(nodeToSplit)!!
        if (parent.left == nodeToSplit) {
            parent.left = newPair
        } else {
            parent.right = newPair
        }
    }

    private fun doExplode(nodeToExplode: SnailFishNumberPair) {
        val listOfNodes = this.toList()
        val nodeIndex = listOfNodes.indexOfFirst { it == nodeToExplode.left }
        if (nodeIndex - 1 >= 0) {
            listOfNodes[nodeIndex - 1].value =
                listOfNodes[nodeIndex - 1].value + (nodeToExplode.left as SnailFishNumberRegular).value
        }
        if (nodeIndex + 2 < listOfNodes.size) {
            listOfNodes[nodeIndex + 2].value =
                listOfNodes[nodeIndex + 2].value + (nodeToExplode.right as SnailFishNumberRegular).value
        }
        val parent = getParent(nodeToExplode)!!
        if (parent.left == nodeToExplode) {
            parent.left = SnailFishNumberRegular(0)
        } else {
            parent.right = SnailFishNumberRegular(0)
        }
    }

    abstract fun magnitude(): Long

    abstract fun toList(): List<SnailFishNumberRegular>
}

class SnailFishNumberRegular(
    var value: Int): SnailFishNumber() {

    override fun findToBeExplodedPair(nested: Int) = null

    override fun findToBeSplittedNumber() = if (value >= 10) this else null

    override fun getParent(ofNode: SnailFishNumber): SnailFishNumberPair? = null

    override fun magnitude() = value.toLong()

    override fun toString() = "$value"
    override fun toList() = listOf(this)
}

class SnailFishNumberPair(
    var left: SnailFishNumber,
    var right: SnailFishNumber): SnailFishNumber() {

    override fun findToBeSplittedNumber() = left.findToBeSplittedNumber() ?: right.findToBeSplittedNumber()

    override fun findToBeExplodedPair(nested: Int) =
        if (nested >= 4) this else left.findToBeExplodedPair(nested + 1) ?: right.findToBeExplodedPair(nested + 1)

    override fun getParent(ofNode: SnailFishNumber) =
        if (left == ofNode || right == ofNode) this else left.getParent(ofNode) ?: right.getParent(ofNode)

    override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()

    override fun toString() = "[$left,$right]"
    override fun toList() = left.toList() + right.toList()
}


