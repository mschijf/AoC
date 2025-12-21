package adventofcode

fun main() {
    runYear(year = 2019,
        test = false,
        verbose = false)
}


fun runYear (year: Int, test: Boolean, verbose: Boolean) {
    if (verbose) {
        (1..25).forEach { dayNr -> runDay(year, dayNr, test=test, true) }
    } else {
        println()
        println("Day Name                                     Init        Puzzle 1        Puzzle 2                                     Part 1                                     Part 2")
        println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------")
        runDay(year, 1, test=false, false, true)
        (1..25).forEach { dayNr -> runDay(year, dayNr, test=false, false) }
        println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------")
    }
}

fun runDay(year: Int, dayNr: Int, test: Boolean, verbose: Boolean, warmingUp: Boolean = false) {

    val fullClassName = determineFullClassName(year, dayNr)
    if (fullClassName != null) {
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
                    print("%6d.%03d ms   ".format(timePassed0 / 1_000_000, timePassed0 % 1_000))
                } else {
                    print(" ${result.dayOfMonth.toString().padStart(2, ' ')} ${result.name.take(30).padEnd(30, ' ')}: ")
                    print("%6d.%03d ms   ".format(timePassed0 / 1_000_000, timePassed0 % 1_000))
                    print(
                        "%6d.%03d ms   ".format(
                            result.timePassedPart1Ns / 1_000_000,
                            result.timePassedPart1Ns % 1_000
                        )
                    )
                    print(
                        "%6d.%03d ms   ".format(
                            result.timePassedPart2Ns / 1_000_000,
                            result.timePassedPart2Ns % 1_000
                        )
                    )

                    print("%40s".format(result.resultPart1))
                    print("   ")
                    print("%40s".format(result.resultPart2))

                }
                println()
            }

        } catch (_: ClassNotFoundException) {
            println("No class/implementation found for $year, Daynumber $dayNr ")
        } catch (otherE: Exception) {
            println("$fullClassName runs with exception ${otherE.cause}")
        }
    } else {
        println("No class/implementation found for $year, Daynumber $dayNr ")
    }
}

private fun determineFullClassName(year: Int, dayNr: Int): String? {
    val classNameDay = "Day%02d".format(dayNr)
    val packageNameYear = "adventofcode.year$year"
    val classNameSolver = "PuzzleSolver"
    val packageNameYearPlusMonthDay = "adventofcode.year$year.december%02d".format(dayNr)
    val packageNameYearPlusDay = "adventofcode.year$year.day%02d".format(dayNr)
    return if (fullClassNameExists(packageNameYear, classNameDay)) {
        "$packageNameYear.$classNameDay"
    } else if (fullClassNameExists(packageNameYearPlusMonthDay, classNameSolver)) {
        "$packageNameYearPlusMonthDay.$classNameSolver"
    }  else if (fullClassNameExists(packageNameYearPlusDay, classNameDay)) {
        "$packageNameYearPlusDay.$classNameDay"
    } else if (fullClassNameExists(packageNameYearPlusMonthDay, classNameDay)) {
        "$packageNameYearPlusMonthDay.$classNameDay"
    } else {
        null
    }
}

private fun fullClassNameExists(packageName: String, className: String) : Boolean {
    return try {
        Class.forName("$packageName.$className").kotlin
        true
    } catch (_: ClassNotFoundException) {
        false
    }
}
