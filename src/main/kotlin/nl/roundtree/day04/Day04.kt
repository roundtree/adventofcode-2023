package nl.roundtree.day04

import println
import readInput

fun main() {

    fun getAmountOfMyWinningNumbers(line: String): Int {
        val numberRegex = "\\d+".toRegex()
        val splittedLine = line.split('|')

        val winningNumbers = numberRegex.findAll(splittedLine[0].split(':')[1])
                .map { it.value }
                .toCollection(mutableListOf())

        val myNumbers = numberRegex.findAll(splittedLine[1])
                .map { it.value }
                .toCollection(mutableListOf())

        return winningNumbers.intersect(myNumbers).size
    }

    fun part1(input: List<String>): Int {
        return input.map { line ->
            val amountOfMyWinningNumbers = getAmountOfMyWinningNumbers(line)
            return@map if (amountOfMyWinningNumbers > 0) {
                var points = 1
                for (i in 1..<amountOfMyWinningNumbers) {
                    points *= 2
                }
                points
            } else {
                0
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val countsOfWinningCardsByCardNumber = hashMapOf<Int, Int>()

        input.forEach {
            val cardNumber = Integer.valueOf("\\d+".toRegex().findAll(it).first().value)
            val amountOfMyWinningNumbers = getAmountOfMyWinningNumbers(it)

            if (countsOfWinningCardsByCardNumber.containsKey(cardNumber)) {
                // process winnings of copy cards
                for (i in 0..<countsOfWinningCardsByCardNumber.getValue(cardNumber)) {
                    IntRange(cardNumber + 1, cardNumber + amountOfMyWinningNumbers).forEach { nextWonCardNumber ->
                        countsOfWinningCardsByCardNumber.compute(nextWonCardNumber) { _, v -> if (v == null) 1 else v + 1 }
                    }
                }
            }

            // process original cards + winnings
            IntRange(cardNumber, cardNumber + amountOfMyWinningNumbers).forEach { nextWonCardNumber ->
                countsOfWinningCardsByCardNumber.compute(nextWonCardNumber) { _, v -> if (v == null) 1 else v + 1 }
            }
        }

        return countsOfWinningCardsByCardNumber.values.sum()
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("day04/Day04")
    part1(input).println() // 25183
    part2(input).println() // 5667240
}
