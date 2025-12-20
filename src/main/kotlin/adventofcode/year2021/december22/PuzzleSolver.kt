package adventofcode.year2021.december22

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val actionList = inputLines.map {Action(it)}.filter{it.inRange(-50,50)}
        val cuboidSet = doSteps(actionList)
        return cuboidSet.cubeCount().toString()
    }

    override fun resultPartTwo(): String {
        val actionList = inputLines.map {Action(it)}
        val cuboidSet = doSteps(actionList)
        return cuboidSet.cubeCount().toString()
    }

    private fun doSteps(actionList: List<Action>): CuboidSet {
        var cuboidSet = CuboidSet()

        actionList.forEach {action ->
            cuboidSet = if (action.turnOn) {
                cuboidSet.plus(action.cuboid)
            } else {
                cuboidSet.minus(action.cuboid)
            }
        }
        return cuboidSet
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Action(inputLine: String) {

    override fun toString() = "$turnOn --> $cuboid"

    fun inRange(minRange: Long, maxRange: Long): Boolean {
        return cuboid.intersectionOrNull(Cuboid(minRange, maxRange, minRange, maxRange, minRange, maxRange)) != null
    }

    val turnOn = inputLine.substringBefore(" ") == "on"
    val cuboid = Cuboid (
        inputLine.substringAfter("x=").substringBefore("..").toLong(),
        inputLine.substringAfter("..").substringBefore(",").toLong(),
        inputLine.substringAfter("y=").substringBefore("..").toLong(),
        inputLine.substringAfter("y=").substringAfter("..").substringBefore(",").toLong(),
        inputLine.substringAfter("z=").substringBefore("..").toLong(),
        inputLine.substringAfter("z=").substringAfter("..").toLong()
    )
}



