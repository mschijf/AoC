package adventofcode.year2017

import adventofcode.PuzzleSolverAbstract
import tool.coordinate.threedimensional.Point3DLong
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    Day1720(test=false).showResult()
}

class Day1720(test: Boolean) : PuzzleSolverAbstract(test) {

    private val particleList = inputLines.mapIndexed{index, line -> Particle.of(index, line) }

//    override fun resultPartOne(): Any {
//        var next = particleList
//        var prevSort = particleList.sortedBy { it.distanceToOrg() }.map { it.id }
//        var i = 0
//        var eqSort = 0
//        var first = -1
//        while(true) {
//            i++
//            next = next.map { it.tick() }
//            val currentSort = next.sortedBy { it.distanceToOrg() }.map { it.id }
//            if (i >= 1_000)//currentSort == prevSort)
//                break
//            if (currentSort == prevSort) {
//                if (first < 0)
//                    first = i
//                eqSort++
//            }
//            prevSort = currentSort
//        }
//        println("$i:  $eqSort  ($first)")
//        return prevSort.first()
//    }

    override fun resultPartOne(): Any {
        var minList = particleList
        while (minList.any{ !it.acceleration.equalSign(it.velocity) } ) {
            minList = minList.map { it.tick() }
        }
        while (minList.any{ !it.velocity.equalSign(it.pos) } ) {
            minList = minList.map {it.tick()}
        }
        return minList.minBy{1_000_000 * it.accSum() + it.velocitySum()}.id
    }

    fun resultPartOne_version2(): Any {
        val minAcc = particleList.minOf { it.accSum()  }
        var minList = particleList.filter { it.accSum() == minAcc }
        while (minList.size != 1 && minList.any{ !it.acceleration.equalSign(it.velocity) } ) {
            minList = minList.map {it.tick()}
        }

        val minVelocity = minList.minOf { it.velocitySum()  }
        minList = minList.filter { it.velocitySum() == minVelocity }
        while (minList.size != 1 && minList.any{ !it.velocity.equalSign(it.pos) } ) {
            minList = minList.map {it.tick()}
        }
        return minList.first().id
    }

    override fun resultPartTwo(): Any {
        var minList = particleList

        repeat(1000) {
            minList = minList
                .map { it.tick() }
                .groupBy { it.pos }
                .filterValues { it.size == 1 }
                .values
                .flatten()
        }
        return minList.size
    }

    fun Point3DLong.equalSign(other: Point3DLong) =
        (this.x == 0L || this.x.sign == other.x.sign) &&
                (this.y == 0L || this.y.sign == other.y.sign) &&
                (this.z == 0L || this.z.sign == other.z.sign)
}


data class Particle(val id: Int, val pos: Point3DLong, val velocity: Point3DLong, val acceleration: Point3DLong) {

    fun tick(): Particle {
        val newVelocity = velocity.plusXYZ(acceleration.x, acceleration.y, acceleration.z)
        val newPos = pos.plusXYZ(newVelocity.x, newVelocity.y, newVelocity.z)
        return Particle(id, newPos, newVelocity, acceleration)
    }

    fun distanceToOrg(): Long =
        pos.distanceTo(Point3DLong.origin)

    fun accSum() = acceleration.x.absoluteValue + acceleration.y.absoluteValue + acceleration.z.absoluteValue
    fun velocitySum() = velocity.x.absoluteValue + velocity.y.absoluteValue + velocity.z.absoluteValue
    fun posSum() = pos.x.absoluteValue + pos.y.absoluteValue + pos.z.absoluteValue

    companion object {
        fun of(id: Int, rawInput: String): Particle {
            //p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>
            val pos = Point3DLong.of(rawInput.substringAfter("p=<").substringBefore(">,").trim())
            val speed = Point3DLong.of(rawInput.substringAfter("v=<").substringBefore(">,").trim())
            val acc = Point3DLong.of(rawInput.substringAfter("a=<").substringBefore(">").trim())
            return Particle(id, pos, speed, acc)
        }
    }

    override fun toString(): String {
        return "\n$id: $pos, $velocity, $acceleration"
    }
}

