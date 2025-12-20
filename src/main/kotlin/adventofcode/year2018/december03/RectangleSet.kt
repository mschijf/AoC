package adventofcode.year2018.december03

data class RectangleSet(
    private val rectangleList: List<Rectangle> = emptyList()) {

    fun area() = rectangleList.sumOf { rectangle -> rectangle.area() }

    fun plus(newRectangle: Rectangle?): RectangleSet {
        if (newRectangle == null)
            return this
        var leftOver = listOf(newRectangle)
        for (rectangle in rectangleList) {
            leftOver = leftOver.flatMap { leftOverRectangle -> leftOverRectangle.minus(rectangle) }
        }
        return RectangleSet(rectangleList + leftOver)
    }

    fun minus(rectangle: Rectangle?): RectangleSet {
        return RectangleSet(rectangleList.map { it.minus(rectangle) }.flatten())
    }

}
