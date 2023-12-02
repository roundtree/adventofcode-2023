package day01

import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return input
                .stream()
                .map { s -> s.filter { it.isDigit() } }
                .map { s -> "${s?.first()}${s?.last()}" }
                .mapToInt { s -> Integer.valueOf(s) }
                .sum()
    }

    fun mapWordsToDigits(stringContainingDigitWords: String): String {
        val replacements = mapOf(
                "one" to "1",
                "two" to "2",
                "three" to "3",
                "four" to "4",
                "five" to "5",
                "six" to "6",
                "seven" to "7",
                "eight" to "8",
                "nine" to "9"
        )
        
        var stringToReturn = ""
        stringContainingDigitWords.toCharArray().forEach { char ->
            run {
                stringToReturn += char;

                replacements.entries.stream().forEach { entry -> 
                    run {
                        if (stringToReturn.contains(entry.key)) {
                            stringToReturn = stringToReturn.replace(entry.key, "${entry.value}${entry.key}")
                        }
                    }
                }
            }
        }
        
        return stringToReturn
    }

    fun part2(input: List<String>): Int {
        return input
                .stream()
                .map { mapWordsToDigits(it) }
                .map { s -> s.filter { it.isDigit() } }
                .map { s -> "${s?.first()}${s?.last()}" }
                .mapToInt { s -> Integer.valueOf(s) }
                .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/Day01_test")
    check(part1(testInput) == 142)
    check(part2(testInput) == 281)

    val input = readInput("day01/Day01")
    part1(input).println() // 54927
    part2(input).println() // 54581
}
