package adventofcode.year2020.december23

class CupList(input: String) {
    private val cups = input.toList().map {it.digitToInt()}.toMutableList()
    private var currentIndex = 0

    fun addUpToOneMillion() {
        val start = cups.size
        for (v in start .. 1_000_000) {
            cups.add(v)
        }
    }

    fun move() {
        val current = cups[currentIndex]
        val pickUp = listOf(cups[(currentIndex+1) % cups.size], cups[(currentIndex+2) % cups.size], cups[(currentIndex+3) % cups.size])
        val destinationIndex = findDestinationIndex(current, pickUp)
        val destination = cups[destinationIndex]

        cups.remove(pickUp[0])
        cups.remove(pickUp[1])
        cups.remove(pickUp[2])

        val afterIndex = cups.indexOf(destination)
        cups.add(afterIndex+1, pickUp[2])
        cups.add(afterIndex+1, pickUp[1])
        cups.add(afterIndex+1, pickUp[0])

        currentIndex = (cups.indexOf(current)+1) % cups.size
    }

    private fun findDestinationIndex(current: Int, pickUp: List<Int>): Int {
        val lowestValue = cups.min()
        val highestValue = cups.max()

        var destinationValue = if (current == lowestValue) highestValue else current - 1

        var i = (currentIndex + 1) % cups.size
        while (true) {
            if (cups[i] == destinationValue) {
                if (cups[i] in pickUp) {
                    destinationValue = if (destinationValue == lowestValue) highestValue else destinationValue - 1
                } else {
                    return i
                }
            }
            i = (i+1) % cups.size
        }
    }

    fun labelsAfterOne(): String {
        var result = ""
        var index = cups.indexOf(1)
        repeat(cups.size){
            result = result + cups[index]
            index = (index + 1) % cups.size
        }
        return result.drop(1)
    }

    override fun toString() = cups.joinToString(" ") { if (it == cups[currentIndex]) "($it)" else "$it" }

}
