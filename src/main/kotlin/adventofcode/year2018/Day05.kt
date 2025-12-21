package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract
import java.util.*

fun main() {
    Day05(test=false).showResult()
}


class Day05(test: Boolean) : PuzzleSolverAbstract(test) {

    private val line = inputLines.first()

    override fun resultPartOne(): Any {
        return reduce(line)
    }

    override fun resultPartTwo(): Any {
        return ('a'..'z').minOf{reduce(line, it+it.uppercase())}
    }

    private fun reduce(line: String, ignoreString: String = ""): Int {
        val leftPart = Stack<Char>()
        var right = 0
        while (right < line.length) {
            if (line[right] !in ignoreString) {
                if (leftPart.isEmpty()) {
                    leftPart.push(line[right])
                } else if (leftPart.peek().uppercase() == line[right].uppercase() && leftPart.peek() != line[right]) {
                    leftPart.pop()
                } else {
                    leftPart.push(line[right])
                }
            }
            right++
        }
        //println(leftPart.joinToString(""))
        return leftPart.size
    }


    //Todd Ginsberg
    private infix fun Char?.matches(other: Char): Boolean =
        when {
            this == null -> false
            this.uppercaseChar() != other.uppercaseChar() -> false
            else -> this != other
        }

    private fun String.react(): String =
        this.fold(mutableListOf<Char>()) { done, char ->
            if (done.lastOrNull() matches char)
                done.removeLast()
            else
                done.add(char)
            done
        }
            .joinToString(separator = "")
}


