package adventofcode.year2020.toolday20

class Tile(inputLines: List<String>) {
    val id = inputLines.first().substringAfter("Tile ").substringBefore(":").toLong()
    val tileConfigList = createAllConfigurations(inputLines)

    private fun createAllConfigurations(inputLines: List<String>): List<TileConfig> {
        val startGrid = inputLines.drop(1).map{it.toList()}
        return listOf(
            TileConfig(this, startGrid),
            TileConfig(this, rotate90(startGrid)),
            TileConfig(this, rotate90(rotate90(startGrid))),
            TileConfig(this, rotate90(rotate90(rotate90(startGrid)))),
            TileConfig(this, flip(startGrid)),
            TileConfig(this, rotate90(flip(startGrid))),
            TileConfig(this, rotate90(rotate90(flip(startGrid)))),
            TileConfig(this, rotate90(rotate90(rotate90(flip(startGrid)))))
        )
    }

    private fun flip(aGrid: List<List<Char>>): List<List<Char>> {
        return aGrid.mapIndexed{ row, rows -> List(rows.size) { col -> aGrid[row][rows.size - col - 1] } }
    }

    private fun rotate90(aGrid: List<List<Char>>): List<List<Char>> {
        return aGrid.mapIndexed{ row, rows -> List(rows.size) { col -> aGrid[aGrid.size - 1 - col][row] } }
    }

    fun updateMatchList(allTiles: List<Tile>) {
        allTiles.forEach {otherTile ->
            if (this != otherTile)
                updateMatch(otherTile)
        }
    }

    private fun updateMatch(other: Tile) {
        for (tileConfig in tileConfigList) {
            tileConfig.updateMatch(other.tileConfigList)
        }
    }
}