package adventofcode.year2021

import tool.coordinate.threedimensional.Point3D
import tool.mylambdas.collectioncombination.mapCombinedItems

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.splitByCondition

fun main() {
    Day2119(test=false).showResult()
}

class Day2119(test: Boolean) : PuzzleSolverAbstract(test) {

    private val scannerList = inputLines.splitByCondition { it.isBlank() }.map{Scanner.of(it)}

    override fun resultPartOne(): String {
        return getTransformedAndTransposedScannerList()
            .flatMap {it.beaconPositions }
            .distinct()
            .size.toString()
    }

    override fun resultPartTwo(): String {
        return getTransformedAndTransposedScannerList()
            .mapCombinedItems { scanner1, scanner2 ->  scanner1.scannerPosition!!.distanceTo(scanner2.scannerPosition!!)}
            .max()
            .toString()
    }

    private var lazyResult: List<Scanner>? = null
    private fun getTransformedAndTransposedScannerList(): List<Scanner> {
        if (lazyResult != null)
            return lazyResult!!
        val scanner0 = scannerList.first()
        val todo = ArrayDeque(scannerList.drop(1))
        val transformedScannerList = mutableListOf(Scanner(scanner0.id, scanner0.beaconPositions, Point3D(0,0,0)))
        while (todo.isNotEmpty()) {
            val scanner = todo.removeFirst()
            val result = transformedScannerList.firstNotNullOfOrNull { it.transformAndTranspose(scanner) }
            if (result != null) {
                transformedScannerList.add(result)
            } else {
                todo.add(scanner)
            }
        }
        lazyResult = transformedScannerList
        return transformedScannerList
    }

}

//----------------------------------------------------------------------------------------------------------------------

data class Scanner(val id: Int, val beaconPositions: Set<Point3D>, val scannerPosition: Point3D? = null) {

    fun transformAndTranspose(otherScanner: Scanner): Scanner? {
        (0..5).forEach { facingIndex ->
            (0..3).forEach { rotatingIndex ->
                val transformedSet = otherScanner.beaconPositions.map { it.face(facingIndex).rotate(rotatingIndex) }.toSet()

                this.beaconPositions.forEach { s1 ->
                    transformedSet.forEach { s2 ->
                        val difference = s1-s2
                        val movedTransformedSet = transformedSet.map { it.plus(difference) }.toSet()
                        if (movedTransformedSet.intersect(this.beaconPositions).size >= 12) {
                            //we have a mapping!! create new scanner with transposed details
                            return Scanner(otherScanner.id, movedTransformedSet, difference)
                        }
                    }
                }

            }
        }
        return null
    }

    companion object {
        //--- scanner 0 ---
        //404,-588,-901
        // ...
        fun of (rawInput: List<String>) : Scanner {
            return Scanner(
                id = rawInput.first().substringAfter("--- scanner "). substringBefore(" ---").toInt(),
                beaconPositions = rawInput.drop(1).map{ Point3D.of(it) }.toSet()
            )
        }
    }
}


fun Point3D.face(facing: Int): Point3D =
    when (facing) {
        0 -> this
        1 -> Point3D(x, -y, -z)
        2 -> Point3D(x, -z, y)
        3 -> Point3D(-y, -z, x)
        4 -> Point3D(y, -z, -x)
        5 -> Point3D(-x, -z, -y)
        else -> error("Invalid facing")
    }

fun Point3D.rotate(rotating: Int): Point3D =
    when (rotating) {
        0 -> this
        1 -> Point3D(-y, x, z)
        2 -> Point3D(-x, -y, z)
        3 -> Point3D(y, -x, z)
        else -> error("Invalid rotation")
    }
