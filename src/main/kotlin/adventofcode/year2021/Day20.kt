package adventofcode.year2021

import adventofcode.PuzzleSolverAbstract

fun main() {
    Day20(test=false).showResult()
}

class Day20(test: Boolean) : PuzzleSolverAbstract(test) {

    private val enhancementAlgorithmString = inputLines.first()

    override fun resultPartOne(): String {
        return enhanceInput(2).toString()
    }

    override fun resultPartTwo(): String {
        return enhanceInput(50).toString()
    }

    private fun enhanceInput(timesEnhanced: Int): Int {
        var image = Image(inputLines.drop(2).map { it.toList() })

        repeat(timesEnhanced) {
            image = image.enhance(enhancementAlgorithmString)
        }

//        image.print()
//        println()

        return image.countLightPixels()
    }


}

//----------------------------------------------------------------------------------------------------------------------

class Image(
    private var image: List<List<Char>>,
    private val infinitePixelColor: Char = '.') {

    fun enhance(enhancementAlgorithmString: String): Image {
        image=extendImage(image, 1)
        val result = image
            .mapIndexed{ row, rowList -> List(rowList.size) { col -> enhancePixel(enhancementAlgorithmString, row, col)} }
        return Image(result, enhancePixel(enhancementAlgorithmString, -10, -10))
    }

    private fun enhancePixel(enhancementAlgorithmString: String, row: Int, col: Int): Char {
        return enhancementAlgorithmString[getAlgorithmIndex(row, col)]
    }

    private fun getAlgorithmIndex(row: Int, col: Int) : Int {
        var result = 0
        for (r in row-1 .. row+1) {
            for (c in col-1..col+1) {
                result = 2*result + getPixelValue(r, c)
            }
        }
        return result
    }

    private fun getPixelValue(row: Int, col: Int) : Int {
        return if (row in image.indices && col in image[row].indices)  {
            if (image[row][col] == '.') 0 else 1
        } else {
            if (infinitePixelColor == '.') 0 else 1
        }
    }

    private fun extendImage(image: List<List<Char>>, extraBorderPixels: Int): List<List<Char>> {
        return List(extraBorderPixels){List(2 * extraBorderPixels + image[0].size ) {infinitePixelColor} } +
                image.map { List(extraBorderPixels){infinitePixelColor} + it + List(extraBorderPixels){infinitePixelColor} } +
                List(extraBorderPixels){List(2 * extraBorderPixels + image[0].size) {infinitePixelColor} }
    }

    fun countLightPixels(): Int {
        return image.sumOf { rowList ->  rowList.count { ch -> ch == '#' } }
    }

    fun print() {
        image.forEach { rowList ->
            rowList.forEach { pixel ->
                print(pixel)
            }
            println()
        }
    }

}