package adventofcode.year2020.december24

// See: https://math.stackexchange.com/questions/2254655/hexagon-grid-coordinate-system

data class Hexagon (val x: Int, val y: Int, val z: Int){
    fun moveTo(direction: HexagonDirection) = Hexagon(x + direction.dx, y + direction.dy, z + direction.dz)
    fun getNeighbours(): List<Hexagon> {
        return listOf(
            this.moveTo(HexagonDirection.EAST),
            this.moveTo(HexagonDirection.WEST),
            this.moveTo(HexagonDirection.SOUTHWEST),
            this.moveTo(HexagonDirection.NORTHEAST),
            this.moveTo(HexagonDirection.SOUTHEAST),
            this.moveTo(HexagonDirection.NORTHWEST)
        )
    }
}

enum class HexagonDirection(val dx: Int, val dy: Int, val dz: Int) {
    EAST (-1, 1, 0),
    WEST (1, -1, 0),

    NORTHEAST (0, 1, -1),
    SOUTHWEST(0, -1, 1),

    NORTHWEST (1, 0, -1),
    SOUTHEAST (-1, 0, 1)
}

