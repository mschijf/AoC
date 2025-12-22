package tool.coordinate.twodimensional

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

data class LinePiece(val from: Point, val to: Point) {
    val isHorizontal get() = from.y == to.y
    val isVertical get() = from.x == to.x
    val isDiagonal get() = (from.x - to.x).absoluteValue  == (from.y - to.y).absoluteValue

    fun length() = from.distanceTo(to)

    val maxX get() = max (from.x, to.x)
    val minX get() = min (from.x, to.x)
    val maxY get() = max (from.y, to.y)
    val minY get() = min (from.y, to.y)
}



