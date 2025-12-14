package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day1519(test=false).showResult()
}

class Day1519(test: Boolean) : PuzzleSolverAbstract(test) {
    private val replacementList = inputLines.dropLast(2).map { it.split(" => ") }.map { it[0] to it[1].toAtomList() }
    private val medicineMolecule = inputLines.last().toAtomList()

    override fun resultPartOne(): Any {
        return replacementList
            .flatMap{replacement ->
                medicineMolecule.replaceBy(replacement).map{it.joinToString("")}
            }.distinct().size
    }

    override fun resultPartTwo(): Any {
        return shrinkRecursive(medicineMolecule)
    }


    private fun List<String>.replaceBy(replacement: Pair<String, List<String>>) =
        this.withIndex()
            .filter { (_, atom) -> atom == replacement.first}
            .map { (index, _) -> this.subList(0, index) + replacement.second + this.subList(index + 1, this.size) }

    private fun List<String>.indexOf(atom: List<String>, startIndex: Int = 0) =
        (startIndex..size-atom.size).indexOfFirst { i -> this.subList(i, this.size).startsWith(atom) }

    private fun List<String>.startsWith(atom: List<String>) =
        if (atom.size > this.size) false else atom.indices.all{index -> this[index] == atom[index]}

    /**
     * Very greedy algorithm
     * the replacement list is ordered descending by the replacement part. In the 'shrink', we therefore choose the biggest shrink first
     * 'the assumption' is that by doing this, teh first solution found is the best
     *
     * note: I run a full bruteforce (DFS), for a while. It found three times a 212 steps solution, and nothing else after running for an hour
     *       (but it didn't end as well, so ... still an assumption (?)
     *
     *       Read: https://www.happycoders.eu/algorithms/advent-of-code-2015/ for a very nice solution (needs analysis of the input)
     */
    private fun shrinkRecursive(currentMolecule: List<String>): Int {

        if (currentMolecule == listOf("e")) {
            return 0
        }

        replacementList.sortedByDescending { it.second.size }.forEach { (fromAtom, toAtoms) ->
            var index = currentMolecule.indexOf(toAtoms)
            while (index != -1) {
                val shrunkMolecule = currentMolecule.reverse(index, toAtoms.size, fromAtom)
                val tmp = 1 + shrinkRecursive(shrunkMolecule)
                if (tmp < Int.MAX_VALUE) //the greedy part
                    return tmp
                index = currentMolecule.indexOf(toAtoms, index + toAtoms.size)
            }
        }
        return Int.MAX_VALUE
    }

    private fun List<String>.reverse(fromIndex: Int, length: Int, replaceBy: String) : List<String> {
        return this.subList(0, fromIndex) + replaceBy + this.subList(fromIndex+length, this.size)
    }

    private fun String.toAtomList() =
        this.windowed(size=2, partialWindows = true)
            .filter{it[0].isUpperCase()}
            .map {if (it.length == 1 || it[1].isLowerCase()) it else it[0].toString()}


}


