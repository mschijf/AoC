package adventofcode2017

/**
 * Generalized KnotHash encoding, cause of multiple usages in puzzles
 * Originally build for Day 10
 */

fun String.knotHashEncode(): String {
    val numbers = IntArray(256) { it }
    val lengths = this.map { it.code }.toList()+listOf(17, 31, 73, 47, 23)
    var current = 0
    var skipSize = 0
    repeat(64) {
        val (newCurrent, newSkipSize) = numbers.doOneKnotHashCycle(current, skipSize, lengths)
        current = newCurrent
        skipSize = newSkipSize
    }
    return numbers.toDenseHash().toHex()
}

private fun IntArray.toDenseHash():List<Int> {
    return this.toList().chunked(16).map{it.reduce { acc, i -> acc xor i }}
}

private fun List<Int>.toHex():String {
    return this.joinToString (""){ "%02x".format(it) }
}


fun IntArray.doOneKnotHashCycle(startCurrent: Int=0, startSkipSize: Int=0, lengths: List<Int>): Pair<Int, Int> {
    var current = startCurrent
    var skipSize = startSkipSize

    lengths.forEach {length ->
        this.reverseOrder(current, length)
        current = (current + length + skipSize) % this.size
        skipSize++
    }
    return Pair(current, skipSize)
}


private fun IntArray.reverseOrder(current: Int, length:Int) {
    var i = 0
    while (i < length/2) {
        this.swap((current+i)%this.size, (current+length-i-1)%this.size)
        i++
    }
}

private fun IntArray.swap(pos1: Int, pos2:Int) {
    val tmp = this[pos1]
    this[pos1] = this[pos2]
    this[pos2] = tmp
}
