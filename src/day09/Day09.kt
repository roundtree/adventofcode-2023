package day09

import println
import readInput

fun main() {

    fun extractNumberSequences(it: String): MutableList<List<Int>> {
        val numbers = "-?\\d+".toRegex().findAll(it).map { it.value.toInt() }.toList()
        val numberSequences = mutableListOf(numbers)

        var found = false
        var currentNumberSequence = numbers
        while (!found) {
            val newNumberSequence = mutableListOf<Int>()
            numberSequences.add(newNumberSequence)

            for (i in 0..<currentNumberSequence.size - 1) {
                newNumberSequence.add(currentNumberSequence[i + 1] - currentNumberSequence[i])
            }

            currentNumberSequence = newNumberSequence
            found = currentNumberSequence.all { number -> number == 0 }
        }
        return numberSequences
    }

    fun part1(input: List<String>): Int {
        return input.map { 
            val numberSequences = extractNumberSequences(it)

            var finalNumber = numberSequences.last().last()
            for(i in numberSequences.size - 2 downTo 0) {
                finalNumber += numberSequences[i].last()
            }
            
            return@map finalNumber
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            val numberSequences = extractNumberSequences(it)

            var finalNumber = numberSequences.last().first()
            for(i in numberSequences.size - 2 downTo 0) {
                finalNumber = numberSequences[i].first() - finalNumber
            }

            return@map finalNumber
        }.sum()
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("day09/Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("day09/Day09")
    part1(input).println() // 1930746032
    part2(input).println() // 1154
}

