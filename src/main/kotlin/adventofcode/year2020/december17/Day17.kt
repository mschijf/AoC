package adventofcode.year2020.december17

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day17(test=false).showResult()
}

class Day17(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        var activeCubes = inputLines.map { it.toList()}
            .mapIndexed { y, chars -> chars.mapIndexed { x, c -> if (c=='#') Pos(x,y,0) else null }.filterNotNull() }
            .flatten()

        repeat (6) {
            val remainActiveList = activeCubes
                .filter { cube -> cube.neighbours().count { neighbour -> neighbour in activeCubes } in 2..3 }
            val newActive = activeCubes
                .map { it.neighbours() }
                .flatten()
                .distinct()
                .filter { cube -> cube.neighbours().count { neighbour -> neighbour in activeCubes } == 3 }
            activeCubes = (remainActiveList + newActive).distinct()
        }

        return activeCubes.size.toString()
    }

    override fun resultPartTwo(): String {
        var activeCubes = inputLines.map { it.toList()}
            .mapIndexed { y, chars -> chars.mapIndexed { x, c -> if (c=='#') PosFour(0, x,y,0) else null }.filterNotNull() }
            .flatten()

        repeat (6) {
            val remainActiveList = activeCubes
                .filter { cube -> cube.neighbours().count { neighbour -> neighbour in activeCubes } in 2..3 }
            val newActive = activeCubes
                .map { it.neighbours() }
                .flatten()
                .distinct()
                .filter { cube -> cube.neighbours().count { neighbour -> neighbour in activeCubes } == 3 }
            activeCubes = (remainActiveList + newActive).distinct()
        }

        return activeCubes.size.toString()
    }

}


data class Pos(val x: Int, val y: Int, val z: Int) {

    fun neighbours(): List<Pos> {
        val result = mutableListOf<Pos>()
        for (dx in x-1 .. x+1) {
            for (dy in y - 1..y + 1) {
                for (dz in z - 1..z + 1) {
                    result.add(Pos(dx, dy, dz))
                }
            }
        }
        return result.minus(this)
    }
}

data class PosFour(val w: Int, val x: Int, val y: Int, val z: Int) {

    fun neighbours(): List<PosFour> {
        val result = mutableListOf<PosFour>()
        for (dw in w-1 .. w+1) {
            for (dx in x - 1..x + 1) {
                for (dy in y - 1..y + 1) {
                    for (dz in z - 1..z + 1) {
                        result.add(PosFour(dw, dx, dy, dz))
                    }
                }
            }
        }
        return result.minus(this)
    }
}

