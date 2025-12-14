package adventofcode.year2015

import adventofcode.PuzzleSolverAbstract
import java.util.Comparator
import java.util.PriorityQueue
import kotlin.math.max

fun main() {
    Day1522(test=false).showResult()
}


//Puzzle Input (boss):
//  Hit Points: 51
//  Damage: 9

const val POISON_DAMAGE = 3
const val MISSILE_DAMAGE = 4
const val DRAIN_DAMAGE = 2

const val POISON_TIMER = 6
const val SHIELD_TIMER = 6
const val RECHARGE_TIMER = 5

const val MAGIC_MISSILE_COST = 53
const val DRAIN_COST = 73
const val SHIELD_COST = 113
const val POISON_COST = 173
const val RECHARGE_COST = 229

const val RECHARGE_MANA_POINTS = 101


class Day1522(test: Boolean) : PuzzleSolverAbstract(test) {

    private fun startGameStatus(hasExtraCost: Boolean) =
            GameStatus(
                shieldTimer = 0, poisonTimer = 0, rechargeTimer = 0,
                hitPoints = 50, manaSpend = 0, manaPoints = 500,
                bossHitPoints = 51, damage = 9,
                playerHasExtraCost = hasExtraCost
            )

    //900 is correct
    override fun resultPartOne(): Any {
        return run(startGameStatus(false))
    }

    //1216 is correct
    override fun resultPartTwo(): Any {
        return run(startGameStatus(true))
    }
    
    private fun run(startGameStatus: GameStatus): Int {
        val compareByCost: Comparator<GameStatus> = compareBy { it.manaSpend }
        val priorityQueue = PriorityQueue(compareByCost)

        priorityQueue.addAll(startGameStatus.getStatusListAfterAllPossiblePlayerMoves())
        while (priorityQueue.isNotEmpty()) {
            val current = priorityQueue.remove()
            if (current.bossIsKilled()) {
                return current.manaSpend
            }

            val statusAfterBossMove = current.getStatusAfterBossMove()
            if (statusAfterBossMove.bossIsKilled()) {
                return current.manaSpend
            }

            if (statusAfterBossMove.playerStillAlive()) {
                priorityQueue.addAll(statusAfterBossMove.getStatusListAfterAllPossiblePlayerMoves())
            }
        }
        return -1
    }
}


data class GameStatus(
    val shieldTimer: Int,
    val poisonTimer: Int,
    val rechargeTimer: Int,

    val hitPoints: Int,
    val manaSpend: Int,
    val manaPoints: Int,

    val bossHitPoints: Int,
    val damage: Int,

    val playerHasExtraCost: Boolean
) {

    private val extra = if(playerHasExtraCost) 1 else 0

    override fun toString(): String {
        return "Player has $hitPoints hit points, 0 armor, $manaPoints mana\n" +
                "Boss has $bossHitPoints hit points"
    }

    fun bossIsKilled(): Boolean {
        return bossHitPoints <= 0
    }

    fun playerStillAlive(): Boolean {
        return hitPoints > 0
    }

    private fun poisonDamage() =
        if (poisonTimer > 0) POISON_DAMAGE else 0

    private fun rechargeManaPoints() =
        if (rechargeTimer > 0) RECHARGE_MANA_POINTS else 0

    fun getStatusAfterBossMove(): GameStatus {
        val cost = 0
        val newBossHitPoints = bossHitPoints - poisonDamage()
        return GameStatus(
            shieldTimer = max(shieldTimer-1, 0),
            poisonTimer = max(poisonTimer-1, 0),
            rechargeTimer = max(rechargeTimer-1, 0),

            hitPoints = if (newBossHitPoints > 0)
                hitPoints - if (shieldTimer > 0) damage-7 else damage
            else
                hitPoints,
            manaSpend = manaSpend + cost,
            manaPoints = manaPoints - cost + rechargeManaPoints(),

            bossHitPoints = newBossHitPoints,
            damage = damage,
            playerHasExtraCost
        )
    }

    fun getStatusListAfterAllPossiblePlayerMoves(): List<GameStatus> {
        return listOfNotNull( magicMissile(), drain(), shield(), poison(), recharge() )
    }

    private fun magicMissile(): GameStatus? {
        val cost = MAGIC_MISSILE_COST
        if (manaPoints >= cost && hitPoints > extra) {
            return GameStatus(
                shieldTimer = max(shieldTimer-1, 0),
                poisonTimer = max(poisonTimer-1, 0),
                rechargeTimer = max(rechargeTimer-1, 0),

                hitPoints = hitPoints - extra,
                manaSpend = manaSpend + cost,
                manaPoints = manaPoints - cost + rechargeManaPoints(),

                bossHitPoints = bossHitPoints - poisonDamage() - MISSILE_DAMAGE,
                damage = damage,
                playerHasExtraCost
            )
        }
        return null
    }

    private fun drain(): GameStatus? {
        val cost = DRAIN_COST
        if (manaPoints >= cost && hitPoints > extra) {
            return GameStatus(
                shieldTimer = max(shieldTimer-1, 0),
                poisonTimer = max(poisonTimer-1, 0),
                rechargeTimer = max(rechargeTimer-1, 0),

                hitPoints = hitPoints + 2 - extra,
                manaSpend = manaSpend + cost,
                manaPoints = manaPoints - cost + rechargeManaPoints(),

                bossHitPoints = bossHitPoints - poisonDamage() - DRAIN_DAMAGE,
                damage = damage,
                playerHasExtraCost
            )
        }
        return null
    }


    private fun shield(): GameStatus? {
        val cost = SHIELD_COST
        if (manaPoints >= cost && shieldTimer <= 1 && hitPoints > extra) {
            return GameStatus(
                shieldTimer = SHIELD_TIMER,
                poisonTimer = max(poisonTimer-1, 0),
                rechargeTimer = max(rechargeTimer-1, 0),

                hitPoints = hitPoints - extra,
                manaSpend = manaSpend + cost,
                manaPoints = manaPoints - cost + rechargeManaPoints(),

                bossHitPoints = bossHitPoints - poisonDamage(),
                damage = damage,
                playerHasExtraCost
            )
        }
        return null
    }

    private fun poison(): GameStatus? {
        val cost = POISON_COST
        if (manaPoints >= cost && poisonTimer <= 1 && hitPoints > extra) {
            return GameStatus(
                shieldTimer = max(shieldTimer-1, 0),
                poisonTimer = POISON_TIMER,
                rechargeTimer = max(rechargeTimer-1, 0),

                hitPoints = hitPoints - extra,
                manaSpend = manaSpend + cost,
                manaPoints = manaPoints - cost + rechargeManaPoints(),

                bossHitPoints = bossHitPoints - poisonDamage(),
                damage = damage,
                playerHasExtraCost
            )
        }
        return null
    }


    private fun recharge(): GameStatus? {
        val cost = RECHARGE_COST
        if (manaPoints >= cost && rechargeTimer <= 1 && hitPoints > extra) {
            return GameStatus(
                shieldTimer = max(shieldTimer-1, 0),
                poisonTimer = max(poisonTimer-1, 0),
                rechargeTimer = RECHARGE_TIMER,

                hitPoints = hitPoints - extra,
                manaSpend = manaSpend + cost,
                manaPoints = manaPoints - cost + rechargeManaPoints(),

                bossHitPoints = bossHitPoints - poisonDamage(),
                damage = damage,
                playerHasExtraCost
            )
        }
        return null
    }

}
