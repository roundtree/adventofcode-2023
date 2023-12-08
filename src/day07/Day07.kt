package day07

import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.map { line ->
            val split = line.split(" ")
            return@map Hand(split[0], split[1].toInt())
        }.sortedDescending()
                .withIndex()
                .sumOf { (it.index + 1) * it.value.bid }
    }

    fun part2(input: List<String>): Int {
        return input.map { line ->
            val split = line.split(" ")
            return@map JokerHand(split[0], split[1].toInt())

        }.sortedDescending()
                .withIndex()
                .sumOf { (it.index + 1) * it.value.bid }

    }

    // test if implementation meets criteria from the description
    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("day07/Day07")
    part1(input).println() // 246163188
    part2(input).println() // 245794069
}

open class Hand(private val cards: String,
                    val bid: Int,
                    val cardRankingOrder: String = "AKQJT98765432") : Comparable<Hand> {

    open val type: Type = determineType(cards)

    fun determineType(cards: String): Type {
        return when {
            "^(.)\\1*\$".toRegex().matches(cards) -> Type.FIVE_OF_A_KIND
            "(.)\\1{3}".toRegex().containsMatchIn(sortCards(cards)) -> Type.FOUR_OF_A_KIND
            "^(.)\\1{2}(.)\\2|(.)\\3(.)\\4{2}\$".toRegex().matches(sortCards(cards)) -> Type.FULL_HOUSE
            "(.)\\1{2}".toRegex().containsMatchIn(sortCards(cards)) -> Type.THREE_OF_A_KIND
            "(.)\\1(.)\\2|(.)\\3[A-Z0-9](.)\\4".toRegex().containsMatchIn(sortCards(cards)) -> Type.TWO_PAIR
            "(.)\\1".toRegex().containsMatchIn(sortCards(cards)) -> Type.ONE_PAIR
            else -> Type.HIGH_CARD
        }
    }

    private fun sortCards(cards: String): String {
        return cards.toCharArray().sorted().joinToString("")
    }

    override fun compareTo(other: Hand): Int {
        if (this.type.ordinal == other.type.ordinal) {
            val thisCards = this.cards.toCharArray()
            val otherCards = other.cards.toCharArray()

            for (i in thisCards.indices) {
                val currentThisCard = thisCards[i]
                val currentOtherCard = otherCards[i]

                if (currentThisCard != currentOtherCard) {
                    return cardRankingOrder.indexOf(currentThisCard).compareTo(cardRankingOrder.indexOf(currentOtherCard))
                }
            }

            return 0
        } else {
            return this.type.ordinal.compareTo(other.type.ordinal)
        }
    }
}

class JokerHand(private val cards: String, 
                bid: Int) : Hand(cards, bid, "AKQT98765432J") {

    override val type = replaceJokersAnDetermineType()

    fun replaceJokersAnDetermineType(): Type {
        var jokerReplacedHandString = cards

        if (jokerReplacedHandString.contains('J')) {
            val charactersWithCount = mutableMapOf<String, Int>()
            jokerReplacedHandString.filter { it != 'J' }.forEach { character ->
                charactersWithCount.compute(character.toString()) { _, v -> if (v == null) 1 else v + 1 }
            }

            jokerReplacedHandString = jokerReplacedHandString.replace("J", charactersWithCount.maxByOrNull { v -> v.value }?.key
                    ?: "J")
        }
        
        return super.determineType(jokerReplacedHandString)
    }
}

enum class Type {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}
