package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day08(test=false).showResult()
}

class Day08(test: Boolean) : PuzzleSolverAbstract(test) {

    private val numberList = inputLines.first().split(" ").map{it.toInt()}

    override fun resultPartOne(): Any {
        return numberList.getTreeInfo(part2=false).metadataSum
    }

    override fun resultPartTwo(): Any {
        return numberList.getTreeInfo(part2=true).metadataSum
    }

    private fun List<Int>.getTreeInfo(part2: Boolean): TreeInfo {
        val childCount = this[0]
        val metadataCount = this[1]
        var childTreeStartIndex = 2
        val metadataChildList = mutableListOf<Int>()
        repeat(childCount) {
            val childResult = this.subList(childTreeStartIndex, this.size).getTreeInfo(part2)
            childTreeStartIndex += childResult.treeLength
            metadataChildList.add(childResult.metadataSum)
        }
        val metadataSum = if (!part2 || childCount == 0) {
            metadataChildList.sum() + this.subList(childTreeStartIndex, childTreeStartIndex + metadataCount).sum()
        } else {
            this.subList(childTreeStartIndex, childTreeStartIndex + metadataCount)
                .filter {value -> value <= metadataChildList.size }
                .sumOf {childIndex -> metadataChildList[childIndex-1]}
        }
        return TreeInfo(childTreeStartIndex+metadataCount, metadataSum)
    }
}

data class TreeInfo(val treeLength: Int, val metadataSum: Int)


