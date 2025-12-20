package adventofcode.year2018.december07

import adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val simpleInput = inputLines.map{ it.split(" ").run{this[1] to this[7]}}

    override fun resultPartOne(): Any {
        val steps = (simpleInput.map{it.first} + simpleInput.map{it.second}).distinct().associateWith{letter -> Step(letter)}
        simpleInput.forEach {
            steps[it.second]!!.addDependency(steps[it.first]!!)
        }

        var result = ""
        while (steps.values.any { !it.hasBeenExecuted() }) {
            val nextStep = steps.values.filter { it.canBeDone() }.sortedBy { it.name }
            result = "$result${nextStep.first().name}"
            nextStep.first().execute()
        }
        return result
    }

    override fun resultPartTwo(): Any {
        var totalTime = 0
        val basicTime = if (test) 0 else 60
        val steps = (simpleInput.map{it.first} + simpleInput.map{it.second}).distinct().associateWith{letter -> Step(letter, basicTime)}
        simpleInput.forEach {
            steps[it.second]!!.addDependency(steps[it.first]!!)
        }

        val maxWorkers = if (test) 2 else 5
        val workers = mutableListOf<Step>()
        while (steps.values.any { !it.hasBeenExecuted() }) {
            val nextSteps = steps.values.filter { it.canBeDone() }.sortedBy { it.name }
            for (step in nextSteps) {
                if (workers.size < maxWorkers) {
                    workers.add(step)
                }
            }
            val executionTime = workers.minOf { it.timeToFinish() }
            workers.forEach { w -> w.execute(executionTime) }
            workers.removeIf { w -> w.hasBeenExecuted() }

            totalTime += executionTime
        }
        return totalTime
    }

    class Step(val name: String, basicTime: Int = 0) {
        private val dependencyList = mutableListOf<Step>()
        private var done = false

        private var timeRun: Int = 0
        private val timeNeeded = (name[0] - 'A' + 1) + basicTime

        fun canBeDone() = !done && timeRun == 0 && dependencyList.all{it.done}
        fun hasBeenExecuted() = done
        fun timeToFinish() = timeNeeded - timeRun

        fun execute(timePassed: Int=timeNeeded) {
            timeRun += timePassed
            if (timeRun >= timeNeeded)
                done = true
        }

        fun addDependency(otherStep: Step) {
            dependencyList.add(otherStep)
        }

        override fun toString(): String {
            return "@$name: $dependencyList"
        }
    }
}


