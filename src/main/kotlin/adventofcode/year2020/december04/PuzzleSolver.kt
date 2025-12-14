package adventofcode.year2020.december04

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.splitByCondition

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val passportList = inputLines.splitByCondition { it.isEmpty() }.map{Passport(it)}
        return passportList.count { it.isValid() }.toString()
    }

    override fun resultPartTwo(): String {
        val passportList = inputLines.splitByCondition { it.isEmpty() }.map{Passport(it)}
        return passportList.count { it.isValidByData() }.toString()
    }
}

class Passport(inputLines:List<String>) {
    private val input = inputLines.joinToString(separator = " ", postfix = " ")

    private val byr = getFieldValue("byr")
    private val iyr = getFieldValue("iyr")
    private val eyr = getFieldValue("eyr")
    private val hgt = getFieldValue("hgt")
    private val hcl = getFieldValue("hcl")
    private val ecl = getFieldValue("ecl")
    private val pid = getFieldValue("pid")
    private val cid = getFieldValue("cid")

    fun isValid(): Boolean {
        return byr != null && iyr != null && eyr != null && hgt != null && hcl != null && ecl != null && pid != null
    }

    fun isValidByData(): Boolean {
        if (!isValid())
            return false
        if (byr!!.toIntOrNull() !in 1920..2002 )
            return false
        if (iyr!!.toIntOrNull() !in 2010..2020 )
            return false
        if (eyr!!.toIntOrNull() !in 2020..2030 )
            return false

        if (!hgt!!.endsWith("in") && !hgt.endsWith("cm") )
            return false
        if (hgt.endsWith("cm"))
            if (hgt.substring(0, hgt.length-2).toIntOrNull() !in 150 ..193)
                return false
        if (hgt.endsWith("in"))
            if (hgt.substring(0, hgt.length-2).toIntOrNull() !in 59 ..76)
                return false

        if (hcl!!.length != 7 || hcl[0] != '#' || hcl.substring(1).any{it !in "0123456789abcdef"})
            return false

        if (ecl!! !in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth"))
            return false

        if (pid!!.length != 9 || pid.any{it !in "0123456789"})
            return false

        return true
    }

    private fun getFieldValue(passportFieldName: String) : String? {
        return if (input.contains(passportFieldName))
            input.substringAfter("$passportFieldName:").substringBefore(" ")
        else
            null
    }
}

