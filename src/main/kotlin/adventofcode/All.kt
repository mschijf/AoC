package adventofcode

fun main() {
    for (year in 2015..2025) {
        runYear(year = year,
            test = false,
            verbose = false)

    }
//    runYear(year = 2025,
//        test = false,
//        verbose = false)
}


fun runYear (year: Int, test: Boolean, verbose: Boolean) {
    if (verbose) {
        (1..25).forEach { dayNr -> runDay(year, dayNr, test=test, true) }
    } else {
        println()
        println("Day Name                                   Init      Puzzle 1      Puzzle 2                                     Part 1                                     Part 2")
        println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------")
        runDay(year, 1, test=false, false, true)
        (1..25).forEach { dayNr -> runDay(year, dayNr, test=false, false) }
        println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------")

    }
}

fun runDay(year: Int, dayNr: Int, test: Boolean, verbose: Boolean, warmingUp: Boolean = false) {

    val fullClassName = determineFullClassName(year, dayNr)
    try {
        val startTime = System.nanoTime()
        val kClass = Class.forName(fullClassName).kotlin
        val methodName = if (verbose) "showResultShort" else "executeOnly"
        val method = kClass.members.find { it.name == methodName }
        val obj = kClass.constructors.first().call(test)
        val timePassed0 = System.nanoTime() - startTime
        val response = method!!.call(obj)
        if (!verbose) {
            val result = response as PuzzleResultData
            if (warmingUp) {
                print("    ${"Warming up ...".padEnd(30, ' ')}: ")
                print("%4d.%03d ms   ".format(timePassed0 / 1_000_000, timePassed0 % 1_000))
            } else {
                print(" ${result.dayOfMonth.toString().padStart(2, ' ')} ${result.name.take(30).padEnd(30, ' ')}: ")
                print("%4d.%03d ms   ".format(timePassed0 / 1_000_000, timePassed0 % 1_000))
                print("%4d.%03d ms   ".format(result.timePassedPart1Ns / 1_000_000, result.timePassedPart1Ns % 1_000))
                print("%4d.%03d ms   ".format(result.timePassedPart2Ns / 1_000_000, result.timePassedPart2Ns % 1_000))

                print("%40s".format(result.resultPart1))
                print ("   ")
                print("%40s".format(result.resultPart2))

            }
            println()
        }

    } catch (_: ClassNotFoundException) {
        if (verbose) {
            println("$fullClassName not implemented (yet)")
        } else {
            println("No class/implementation found for $year, Daynumber $dayNr ")
        }
    } catch (otherE: Exception) {
        println("$fullClassName runs with exception ${otherE.cause}")
    }
}

private fun determineFullClassName(year: Int, dayNr: Int): String {

    return try {
        val className = "Day%02d%02d".format(year - 2000, dayNr)
        val packageName = "adventofcode.year$year"

        val kClass = Class.forName("$packageName.$className").kotlin
        "$packageName.$className"
    } catch (_: ClassNotFoundException) {
        val className = "PuzzleSolver"
        val packageName = "adventofcode.year$year.december%02d".format(dayNr)

        try {
            val kClass = Class.forName("$packageName.$className").kotlin
            "$packageName.$className"
        } catch (_: ClassNotFoundException) {
            "No class/implementation found for $year, Daynumber $dayNr "
        }

    }
}
