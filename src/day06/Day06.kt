package day06

import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val times = "\\d+".toRegex().findAll(input[0]).map { it.value.toLong() }.toList()
        val recordDistances = "\\d+".toRegex().findAll(input[1]).map { it.value.toLong() }.toList()
        val races = times.withIndex().map { Race(it.value, recordDistances[it.index]) }.toList()
        
        return races.map { it.getNumberOfWinningStrategies() }.reduce(Int::times)
    }

    fun part2(input: List<String>): Int {
        val time = "\\d+".toRegex().findAll(input[0]).map { it.value }.joinToString("").toLong()
        val recordDistance = "\\d+".toRegex().findAll(input[1]).map { it.value }.joinToString("").toLong()
        return Race(time, recordDistance).getNumberOfWinningStrategies()
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("day06/Day06")
    part1(input).println() // 114400
    part2(input).println() // 21039729
}

class Race(private val time: Long, private val recordDistance: Long) {
    
    fun getNumberOfWinningStrategies(): Int {
        var numberOfWinningStrategies = 0
        
        for (holdButtonTime in 0..time) {
            val raceDistance = (time - holdButtonTime) * holdButtonTime
            if (raceDistance > recordDistance) {
                numberOfWinningStrategies++
            }
        }
        
        return numberOfWinningStrategies
    }
}
