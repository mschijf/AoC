package adventofcode.year2020.toolday20

class Arrangement(inputArrangement:List<List<TileConfig?>> ) {
    private val arrangement = inputArrangement.map { rows -> rows.map { it!! } }

    fun getCornerSquaresProduct(): Long {
        return arrangement[0][0].tile.id *
                arrangement[0][arrangement.size-1].tile.id *
                arrangement[arrangement.size-1][arrangement.size-1].tile.id *
                arrangement[arrangement.size-1][0].tile.id
    }

    fun getRoughness(): Long {
        return Image(this).getRoughNess().toLong()
    }

    fun mergeTiles(): List<String> {
        val result = mutableListOf<String>()
        val compressed = arrangement.map{ rows -> rows.map{ tileCfg -> tileCfg.getGridWithoutBorders()} }
        for (puzzleRow in compressed.indices) {
            for (tileRow in compressed[puzzleRow][0].indices) {
                val totalRow = mutableListOf<Char>()
                for (puzzleCol in compressed[puzzleRow].indices) {
                    totalRow += compressed[puzzleRow][puzzleCol][tileRow]
                }
                result.add(totalRow.joinToString(""))
            }
        }
        return result
    }

}