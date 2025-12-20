package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import tool.math.isPrime

fun main() {
    Day1723(test=false).showResult()
}

class Day1723(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val program = ProgramDay23(inputLines, mutableListOf<Long>(0,0,0,0, 0,0,0,0))
        return program.runProgram()
    }

    /**
     * Ik heb er voor gekozen om het programma te analyseren en te herschijven naar een 3GL taal.
     * Goed kijken hoe de loopjes lopen en dan de logica reverse te analyseren. Dan komen we uit op
     * het volgende programma
     *
     *        var h = 0
     *        for (b in 108400..125400 step 17) {
     *            var f = false
     *            for (d in 2 until b) {
     *                for (e in 2 until b) {
     *                    if (d*e == b) {
     *                        f = true
     *                    }
     *                }
     *            }
     *            if (f)
     *                h++
     *        }
     *        return h
     *
     * De twee binnenste lussen bekijken of er delers zijn van b, anders gezegd of b wel of niet priem is. Dat kan al herschreven worden:
     *
     *        var h = 0
     *        for (b in 108400..125400 step 17) {
     *            var f = false
     *            for (d in 2 until b) {
     *                if (b % d == 0) {
     *                    f = true
     *                    break
     *                }
     *            }
     *            if (f)
     *                h++
     *        }
     *        return h
     *
     *  Dit runt al binnen 200 ms en levert 903 als antwoord op. Gebruikmakend van de isPrime() hulpfunctie die ik
     *  al had, kan het nog korter (en sneller):
     *
     *        return (108400..125400 step 17).count { !it.isPrime() }
     *
     */

    override fun resultPartTwo(): Any {
        return (108400..125400 step 17).count { !it.isPrime() }

//      Todd's answer:
//
//        val a = inputLines.first().split(" ")[2].toInt() * 100 + 100000
//        return (a .. a+17000 step 17).count {
//            !it.toBigInteger().isProbablePrime(5)
//        }

//        The loop:
//
//        var h = 0
//        for (b in 108400..125400 step 17) {
//            var f = false
//            for (d in 2 until b) {
//                if (b % d == 0) {
//                    f = true
//                    break
//                }
//            }
//            if (f)
//                h++
//        }
//        return h
    }
}



class ProgramDay23(
    private val programList: List<String>,
    private val register: MutableList<Long> ){

    private var lastPlayedFrequency = -1L
    private var ip = 0

    private var countMuls = 0L

    fun runProgram(): Long {
        while(ip in programList.indices) {
            executeStep()
        }
        return countMuls
    }

    private fun executeStep() {
        val statement = programList[ip].toStatement()
        val regName = if (statement.second.isRegister()) statement.second.asRegister()-'a' else -1
        val firstValue = if (statement.second.isRegister()) register[statement.second.asRegister()-'a'] else statement.second.asLong()
        val secondValue = if (statement.third.isRegister()) register[statement.third.asRegister()-'a'] else statement.third.asLong()
        when (statement.instruction()) {
            "set" -> {register[regName] = secondValue; ip++}
            "sub" -> {register[regName] -= secondValue; ip++}
            "mul" -> {register[regName] *= secondValue; countMuls++; ip++}
            "jnz" -> if (firstValue != 0L) ip += secondValue.toInt() else ip++
            else -> throw Exception("unknown instruction")
        }
    }

    private fun String.toStatement(): Triple<String, String, String> {
        val words = this.trim().split(" ")
        return if (words.size == 3) {
            Triple(words[0].trim(), words[1].trim(), words[2].trim())
        } else {
            Triple(words[0].trim(), words[1].trim(), "0")
        }
    }

    private fun Triple<String, String, String>.instruction() = this.first
    private fun String.isRegister() = (this.length == 1) && (this[0] in 'a' .. 'z')
    private fun String.asRegister() = this[0]
    private fun String.asLong() = this.toLong()
}

