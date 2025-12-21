package adventofcode.year2020

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day21(test=false).showResult()
}

class Day21(test: Boolean) : PuzzleSolverAbstract(test) {

    private val foodList = inputLines.map { Food(it) }
    private val totalAllergenSet = foodList.map { it.allergenSet }.flatten().toSet()
    private val totalIngredientsSet = foodList.map { it.ingredientSet }.flatten().toSet()

    override fun resultPartOne(): String {
        val possibleIngredientsForAllergenes = totalAllergenSet.map{ allergen -> getPossibleIngredientsPerAllergen(allergen) }
        val nonAllergyIngredients = totalIngredientsSet - possibleIngredientsForAllergenes.flatten().toSet()
        val count = nonAllergyIngredients.sumOf { ingr -> foodList.count { food -> food.hasIngredient(ingr) } }
        return count.toString()
    }

    override fun resultPartTwo(): String {
        val possibleIngredientsForAllergenes = totalAllergenSet.associateWith { allergen -> getPossibleIngredientsPerAllergen(allergen).toMutableSet() }
        do {
            val singleSet = possibleIngredientsForAllergenes.values.filter { it.size == 1 }.flatten().toSet()
            for (ingredient in possibleIngredientsForAllergenes.values) {
                if (ingredient.size > 1) {
                    ingredient.removeAll(singleSet)
                }
            }
        } while (possibleIngredientsForAllergenes.values.any { it.size > 1 })

        return possibleIngredientsForAllergenes.toSortedMap().values.joinToString(",") { it.first().name }
    }

    private fun getPossibleIngredientsPerAllergen(allergen: Allergen): Set<Ingredient> {
        var result = emptySet<Ingredient>()
        for (food in foodList.filter {food -> food.hasAllergen(allergen) }) {
            result = if (result.isEmpty()) food.ingredientSet else result intersect food.ingredientSet
        }
        return result
    }
}

class Food(inputLine: String) {
    val ingredientSet = inputLine.substringBefore(" (contains").split(" ").map{Ingredient(it)}.toSet()
    val allergenSet = inputLine.substringAfter("(contains ").substringBefore(")").split(", ").map{Allergen(it)}.toSet()
    fun hasAllergen(allergen: Allergen) = allergen in allergenSet
    fun hasIngredient(ingredient: Ingredient) = ingredient in ingredientSet
}

data class Ingredient(val name: String)
data class Allergen(val name: String): Comparable<Allergen> {
    override fun compareTo(other: Allergen): Int {
        return name.compareTo(other.name)
    }
}


