package adventofcode.year2021

import java.lang.Integer.max
import java.lang.Integer.min

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day2117(test=false).showResult()
}

class Day2117(test: Boolean) : PuzzleSolverAbstract(test) {
    private val targetMinX = inputLines.first().substringAfter("target area: x=").substringBefore("..").toInt()
    private val targetMaxX = inputLines.first().substringAfter("..").substringBefore(",").toInt()
    private val targetMinY = inputLines.first().substringAfter(", y=").substringBefore("..").toInt()
    private val targetMaxY = inputLines.first().substringAfterLast("..").toInt()

    override fun resultPartOne(): String {
        var maxHeight = -999
        for (speedX in 0 .. targetMaxX) {
            for (speedY in min(0,targetMinY).. 1000) { //deze 1000 kan berekend worden en daarmee beter worden afgesteld.
                val height = shoot(Speed(speedX,speedY))
                maxHeight = max(maxHeight, height)
            }
        }
        return maxHeight.toString()
    }

    override fun resultPartTwo(): String {
        var count = 0
        for (speedX in 0 .. targetMaxX) {
            for (speedY in min(0,targetMinY).. 1000) {
                val height = shoot(Speed(speedX,speedY))
                if (height > -1_000_000_000) {
                    count++
                }
            }
        }
        return count.toString()
    }

    private fun shoot(startSpeed: Speed): Int {
        var probePos = Pos (0,0)
        var speed = startSpeed

        var maxY = probePos.y
        while ( !posInTarget(probePos) && !posPassedTarget(probePos) ) {
            probePos = probePos.moveTo(speed)
            speed = speed.changeSpeed()
            maxY = max(maxY, probePos.y)
        }

        return if (posInTarget(probePos)) maxY else -1_000_000_000
    }


    private fun posInTarget(pos: Pos): Boolean {
        return pos.x in targetMinX..targetMaxX && pos.y in targetMinY..targetMaxY
    }

    private fun posPassedTarget(pos: Pos): Boolean {
        return pos.x > targetMaxX+1 || pos.y < targetMinY
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Pos(val x: Int, val y: Int) {
    override fun hashCode() = 1000* x + y
    override fun equals(other: Any?) = if (other is Pos) other.x == x && other.y == y else super.equals(other)
    override fun toString() = "($x, $y)"

    fun moveTo(speed: Speed) = Pos(x+speed.x, y+speed.y)
}

class Speed(val x: Int, val y: Int) {
    override fun toString() = "($x, $y)"
    fun changeSpeed() = Speed (if (x < 0) x+1 else if (x > 0) x-1 else x, y-1)
}
