package adventofcode.year2021.december24

import java.lang.Long.max
import kotlin.math.min

/*
 *
 * See bottom of this code for exlplanation
 *
 */

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    val programPartParameterList = inputLines.chunked(18).map{ProgramPartParameter.of(it)}

    override fun resultPartOne(): String {
        return findMaxW().toString()
    }

    override fun resultPartTwo(): String {
        return findMinW().toString()
    }


    private fun findMaxW():Long {
        var zValueReachedByMaxW = mapOf<Long, Long>(0L to 0L)
        for (programPartIndex in programPartParameterList.indices) {
//            println ("$programPartIndex: ${zValueReachedByMaxW.size}")
            zValueReachedByMaxW = calculateMaxWPerProgramPart(programPartIndex, zValueReachedByMaxW)
        }
//        println ("after program: ${zValueReachedByMaxW.size}")
        return zValueReachedByMaxW[0]!!
    }

    private fun calculateMaxWPerProgramPart(programPartIndex: Int, currentZmap: Map<Long, Long> ): Map<Long, Long> {
        val parameter = programPartParameterList[programPartIndex]
        val newZMap = mutableMapOf<Long, Long>()
        for ( (zInput, maxW) in currentZmap) {
            for (w in 1..9) {
                val newZ = aluPart(parameter.divZ, parameter.addX, parameter.addY, w, zInput)
                if (newZMap.contains(newZ)) {
                    newZMap[newZ] = max(newZMap[newZ]!!, maxW * 10 + w)
                } else {
                    newZMap[newZ] = maxW * 10 + w
                }
            }
        }
        return newZMap
    }

    /*
     * For part 2: I did an easy copy/paste and changed max into min. Decided not to refactor that.
     *
     */
    private fun findMinW():Long {
        var zValueReachedByMaxW = mapOf<Long, Long>(0L to 0L)
        for (programPartIndex in programPartParameterList.indices) {
//            println ("$programPartIndex: ${zValueReachedByMaxW.size}")
            zValueReachedByMaxW = calculateMinWPerProgramPart(programPartIndex, zValueReachedByMaxW)
        }
//        println ("after program: ${zValueReachedByMaxW.size}")
        return zValueReachedByMaxW[0]!!
    }

    private fun calculateMinWPerProgramPart(programPartIndex: Int, currentZmap: Map<Long, Long> ): Map<Long, Long> {
        val parameter = programPartParameterList[programPartIndex]
        val newZMap = mutableMapOf<Long, Long>()
        for ( (zInput, minW) in currentZmap) {
            for (w in 1..9) {
                val newZ = aluPart(parameter.divZ, parameter.addX, parameter.addY, w, zInput)
                if (newZMap.contains(newZ)) {
                    newZMap[newZ] = min(newZMap[newZ]!!, minW * 10 + w)
                } else {
                    newZMap[newZ] = minW * 10 + w
                }
            }
        }
        return newZMap
    }



    private fun aluPart(divZ: Long, addX: Long, addY: Long, w: Int, z: Long): Long {
        return if ((z % 26L) + addX != w.toLong()) {
            26 * (z / divZ) + w + addY
        } else {
            z / divZ
        }
    }
}

data class ProgramPartParameter(val divZ: Long, val addX: Long, val addY: Long) {
    companion object {
        fun of(rawInput: List<String>): ProgramPartParameter {
            return ProgramPartParameter(
                divZ = rawInput[4].split(" ").last().toLong(),
                addX = rawInput[5].split(" ").last().toLong(),
                addY = rawInput[15].split(" ").last().toLong()
            )
        }
    }
}


//----------------------------------------------------------------------------------------------------------------------
/*
 * Analysing the input shows that the program is a repeating set of 18 instructions, with a few changes per set of 18
 * (see analyse.xlsx in the data of December24):
 *
 * lines 5, 6 and 16 (one-based) differ in the number
 *   - which is z is divided by (line 5)
 *   - which is added to x (line6) or
 *   - which is added to y (line 16)
 *
 * inp w
 * mul x 0
 * add x z
 * mod x 26
 * div z 1     <-- the 1 differs (extra: it is either 1 or 26), call this variable zDiv
 * add x 15    <-- the 15 differs, call this variable xAdd
 * eql x w
 * eql x 0
 *
 * mul y 0
 * add y 25
 * mul y x
 * add y 1
 * mul z y
 *
 * mul y 0
 * add y w
 * add y 13    <-- the 13 differs, call this variable yAdd
 * mul y x
 * add z y
 *
 * We also can see, that x and y always are rest to zero before they get really used, that z is the one that is carried
 * from program part to program part and that w is starting at the beginning as being the (one-digit) input
 *
 * Analysis of the code part:
 *
 * The first 8 lines
 *   sets x to 1 if (z % 26) + xDiv != w else x is set to 0
 *   and it also sets z to z / zDiv
 *
 * if x = 0, then z is no longer changed, meaning newZ = z / zDiv
 * if x = 1, then newZ = 26 * (z / zDiv) + w + yAdd
 *
 *  We can rewrite these code lines into a kotlin function, with
 *              - the three differentiating parameters,
 *              - w as input and
 *              - z as previous value
 * This function is called aluPart(divZ, addX, addY, w, z) and returns the new z
 *
 * Now you can run program part by program part and each iteration
 *    - starts with a serie of z inputs, and for each z value, we now what the maximum w input was to come to that z value
 *    - after an iteration, we have a new serie of z-values, with corresponding max w inputs.
 * Because each iteration has 9 * zinputs calculations and zinputs is growing, theoratically, you can come up with 9^14 calculations
 * Luckily, the number of different values for z is much smaller per ieteration (it turns out that it reaches 6,5 million different values)
 *
 *
 */

//----------------------------------------------------------------------------------------------------------------------
