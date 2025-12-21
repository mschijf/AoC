package adventofcode.year2020.day20

class Image(arrangement: Arrangement) {
    private val compressedImage = arrangement.mergeTiles()
    private val seaMonsterPattern = listOf(
        "                  # ",
        "#    ##    ##    ###",
        " #  #  #  #  #  #   ")

    fun getRoughNess(): Int {
        val count = countMonsters()
        if (count == 0)
            return -1
        val hashTagPerMonster = seaMonsterPattern.sumOf { it.count{ch -> ch == '#'}}
        val hashtagPerImage = compressedImage.sumOf { it.count{ch -> ch == '#'}}
        return hashtagPerImage - count * hashTagPerMonster
    }


    private fun countMonsters(): Int {
        var count = 0
        for (row in 1 until compressedImage.size-1) {
            var index = findFirst(seaMonsterPattern[1], compressedImage[row])
            while (index != -1) {
                val patternMatch = startsWith(seaMonsterPattern[0], compressedImage[row-1].substring(index)) &&
                        startsWith(seaMonsterPattern[2],compressedImage[row+1].substring(index))

                if (patternMatch) {
                    count++
                }
                index = findFirst(seaMonsterPattern[1], compressedImage[row], index+1)
            }
        }
        return count
    }

    private fun findFirst(needle: String, hayStack: String, startIndex: Int=0): Int {
        var index = startIndex
        while (index + needle.length < hayStack.length) {
            if (startsWith(needle, hayStack.substring(index)))
                return index
            index++
        }
        return -1
    }

    private fun startsWith(needle: String, hayStack: String): Boolean {
        if (needle.length > hayStack.length)
            return false
        for (index in needle.indices) {
            if (needle[index] == '#' && hayStack[index] != '#')
                return false
        }
        return true
    }
}