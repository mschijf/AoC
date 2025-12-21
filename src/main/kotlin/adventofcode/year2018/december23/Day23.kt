package adventofcode.year2018.december23

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.threedimensional.Point3D
import tool.primarytype.log10
import tool.primarytype.pow
import kotlin.math.absoluteValue
import kotlin.math.max

fun main() {
    Day23(test=false).showResult()
}

class Day23(test: Boolean) : PuzzleSolverAbstract(test) {

    private val nanoBotList =
        inputLines.map {
            NanoBot(Point3D.of(it.substringAfter("pos=").substringBefore(", r=")), it.substringAfter("r=").toInt())
        }


    override fun resultPartOne(): Any {
        val strongRobot = nanoBotList.maxBy { it.range }
        return nanoBotList.count { it.location.distanceTo(strongRobot.location) <= strongRobot.range }
    }

    // answer: 71631000

    /**
     * Mooie methode (dank aan reddit): scale down
     * 1. Deel alle input door 10 miljoen. (dus alle coordinaten en alle ranges).
     * 2. Ga da alle punten af die een bepaalde afstand (bijv. 25 = cubeSize) afliggen
     * 3. Dit levert een optimaal punt op.
     * 4. Herhaal vanaf stap 1, maar nu met de input delen door 10 miljoen (dan 1M, dan 100K, 10K, 1K, 100, 10,  1)
     *    Maar nu beginnend vanuit het punt dat je bij stap 3 hebt gevonden. Vermenigvuldig iedere coordinaat van dit punt met 10.
     *
     * Note: het start divider getal (10 miljoen) is gekozen door het grootste inputgetal te vinden, daar de 10-macht
     *       van te bepalen en die door 10 te delen
     * Note: cubeSize heb ik (na wat proberen) op 2*10 gezet. Met alles groter werkt het ook.
     *       nog enigszins over nagedacht. Omdat je met een factor 10 schaalt, wil ik 10 stappen in zowel x, y als z richting gaan onderzoeken
     */
    override fun resultPartTwo(): Any {
        val cubeSize = 2*10
        val maxCoordinate = nanoBotList.maxOf { max(max(max(it.location.x.absoluteValue, it.location.y.absoluteValue), it.location.z.absoluteValue), it.range) }
        var divider = 10.pow(maxCoordinate.log10()).toInt() / 10
        var start = Point3D.origin
        while (divider != 0) {
            start = start.mul(10)
//            print("Start: $start, Divider: $divider ")
            val iteration = nanoBotList.map { NanoBot(it.location.div(divider), it.range/divider) }
            val solution = start.makeCube(cubeSize).maxBy { iteration.countNanoBotsInRange(it) }
            start = solution
//            println(" --> $solution")
            divider /= 10
        }
        return start.distanceTo(Point3D.origin)
    }

    private fun Point3D.makeCube(cubeSize: Int): List<Point3D> {
        val range = (-cubeSize..cubeSize)
        val attemptList =
            range.flatMap { dx ->
                range.flatMap { dy ->
                    range.map { dz ->
                        this.plusXYZ(dx, dy, dz)
                    }
                }
            }
        return attemptList
    }

    private fun List<NanoBot>.countNanoBotsInRange(aPoint: Point3D): Int {
        return this.count{it.location.distanceTo(aPoint) <= it.range}
    }

    private fun Point3D.div(divider: Int): Point3D =
        Point3D(this.x/divider, this.y/divider, this.z/divider)

    private fun Point3D.mul(factor: Int): Point3D =
        Point3D(this.x*factor, this.y*factor, this.z*factor)

    /******************************************************************************************************************/

    /**
     * see https://todd.ginsberg.com/post/advent-of-code/2018/day23/ for solution
     * not foudn by myself, but gives same answer as above (and much faster)
     *
     */
    private fun ginsbergSolutionWithBronKerbosch(): Any {

        // make a map per NanoBot with a set of other bots that are in each other's reach
        val neighbors: Map<NanoBot, Set<NanoBot>> = nanoBotList.map { bot ->
            Pair(bot, nanoBotList.filterNot { it == bot }.filter { bot.withinRangeOfSharedPoint(it) }.toSet())
        }.toMap()

        //Zoek de largest clique...
        val clique: Set<NanoBot> = BronKerbosch(neighbors).largestClique()

        //En in deze kliek, ga op zoek naar de ... geen idee
        return clique.map { it.location.distanceTo(Point3D.origin) - it.range }.max()
    }

}

//----------------------------------------------------------------------------------------------------------------------

data class NanoBot(val location: Point3D, val range: Int) {
    fun withinRangeOfSharedPoint(other: NanoBot): Boolean =
        location.distanceTo(other.location) <= (range + other.range)
}

//----------------------------------------------------------------------------------------------------------------------
