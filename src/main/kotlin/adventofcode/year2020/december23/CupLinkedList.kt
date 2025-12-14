package adventofcode.year2020.december23

class CupLinkedList(input: String, extraMillion: Boolean=false) {

    private var current: Cup
    private val cupListByNumber = List(if (extraMillion) 1_000_000+1 else input.length+1) {Cup(it)}

    init {
        current = cupListByNumber[input.first().digitToInt()]
        var loop = current
        input.drop(1).forEach {ch ->
            loop.nextCup = cupListByNumber[ch.digitToInt()]
            loop = cupListByNumber[ch.digitToInt()]
        }
        if (extraMillion) {
            val start = input.length+1
            for (v in start..1_000_000) {
                loop.nextCup = cupListByNumber[v]
                loop = cupListByNumber[v]
            }
        }
        loop.nextCup = current
    }

    inner class Cup(val number: Int){
        var nextCup: Cup? = null
    }

    fun move() {

        //determine pickup nodes
        val pickUpBegin = current.nextCup!!
        val pickUpMid = pickUpBegin.nextCup!!
        val pickUpEnd = pickUpMid.nextCup!!

        val destination = findDestination(setOf(pickUpBegin.number, pickUpMid.number, pickUpEnd.number))

        // remove cups
        current.nextCup = pickUpEnd.nextCup

        // add after destination
        pickUpEnd.nextCup = destination.nextCup
        destination.nextCup = pickUpBegin

        current = current.nextCup!!
    }

    private fun findDestination(pickupValueSet: Set<Int>): Cup {
        val lowestValue = 1
        val highestValue = cupListByNumber.size-1

        var destinationValue = if (current.number == lowestValue) highestValue else current.number - 1
        while (destinationValue in pickupValueSet) {
            destinationValue = if (destinationValue == lowestValue) highestValue else destinationValue - 1
        }

        return cupListByNumber[destinationValue]
    }

    fun labelsAfterOne(): String {
        var result = ""
        var loop = cupListByNumber[1].nextCup!!
        while (loop != cupListByNumber[1]) {
            result = "$result${loop.number}"
            loop = loop.nextCup!!
        }
        return result
    }

    fun twoCupsProductAfterCupOne(): Long {
        val firstCup = cupListByNumber[1].nextCup!!
        val secondCup = firstCup.nextCup!!
        return firstCup.number * secondCup.number.toLong()
    }

    override fun toString(): String {
        var result = "(${current.number})"
        var loop = current.nextCup!!
        while (loop != current) {
            result = "$result ${loop.number}"
            loop = loop.nextCup!!
        }
        return result
    }

}