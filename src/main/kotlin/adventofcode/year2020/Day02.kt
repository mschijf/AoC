package adventofcode.year2020

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day02(test=false).showResult()
}

class Day02(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val list = inputLines.map { PasswordPolicy(it) }
        return list.count { it.isValid() }.toString()
    }

    override fun resultPartTwo(): String {
        val list = inputLines.map { PasswordPolicy(it) }
        return list.count { it.isValidPartTwo() }.toString()
    }
}

class PasswordPolicy(inputStr: String) {
    val minOcc = inputStr.substringBefore("-").toInt()
    val maxOcc = inputStr.substringAfter("-").substringBefore(" ").toInt()
    val letter = inputStr.substringAfter(" ").substringBefore(":").first()
    val password = inputStr.substringAfter(": ")

    fun isValid() = password.count{ch -> ch == letter} in minOcc .. maxOcc
    fun isValidPartTwo() = password[minOcc-1] == letter && password[maxOcc-1] != letter ||
            password[minOcc-1] != letter && password[maxOcc-1] == letter
}



