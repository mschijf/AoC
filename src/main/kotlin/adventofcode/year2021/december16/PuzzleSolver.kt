package adventofcode.year2021.december16

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val bitString = inputLines.first().map{toBits(it)}.joinToString(separator = "")

    override fun resultPartOne(): String {
        val parser = Parser().parsePackage(bitString)
        return parser.versionSum().toString()
    }

    override fun resultPartTwo(): String {
        val parser = Parser().parsePackage(bitString)
        return parser.value().toString()
    }

    private fun toBits(ch:Char) : String {
        val bits = "000" + Integer.toBinaryString(if (ch in '0' .. '9') ch - '0' else 10 + (ch - 'A'))
        return bits.substring(bits.length-4)
    }
}

//----------------------------------------------------------------------------------------------------------------------

abstract class Package(
    val version: Int,
    val id: Int){

    abstract fun bitLength(): Int
    abstract fun versionSum(): Int
    abstract fun value(): Long
}

class Literal(version: Int,
              id: Int,
              private val value: Long,
              private val bitLength: Int): Package(version, id) {

    override fun bitLength() = bitLength + 6
    override fun versionSum() = version
    override fun value() = value
}

class Operator(version: Int,
               id: Int,
               private val lengthTypeId: Int,
               private val packageList: List<Package>): Package(version, id) {

    override fun bitLength() = packageList.sumOf { it.bitLength() } + 6 + 1 + if (lengthTypeId == 0) 15 else 11
    override fun versionSum() = packageList.sumOf { it.versionSum() } + version
    override fun value(): Long {
        return when (id) {
            0 -> packageList.sumOf { it.value() }
            1 -> packageList.map{it.value()}.reduce {acc, i ->  acc * i }
            2 -> packageList.minOf { it.value() }
            3 -> packageList.maxOf { it.value() }
            5 -> if (packageList[0].value() > packageList[1].value()) 1 else 0
            6 -> if (packageList[0].value() < packageList[1].value()) 1 else 0
            7 -> if (packageList[0].value() == packageList[1].value()) 1 else 0
            else -> throw Exception("unexpected operator number")
        }
    }
}

class Parser {

    fun parsePackage(input: String): Package {
        val version = input.substring(0,3).toInt(2)
        val id = input.substring(3, 6).toInt(2)
        if (id == 4) {
            return parseLiteral(version, id, input.substring(6))
        } else {
            val lengthTypeId = input.substring(6, 7).toInt(2)
            return parseOperator(version, id, lengthTypeId, input.substring(7))
        }
    }

    private fun parseLiteral(version: Int, id: Int, literalInput: String): Literal {
        var i=0
        var literalValue = 0L
        while (literalInput[i] == '1') {
            literalValue = 16*literalValue + + literalInput.substring(i+1, i+5).toInt(2)
            i+=5
        }
        literalValue = 16*literalValue + literalInput.substring(i+1, i+5).toInt(2)
        return Literal(version, id, literalValue, i+5)
    }

    private fun parseOperator(version: Int, id: Int, lengthTypeId: Int, operatorInput: String): Operator {
        val packageList = mutableListOf<Package>()
        if (lengthTypeId == 0) {
            val totalLengthInBits = operatorInput.substring(0,15).toInt(2)
            var bitsRead = 0
            while (bitsRead < totalLengthInBits) {
                val remainder = operatorInput.substring(15+bitsRead)
                val pack = parsePackage(remainder)
                packageList.add(pack)
                bitsRead += pack.bitLength()
            }
        } else {
            val totalPackages = operatorInput.substring(0,11).toInt(2)
            var bitsRead = 0
            for (i in 0 until totalPackages) {
                val remainder = operatorInput.substring(11+bitsRead)
                val pack = parsePackage(remainder)
                packageList.add(pack)
                bitsRead += pack.bitLength()
            }
        }
        return Operator(version, id, lengthTypeId, packageList)
    }
}