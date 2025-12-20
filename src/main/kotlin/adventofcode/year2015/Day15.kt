package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import tool.mylambdas.substringBetween

fun main() {
    Day15(test=false).showResult()
}

class Day15(test: Boolean) : PuzzleSolverAbstract(test) {

    private val ingredientList = inputLines.map{Ingredient.of(it)}

    override fun resultPartOne(): Any {
        return findOptimalMix(ingredientList) { par -> par.totalScore()}
    }

    override fun resultPartTwo(): Any {
        return findOptimalMix(ingredientList) { par -> if (par.caloriesTotal() == 500) par.totalScore() else 0L }
    }


    private fun findOptimalMix(
        ingredientList: List<Ingredient>,
        inTheMix: List<Pair<Ingredient, Int>> = emptyList(),
        teaSpoonsLeft: Int = 100,
        scoreFunction: (List<Pair<Ingredient, Int>>) -> Long
    ): Long {
        if (teaSpoonsLeft == 0)
            return scoreFunction(inTheMix)
        if (ingredientList.size == 1)
            return scoreFunction(inTheMix + Pair(ingredientList.first(), teaSpoonsLeft))

        var best = -1L
        for (i in 0..teaSpoonsLeft) {
            val newMix = findOptimalMix(
                ingredientList = ingredientList.drop(1),
                inTheMix = inTheMix + Pair(ingredientList.first(), i),
                teaSpoonsLeft = teaSpoonsLeft-i,
                scoreFunction = scoreFunction)
            if (newMix > best)
                best = newMix
        }
        return best
    }

    private fun List<Pair<Ingredient, Int>>.caloriesTotal() =
        this.sumOf { (ingredient, amount) -> ingredient.calories * amount }

    private fun List<Pair<Ingredient, Int>>.totalScore() : Long {
        val capacityTotal = this.sumOf { (ingredient, amount) -> ingredient.capacity * amount.toLong() }
        val durabilityTotal = this.sumOf { (ingredient, amount) -> ingredient.durability * amount.toLong() }
        val flavorTotal = this.sumOf { (ingredient, amount) -> ingredient.flavor * amount.toLong() }
        val textureTotal = this.sumOf { (ingredient, amount) -> ingredient.texture * amount.toLong() }

        return if (capacityTotal < 0 || durabilityTotal < 0 || flavorTotal < 0 || textureTotal < 0)
            0
        else
            capacityTotal * durabilityTotal * flavorTotal * textureTotal
    }
}


//Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
//Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3

data class Ingredient(val name:String, val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int) {

    companion object {
        fun of(rawInput: String) =
            Ingredient(
                name = rawInput.substringBefore(":"),
                capacity = rawInput.substringBetween("capacity ",",").toInt(),
                durability = rawInput.substringBetween("durability ",",").toInt(),
                flavor = rawInput.substringBetween("flavor ",",").toInt(),
                texture = rawInput.substringBetween("texture ",",").toInt(),
                calories = rawInput.substringAfter("calories ").toInt(),
            )
    }
}


