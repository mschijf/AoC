package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED

fun main() {
    Day1718(test=false).showResult()
}

class Day1718(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne() = runBlocking() {
        val program = ProgramDay18(inputLines)
        val result = program.runProgram()
        result
    }

    /**
     * Zorg voor een coroutine.
     * En laat beide programma's concurrent draaien
     *
     * Truc is de channel (je kan ook van een BlockingQueue gebruik maken, die heeft een geimplementeerde timeout functie)
     * Om de receive van de channel is een 'withTimeOutOrNull gezet.
     * Die zorgt ervoor dat na een bepaalde tijd, de receive functie stop en dan nul teruggeeft
     */
    override fun resultPartTwo() = runBlocking() {
        val queue0 = Channel<Long>(capacity = UNLIMITED)
        val queue1 = Channel<Long>(capacity = UNLIMITED)

        val program0 = ProgramDay18(inputLines, 0, false, queue0, queue1)
        val program1 = ProgramDay18(inputLines, 1, false, queue1, queue0)

        launch {
            program0.runProgram()
        }
        val countSends1 = program1.runProgram()
        countSends1
    }
}

class ProgramDay18(
    private val programList: List<String>,
    private val programId: Int=0,
    private val puzzle1: Boolean=true,
    private val input: Channel<Long> = Channel<Long>(),
    private val output: Channel<Long> = Channel<Long>()) {

    private val register = mutableListOf<Long>(0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, programId.toLong(),0,0,0,0, 0,0,0,0,0, 0)
    private var lastPlayedFrequency = -1L
    private var ip = 0

    private var countSends = 0L


    suspend fun runProgram(): Long {
        while(ip in programList.indices) {
            executeStep()
        }
        return if (puzzle1) lastPlayedFrequency else countSends
    }

    private suspend fun executeStep() {
        val statement = programList[ip].toStatement()
        val regName = if (statement.second.isRegister()) statement.second.asRegister()-'a' else -1
        val firstValue = if (statement.second.isRegister()) register[statement.second.asRegister()-'a'] else statement.second.asLong()
        val secondValue = if (statement.third.isRegister()) register[statement.third.asRegister()-'a'] else statement.third.asLong()
        when (statement.instruction()) {
            "snd" -> {
                if (puzzle1) {
                    lastPlayedFrequency = firstValue;
                    ip++
                } else {
                    output.send(firstValue)
                    countSends++
                    ip++
                }
            }
            "rcv" -> {
                if (puzzle1) {
                    ip = if (firstValue != 0L) -9999 else ip + 1
                } else {
                    val fromQueue = input.receiveWithTimeout(1000L)
                    if (fromQueue != null) {
                        register[regName] = fromQueue
                        ip++
                    } else {
                        ip = -9999
                    }
                }
            }

            "set" -> {register[regName] = secondValue; ip++}
            "add" -> {register[regName] += secondValue; ip++}
            "mul" -> {register[regName] *= secondValue; ip++}
            "mod" -> {register[regName] %= secondValue; ip++}
            "jgz" -> if (firstValue > 0L) ip += secondValue.toInt() else ip++
            else -> throw Exception("unknown instruction")
        }
    }

    private suspend fun Channel<Long>.receiveWithTimeout(timeMillis: Long): Long? {
        val fromQueue = withTimeoutOrNull(timeMillis) {
            this@receiveWithTimeout.receive()
        }
        return fromQueue
    }

    private fun String.toStatement(): Triple<String, String, String> {
        val words = this.split(" ")
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

