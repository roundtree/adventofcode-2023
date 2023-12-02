package day02

import println
import readInput

fun main() {

    fun readGames(input: List<String>): List<Game> {
        val games = input.map { line ->
            val split = line.split(":")
            val roundNumber = Integer.valueOf(split[0].filter { it.isDigit() })

            val roundStrings = split[1].split(";")

            val rounds = roundStrings.map { roundString ->
                val numberOfCubesInRoundPerColor = roundString.split(", ")
                var numberOfBlueCubes = 0
                var numberOfRedCubes = 0
                var numberOfGreenCubes = 0

                numberOfCubesInRoundPerColor.forEach { color ->
                    val amount = Integer.valueOf(color.filter { it.isDigit() })
                    when {
                        color.contains("red") -> numberOfRedCubes = amount
                        color.contains("blue") -> numberOfBlueCubes = amount
                        color.contains("green") -> numberOfGreenCubes = amount
                    }
                }

                return@map Set(numberOfRedCubes, numberOfBlueCubes, numberOfGreenCubes)
            }

            return@map Game(roundNumber, rounds)
        }
        return games
    }

    fun part1(input: List<String>): Int {
        val games = readGames(input)
        return games.filter { it.isGamePossible() }.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        val games = readGames(input)
        return games.sumOf { it.powerOfFewestRequiredCubesPerGame() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("day02/Day02")
    part1(input).println() // 1931
    part2(input).println() // 83105
}
