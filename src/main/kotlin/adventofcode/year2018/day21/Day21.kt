package adventofcode.year2018.day21

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day21(test=true).showResult()
}

class Day21(test: Boolean) : PuzzleSolverAbstract(test) {

    private val ipRegister = inputLines.first().split(" ").last().toInt()
    private val operationList = inputLines.drop(1).map{ Operation.from(it) }

    // read https://todd.ginsberg.com/post/advent-of-code/2018/day21/
    // truc: zoek de instructies die iets met register 0 doen.
    //       in dit geval, is het instructie 30 "eqrr 3 0 4", waar vergeleken word met register[3]
    //       als register[3] gelijk is aan register[0], dan wordt (met een omweggetje) de ip 1 hoger gemaakt,
    //       en schieten we uit het programma.
    //       Als we dus het programma runnen met een willekeurg getal voor register[0] en de eerste keer bij
    //       instructie 30 komen, dan kijken we wat de waarde van register[3] is. Als register[0] dat ook had gehad,
    //       dan waren we klaar.
    //       Dus als we die waarde in het begin in register[0] hadden gestopt, dan zou dat leiden tot de snelste
    //       halt-executie van het programma.

    override fun resultPartOne(): Any {
        val dev = RegisterDevice(ipRegister, mutableListOf(0, 0, 0, 0, 0, 0))
        return dev.executeOperationList(operationList, magicInstruction = 30, magicRegister = 3, debug=false).first()
    }

    override fun resultPartTwo(): Any {
        val dev = RegisterDevice(ipRegister, mutableListOf(0, 0, 0, 0, 0, 0))
        return dev.executeOperationList(operationList, magicInstruction = 30, magicRegister = 3, debug=false).last()
    }

}


