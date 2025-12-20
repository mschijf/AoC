package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day24(test=true).showResult()
}

class Day24(test: Boolean) : PuzzleSolverAbstract(test) {
    private val packageList = inputLines.map{ it.toLong() }.sortedDescending()

    override fun resultPartOne(): Any {
        return findSmallestGroups(packageList, packageList.sum()/3)
            .minOf{ group ->
                group.reduceRight { i, acc -> i*acc }
            }
    }

    override fun resultPartTwo(): Any {
        return findSmallestGroups(packageList, packageList.sum()/4)
            .minOf{ group ->
                group.reduceRight { i, acc -> i*acc }
            }
    }

    private fun findSmallestGroups(packages: List<Long>, groupWeight: Long, group:List<Long> = emptyList(), smallestGroup: Int = packageList.size+1 ): List<List<Long>> {
        return if (group.size > smallestGroup) {
            emptyList()
        } else if (group.sum() == groupWeight) {
            if (packageList.removeGroup(group).canBeBalanced(groupWeight)) listOf(group) else emptyList()
        } else if(group.sum() > groupWeight) {
            emptyList()
        } else {
            val result = mutableListOf<List<Long>>()
            var localSmallest = smallestGroup
            packages.forEachIndexed {  index, pack ->
                val tmp = findSmallestGroups(packages.drop(index + 1), groupWeight, group + pack, localSmallest)
                val sizeFound = tmp.firstOrNull()?.size ?: (packageList.size + 1)
                if (sizeFound < localSmallest) {
                    result.clear()
                    result.addAll(tmp)
                    localSmallest = sizeFound
                } else if (sizeFound == localSmallest) {
                    result.addAll(tmp)
                }
            }
            result
        }
    }

    // we can't use list.minus(otherList), cause if we have a list with non-unique items, all duplicates will be removed
    // when we use 'minus'. This is not whta we want
    private fun List<Long>.removeGroup(from: List<Long>): List<Long> {
        val result = this.toMutableList()
        from.forEach {
            result.remove(it)
        }
        return result
    }


    // het antwoord dat er uit komt als je voor deze functie alleen maar 'return true' doet is hetzelfde.
    // is deze functie nodig? --> ja. Voorbeeld (voor 3): 8, 11, 5.
    // todo: deze functie verbeteren voor 4 groepen. Bij opgave twee, is nu de controle niet voldoende, maar werkt wel

    private fun List<Long>.canBeBalanced(groupWeight: Long,  sum: Long = 0): Boolean {
        if (sum == groupWeight) {
            return true
        } else if(sum > groupWeight) {
            return false
        } else {
            this.forEachIndexed { index, pack ->
                val found = this.drop(index+1).canBeBalanced(groupWeight, sum+pack)
                if (found)
                    return true
            }
            return false
        }
    }
}


