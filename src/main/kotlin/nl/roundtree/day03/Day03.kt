package nl.roundtree.day03

import println
import readInput

fun main() {

    fun lineHasNoDotInRange(line: String, range: IntRange) : Boolean {
        val rangeStart = if (range.first > 0) range.first - 1 else range.first
        val rangeEnd = if (range.last < line.length - 1) range.last + 1 else range.last
        return "[^.]".toRegex().containsMatchIn(line.substring(rangeStart, rangeEnd + 1))
    }

    fun part1(input: List<String>): Int {
        var total = 0;
        for (i in input.indices) {
            val currentLine = input[i]
            val regex = "\\d+".toRegex()
            val matches = regex.findAll(currentLine)
            total += matches.map { matchResult ->
                val range = matchResult.range
                val number = Integer.valueOf(matchResult.value)
                
                return@map when {
                    range.first > 0 && currentLine[range.first - 1] != '.' -> number
                    range.last < currentLine.length - 1 && currentLine[range.last + 1] != '.' -> number
                    // check previous line
                    i > 0 && lineHasNoDotInRange(input[i - 1], range,) -> number
                    // check next line
                    i < input.size - 1 && lineHasNoDotInRange(input[i + 1], range) -> number
                    else -> 0
                }
            }.sum()
        }

        return total
    }


    fun findGearNumbersOnLine(line: String, index: Int): Sequence<Int> {
        val allowedRange = IntRange(index - 1, index + 1)
        return "\\d+".toRegex().findAll(line)
                .filter { it.range.intersect(allowedRange).isNotEmpty() }
                .map { Integer.valueOf(it.value) }
    }

    fun part2(input: List<String>): Int {
        var total = 0;
        for (i in input.indices) {
            val currentLine = input[i]
            val matches = "\\*".toRegex().findAll(currentLine)
            total += matches.map { matchResult ->
                val partNumbers = mutableListOf <Int>()
                val index = matchResult.range.first
                
                if (index > 0 && currentLine[index - 1].isDigit()) {
                    // extract part number from before the *
                    partNumbers.add(Integer.valueOf("\\d+".toRegex().findAll(currentLine.substring(0, index)).last().value))
                }
                
                if (index < currentLine.length - 1 && currentLine[index + 1].isDigit()) {
                    // extract part number from after the *
                    partNumbers.add(Integer.valueOf("\\d+".toRegex().findAll(currentLine.substring(index, currentLine.length)).first().value))
                }
                
                if (i > 0) {
                    // check previous line
                    findGearNumbersOnLine(input[i - 1], index).toCollection(partNumbers)
                }

                if (i < input.size - 1) {
                    // check next line
                    findGearNumbersOnLine(input[i + 1], index).toCollection(partNumbers)
                }
                
                if (partNumbers.size == 2) {
                    return@map partNumbers[0] * partNumbers[1]
                }
                
                return@map 0
            }.sum()
        }
        
        return total
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("day03/Day03")
    part1(input).println() // 540212
    part2(input).println() // 87605697
}
