package adventofcode.year2020.day20

data class TileConfig(
    val tile: Tile,
    private val grid: List<List<Char>>) {

    val upperMatch = mutableSetOf<TileConfig>()
    val rightMatch = mutableSetOf<TileConfig>()
    val bottomMatch = mutableSetOf<TileConfig>()
    val leftMatch = mutableSetOf<TileConfig>()

    private fun isUpperMatchWith(otherTileConfig: TileConfig): Boolean {
        return (grid.indices).all{ col-> grid[0][col] == otherTileConfig.grid[grid.size-1][col]}
    }
    private fun isRightMatchWith(otherTileConfig: TileConfig): Boolean {
        return (grid.indices).all{ row-> grid[row][grid.size-1] == otherTileConfig.grid[row][0]}
    }
    private fun isBottomMatchWith(otherTileConfig: TileConfig): Boolean {
        return (grid.indices).all{ col-> grid[grid.size-1][col] == otherTileConfig.grid[0][col]}
    }
    private fun isLeftMatchWith(otherTileConfig: TileConfig): Boolean {
        return (grid.indices).all{ row-> grid[row][0] == otherTileConfig.grid[row][grid.size-1]}
    }

    fun updateMatch(tileConfigList: List<TileConfig>) {
        tileConfigList.forEach { otherTileConfig ->
            if (this.isUpperMatchWith(otherTileConfig))
                upperMatch.add(otherTileConfig)
            if (this.isRightMatchWith(otherTileConfig))
                rightMatch.add(otherTileConfig)
            if (this.isBottomMatchWith(otherTileConfig))
                bottomMatch.add(otherTileConfig)
            if (this.isLeftMatchWith(otherTileConfig))
                leftMatch.add(otherTileConfig)
        }
    }

    fun getGridWithoutBorders(): List<List<Char>> {
        return grid
            .filterIndexed{index, _ -> index != 0 && index != grid.size-1}
            .map { rows -> rows.filterIndexed{ col, _ -> col != 0 && col != grid.size-1} }
    }
}
