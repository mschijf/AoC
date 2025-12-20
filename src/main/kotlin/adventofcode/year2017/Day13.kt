package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day13(test=false).showResult()
}

/**
 * Korte toelichting:
 * om te kijken of een package op een layer terecht komt, terwijl de scanner net op de bovenste positie staat, kun je
 * een routine schrijven die dat doet (en zo ben ik ook begonnen).
 *
 * Je kan ook een formule bepalen:
 * 1. de scanner in een range van 4 doet er 6 keer over om weer op zijn start positie te komen. In een range van 3 doet het er 4 keer over
 *    In het algemeen heb je (range-1)*2 picoseconden nodig om weer op de start positie te komen.
 * 2. Om te checken of de package gescand wordt door de scanner, moet je, bijv. voor layer 4, kijken of na 4 picos, de scanner weer bovenaan staam
 *    dus: -layerPos mod (range-1)*2 == 0
 *
 * Voor puzzle2:
 * Je bent dan op zoek naar de eerste delay die er voor zorgt dat geen enkele scanner op pos 0 staat. Om te kijken
 * of de scanner op pos 0 staat nar een bepalde delay, moet je ook die delay in de formuel verwerken.
 * Het wordt dan: (-delay-layerPos) mod (range-1)*2 == 0
 *
 * Note: de modulo functie werkt niet lekker met negatieve getallen, daarom is gebruik gemaakt van Math.floorMode
 */


class Day13(test: Boolean) : PuzzleSolverAbstract(test) {

    private val layers =
        inputLines.map{l -> l.split(": ").let{ Layer(it[0].toInt(), it[1].toInt()) }}

    override fun resultPartOne(): Any {
        return layers.sumOf { layer -> if (Math.floorMod(-layer.layerPos, layer.cycleLength()) == 0) layer.range * layer.layerPos else 0}

    }

    override fun resultPartTwo(): Any {
        return generateSequence(0) { it + 1 }
            .first { delay -> layers.none { layer -> Math.floorMod(-delay - layer.layerPos, layer.cycleLength()) == 0}}
    }
}

data class Layer(val layerPos: Int, val range: Int) {
    fun cycleLength() = 2*(range-1)
}


