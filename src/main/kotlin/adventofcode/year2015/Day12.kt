package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import java.lang.StringBuilder

fun main() {
    Day12(test=false).showResult()
}

class Day12(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val t = Thing.of(inputLines.first())
        return t.sumOfNum(checkRed = false)
    }

    override fun resultPartTwo(): Any {
        val t = Thing.of(inputLines.first())
        return t.sumOfNum(checkRed = true)
    }
}



abstract class Thing {
    abstract fun sumOfNum(checkRed: Boolean): Int
    abstract fun hasRed(): Boolean

    companion object {
        fun of(rawInput: String): Thing {
            return when (rawInput[0]) {
                '[' -> MyArray.of(rawInput)
                '{' -> MyObject.of(rawInput)
                '"' -> MyString.of(rawInput)
                else -> MyNumber.of(rawInput)
            }
        }
    }
}

class MyString(private val value: String): Thing() {
    override fun sumOfNum(checkRed: Boolean) = 0
    override fun hasRed() = (value == "red")

    companion object {
        fun of(rawInput: String): MyString {
            return MyString(rawInput.removeSurrounding("\""))
        }
    }
}

class MyNumber(private val value: Int): Thing() {
    override fun hasRed() = false
    override fun sumOfNum(checkRed: Boolean) = value

    companion object {
        fun of(rawInput: String): MyNumber {
            return MyNumber(rawInput.toInt())
        }
    }
}

class MyArray(private val list: List<Thing>): Thing() {
    override fun hasRed() = false
    override fun sumOfNum(checkRed: Boolean) = list.sumOf { it.sumOfNum(checkRed) }

    companion object {
        fun of(rawInput: String): MyArray {
            val list = rawInput.splitTopLevel()
            return MyArray(list.map{Thing.of(it)})
        }
    }
}

class MyObject(private val map: Map<String, Thing>): Thing() {
    override fun hasRed() = false
    override fun sumOfNum(checkRed: Boolean) = if (checkRed && map.values.any { it.hasRed() }) 0 else map.values.sumOf { it.sumOfNum(checkRed) }

    companion object {
        fun of(rawInput: String): MyObject {
            val list = rawInput.splitTopLevel()
            return MyObject(
                list.associate{ entry ->
                    entry.substringBefore(":").removeSurrounding("\"") to Thing.of(entry.substringAfter(":") )
                })
        }
    }
}

/**
 * Annaame:
 *    de string begint met '[' of met '{' en eindigt met het overeenkomende symbool
 *    daarnaast is de ',' het scheidingsteken
 */
private fun String.splitTopLevel(): List<String> {
    var level = 0
    val result = mutableListOf<String>()
    val item = StringBuilder()
    this.substring(1, this.length-1).forEach { char ->
        when (char) {
            '{', '[' -> {
                item.append(char)
                level++
            }
            '}', ']' -> {
                item.append(char)
                level--
            }
            ',' -> {
                if (level == 0) {
                    result.add(item.toString().trim())
                    item.clear()
                } else {
                    item.append(char)
                }
            }
            else -> {
                item.append(char)
            }
        }
    }
    result.add(item.toString().trim())
    return result
}




