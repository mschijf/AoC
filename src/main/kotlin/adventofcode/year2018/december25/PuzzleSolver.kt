package adventofcode.year2018.december25

import adventofcode.PuzzleSolverAbstract
import kotlin.math.absoluteValue

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private val pointList = inputLines.map{Point4D.of(it)}

    // we houden een lijst bij met constellations. Een constellation is een Set van Point4D objcten
    // we beginnen met een lege lijst
    // Neem het eerste punt
    //   we kijken in welke constellatie die zou passen (daarvoor maken we gebruik van de gemaakte functie inConstellation())
    //   dit levert twee groepen pop: de constellations waar het punt wel in past en een goep met constellaties waar die niet in past
    //   die eerste groep wordt nu in totaal één constellatie, want daar zorgt het nieuwe punt voor. Het nieuwe punt komt daar natuurlijk bij
    //   de tweede groep blijft een ongewijzigde groep
    //   voeg nu het samengestelde deel van de eerste groep weer samen met de tweede groep om weer tot een lijst van constellations te komen
    // En herhaal dit voor all punten
    //
    // note: ik heb dit eerst met een for-lus gedaan en die later stukje bij beetje tot één statement weten te vouwen.
    //       met dank aan de lambda's: partition (om een lijst in twee lijsten te partitioneren)
    //                                 run (om een actie te doen op het resultaat van de vorige lambda
    //                                 fold (om de lijst van punten te 'folden' in de groeiende lijst van constellations

    override fun resultPartOne(): Any {

        val constellations = pointList.fold(emptyList<Set<Point4D>>()){ constellationList, point ->
            constellationList
                .partition { point.inConstellation(it) }
                .run {listOf(first.flatten().toSet() + point) + second}
        }

// Misschien meer leesbaar. De variant met de for lus
//        var constellations = emptyList<Set<Point4D>>()
//        pointList.forEach { point ->
//            constellations = constellations
//                .partition { point.inConstellation(it) }
//                .run {listOf(first.flatten().toSet() + point) + second}
//        }
//        println(constellations.map{it.size})
        return constellations.size
    }

    private fun Point4D.inConstellation(constellation: Set<Point4D>): Boolean {
        return constellation.any{it.manhattanDistance(this) <= 3}
    }
}


data class Point4D(val x: Int, val y: Int, val z: Int, val t: Int) {
    fun manhattanDistance(otherPos: Point4D) = (otherPos.x - x).absoluteValue + (otherPos.y - y).absoluteValue + (otherPos.z - z).absoluteValue + (otherPos.t - t).absoluteValue

    companion object {
        fun of(input: String): Point4D {
            val (x,y,z,t) = input.split(",").map{it.trim().toInt()}
            return Point4D(x,y,z,t)
        }
    }
}