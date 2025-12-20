package adventofcode

import tool.coordinate.twodimensional.pos
import java.io.File

abstract class PuzzleSolverAbstract (
    val test: Boolean,
    private val puzzleName: String = "",
    private val hasInputFile: Boolean = true) {

    private val dayOfMonth = getDayOfMonthFromSubClassName()
    private val year = getYearFromSubClassName()

    open fun resultPartOne(): Any = "NOT IMPLEMENTED"
    open fun resultPartTwo(): Any = "NOT IMPLEMENTED"

    val inputLines = inputLines()

    fun showResult() {
        val dayNumber = "%2d".format(dayOfMonth)
        println("Day $dayNumber       : $puzzleName")
        println("Version      : ${if (test) "test" else "real"} input")
        println("------------------------------------")

        printResult(1) { resultPartOne().toString() }
        printResult(2) { resultPartTwo().toString() }
        println("==================================================================")
    }

    fun executeOnly(): PuzzleResultData{
        var resultPart1: Any = "N/A"
        var resultPart2: Any = "N/A"
        val timePassedPart1Ns = getResultTimeOnly() { resultPart1 = resultPartOne() }
        val timePassedPart2Ns = getResultTimeOnly() { resultPart2 = resultPartTwo() }
        return PuzzleResultData(dayOfMonth, puzzleName, timePassedPart1Ns, timePassedPart2Ns, resultPart1, resultPart2)
    }

    private fun printResult(puzzlePart: Int, getResult: () -> String ) {
        val startTime = System.nanoTime()
        val result = getResult()
        val timePassed = System.nanoTime() - startTime
        print("Result part $puzzlePart: $result (after %d.%03d ms)".format(timePassed / 1_000_000, timePassed % 1_000))
        println()
    }

    private fun getResultTimeOnly(getResult: () -> Any ): Long {
        val startTime = System.nanoTime()
        getResult()
        val timePassed = System.nanoTime() - startTime
        return timePassed
    }


    private fun getDayOfMonthFromSubClassName(): Int {
        val className = this.javaClass.name.lowercase()
        val dayOfMonth = if (className.contains("day")) {
            className.substringAfter("day").takeLast(2)
        } else if (className.contains("december") && className.contains("puzzlesolver")) {
            className.substringAfter("december").take(2)
        } else {
            className.takeLast(2)
        }
        return dayOfMonth.toInt()
    }

    private fun getYearFromSubClassName(): Int {
        val className = this.javaClass.name.lowercase()
        val yearStr = if (className.contains(".year")) {
            className.substringAfter(".year").take(4)
        } else {
            throw Exception ("No Year Package found")
        }
        return yearStr.toInt()
    }

    fun inputLines(testFile: String="example", liveFile: String="input", path:String = defaultPath()) =
        if (test) getInputLines(path, testFile) else getInputLines(path, liveFile)

    private fun defaultPath() = String.format("data/%04d/december%02d", year, dayOfMonth)

    private fun getInputLines(path: String, fileName: String): List<String> {
        val file = File("$path/$fileName")
        val inputLines = if (file.exists()) file.bufferedReader().readLines() else emptyList()
        if (inputLines.isEmpty() && hasInputFile)
            println("No input lines!!")
        return inputLines
    }

    fun inputAsGrid(testFile: String="example", liveFile: String="input", path:String = defaultPath()) =
        inputLines(testFile=testFile, liveFile=liveFile, path=path)
            .asGrid()

    fun List<String>.asGrid() =
        this
            .flatMapIndexed { y, line ->
                line.mapIndexed { x, ch ->  pos(x,y) to ch}
            }
            .toMap()
}