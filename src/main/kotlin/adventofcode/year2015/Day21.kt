package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract

import tool.mylambdas.collectioncombination.mapCombinedItems
import tool.mylambdas.splitByCondition
import kotlin.math.max

fun main() {
    Day21(test=false).showResult()
}

// The weapon/armor/ring prices have been copied to the example and input file
// the puzzle input has been added directly in the 'bossPlayer'

//Puzzle Input (boss):
//  Hit Points: 100
//  Damage: 8
//  Armor: 2

class Day21(test: Boolean) : PuzzleSolverAbstract(test) {

    private val itemList = inputLines
        .splitByCondition { it.isEmpty()}
        .map{itemList ->
            itemList
                .drop(1)
                .map{Item.of(it)}}

    private val weaponList = itemList[0]
    private val armorList = itemList[1]
    private val ringList = itemList[2]

    private val emptyItem = Item("No Item", 0,0,0)

    // filled in from puzzle input
    private val boss = Player(100, 8, 2)

    override fun resultPartOne(): Any {
        return makePlayerList()
            .filter{player -> player.beats(boss)}
            .minOf { player -> player.cost }
    }

    override fun resultPartTwo(): Any {
        return makePlayerList().filter{player -> !player.beats(boss)}.maxOf { player -> player.cost }
    }

    private fun Player.beats(other: Player): Boolean {
        var playerHitPoints = this.hitPoints
        var opponentHitPoints= other.hitPoints
        var playerToMove = true
        while (playerHitPoints > 0 && opponentHitPoints > 0) {
            if (playerToMove) {
                opponentHitPoints -= this.damages(other)
            } else {
                playerHitPoints -= other.damages(this)
            }
            playerToMove = !playerToMove
        }
        return (playerHitPoints > 0)
    }

    private fun Player.damages(other: Player) =
        max(1, this.damageScore - other.armorScore)

    private fun makePlayerList(): List<Player> {
        return weaponList.flatMap {weapon ->
            (armorList+emptyItem).flatMap { armor ->
                (ringList+emptyItem+emptyItem).mapCombinedItems { ring1, ring2 ->
                    Player.of(hitPoints = 100, itemList = listOf(weapon, armor, ring1, ring2))
                }
            }
        }
    }
}

data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int) {
    companion object {
        fun of(rawInput: String) =
            rawInput.split("\\s+".toRegex()).let{ rp ->
                Item(
                    name = rp.subList(0, rp.size - 3).joinToString(" "),
                    cost = rp[rp.size - 3].toInt(),
                    damage = rp[rp.size - 2].toInt(),
                    armor = rp[rp.size - 1].toInt(),
                )
            }
    }
}

data class Player(val hitPoints:Int, val damageScore: Int, val armorScore: Int, val cost: Int = 0) {
    companion object {
        fun of(hitPoints: Int, itemList : List<Item>) =
            Player(hitPoints, itemList.sumOf{it.damage}, itemList.sumOf{it.armor}, itemList.sumOf{it.cost})
    }
}


