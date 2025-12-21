package adventofcode.year2018

import adventofcode.PuzzleSolverAbstract
import tool.mylambdas.splitByCondition
import kotlin.math.min

fun main() {
    Day24(test = false).showResult()
}

class Day24(test: Boolean) : PuzzleSolverAbstract(test) {

    private fun createArmy(armyName: String): List<Group> {
        return inputLines
            .splitByCondition { it.isEmpty() }
            .first { it.first() == "$armyName:" }.drop(1)
            .mapIndexed { index, rawInput -> Group.of("$armyName group " + (index+1), rawInput) }
    }

    override fun resultPartOne(): Any {
        val infectionArmy = createArmy("Infection")//orgInfectionArmy.map { it.copy() }
        val immuneArmy = createArmy("Immune System")//orgImmuneArmy.map { it.copy() }

        combat(infectionArmy, immuneArmy)
        return "(infection, immunity) = (${infectionArmy.sumOf { it.unitCount }}, ${immuneArmy.sumOf { it.unitCount }})"
    }

    override fun resultPartTwo(): Any {
        var boost = 0
        var infectionArmy: List<Group>
        var immuneArmy: List<Group>
        do {
            boost++
//            print ("Trying with boost: $boost ==> ")
            infectionArmy = createArmy("Infection")
            immuneArmy = createArmy("Immune System").onEach { it.boostAttackDamage(boost) }

            combat(infectionArmy, immuneArmy)

//            println("(infection, immunity) = (${infectionArmy.sumOf { it.unitCount }}, ${immuneArmy.sumOf { it.unitCount }})")
        } while (infectionArmy.count { it.alive() } != 0)

        return "(infection, immunity) = (${infectionArmy.sumOf { it.unitCount }}, ${immuneArmy.sumOf { it.unitCount }}) after boost: $boost"
    }

    private fun combat(infectionArmy: List<Group>, immuneArmy: List<Group>) {
//        printInfo(infectionArmy, immuneArmy)
        var prevUnitCount = infectionArmy.sumOf { it.unitCount } + immuneArmy.sumOf { it.unitCount }
        while (infectionArmy.any{it.alive()} && immuneArmy.any{it.alive()}) {
            val selection = targetSelectionPhase(infectionArmy.filter { it.alive() }, immuneArmy.filter { it.alive() })
            selection.attackingPhase()
            val newUnitCount = infectionArmy.sumOf { it.unitCount } + immuneArmy.sumOf { it.unitCount }
            if (prevUnitCount == newUnitCount) {
                break
            } else {
                prevUnitCount = newUnitCount
            }
//            printInfo(infectionArmy, immuneArmy)
        }
    }

    private fun printInfo(infectionArmy: List<Group>, immuneArmy: List<Group>) {
        println("Immune System:")
        immuneArmy.forEach { println("  ${it.name}  contains ${it.unitCount} and has effective power (${it.effectivePower()})") }

        println("Infection:")
        infectionArmy.forEach { println("  ${it.name}  contains ${it.unitCount}") }
    }

    private fun targetSelectionPhase(infectionArmy: List<Group>, immuneArmy: List<Group>): Map<Group, Group> {
        val x = immuneArmy.select(infectionArmy)
        val y = infectionArmy.select(immuneArmy)
        return x+y
    }

    private fun List<Group>.select(defendingGroupList: List<Group>): Map<Group, Group> {
        val chosen = mutableSetOf<Group>()
        val result = this
            .sortedWith(compareByDescending<Group> { it.effectivePower() }.thenByDescending { it.initiative })
            .associateWith { attacker ->
                defendingGroupList
                    .filterNot { it in chosen }
                    .filter{it.calculatedDamage(attacker) > 0}
                    .maxWithOrNull(compareBy<Group> { it.calculatedDamage(attacker)}.thenBy{ it.effectivePower() }.thenBy { it.initiative })
                    .also { if (it != null) chosen += it }
            }
        return result.mapNotNull { (key, value) -> value?.let { key to it } }.toMap()
    }

    private fun Map<Group, Group>.attackingPhase() {
        this.keys.sortedByDescending { it.initiative }.forEach {attacker ->
            val defender = this[attacker]!!
            defender.attackedBy(attacker)
        }
    }

}


data class Group(
    val name: String,
    var unitCount: Int,
    val hitPoints: Int,
    val attackType: AttackType,
    var attackDamage: Int,
    val initiative: Int,
    val immuneList: List<AttackType>,
    val weaknessList: List<AttackType>,
) {

    // for data class, we now need to override hashCode and euqls, because the group can be changed due to the var property
    // Tp: rather not use data class with var properties
    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Group) name == other.name else super.equals(other)
    }

    fun alive(): Boolean =
        unitCount > 0

    fun effectivePower(): Int =
        unitCount * attackDamage

    fun attackedBy(attacker: Group) {
        val damage = calculatedDamage(attacker)
        val unitsLost = min(unitCount, damage/hitPoints)
//        println("*** $attacker attacks $name, killing $unitsLost units (by damage of $damage)")
        unitCount -= unitsLost
    }

    fun calculatedDamage(attacker: Group) =
        attacker.effectivePower() * when (attacker.attackType) {
            in immuneList -> 0
            in weaknessList -> 2
            else -> 1
        }

    fun boostAttackDamage(extraDamage: Int) {
        attackDamage += extraDamage
    }

    companion object {
        fun of(name: String, rawInput: String): Group {
            val unitCount = rawInput.substringBefore(" unit")
            val hitPoints = rawInput.substringAfter(" each with ").substringBefore(" hit points ")
            val attackDamage = rawInput.substringAfter(" that does ").substringBefore(" ")
            val attackType = rawInput.substringAfter(attackDamage + " ").substringBefore(" damage")
            val initiative = rawInput.substringAfter("at initiative ")

            val immuneTo = rawInput.toAttackTypeList("immune to ")
            val weakTo = rawInput.toAttackTypeList("weak to ")

            return Group(name, unitCount.toInt(),
                hitPoints.toInt(), AttackType.valueOf(attackType.uppercase()), attackDamage.toInt(), initiative.toInt(),
                immuneTo, weakTo
            )
        }
        private fun String.toAttackTypeList(searcher: String): List<AttackType> {
            return if (this.contains(searcher))
                this.substringAfter(searcher).substringBefore(";").substringBefore(")")
                    .split(",")
                    .map { AttackType.valueOf(it.trim().uppercase()) }
            else
                emptyList()
        }

    }

    override fun toString(): String {
        return "$name ${if (!alive()) "+" else ""}"
    }

    fun print() {
        println("$name ${if (!alive()) "+" else ""}: unit count: $unitCount (Effective Power: ${effectivePower()}) ==> " +
            "hitPoints: $hitPoints, attackType: $attackType, attackDamage: $attackDamage, initiative: $initiative, " +
                    "immune to: '$immuneList', weak to: '$weaknessList'")
    }
}

enum class AttackType {
    FIRE, COLD, SLASHING, RADIATION, BLUDGEONING
}