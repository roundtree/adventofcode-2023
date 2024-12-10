package nl.roundtree.day08

import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val instructions = input[0].toCharArray().map { if (it == 'L') 0 else 1 }

        val nodeMap = input.subList(2, input.size).associate { line ->
            val split = line.split(" = ")
            split[0] to "[A-Z]{3}".toRegex().findAll(split[1]).map { it.value }.toList()
        }

        var currentValue = nodeMap.getValue("AAA")
        var counter = 0

        while (true) {
            instructions.forEach { instruction ->
                val nextMapToFetch = currentValue[instruction]
                currentValue = nodeMap.getValue(nextMapToFetch)
                counter++

                if (nextMapToFetch == "ZZZ") {
                    return counter
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val instructions = input[0].toCharArray().map { if (it == 'L') 0 else 1 }

        val nodeMap = input.subList(2, input.size).associate { line ->
            val split = line.split(" = ")
            split[0] to "[A-Z0-9]{3}".toRegex().findAll(split[1]).map { it.value }.toList()
        }

        var nodesToMove = nodeMap.filter { it.key.endsWith('A') }
                .map { Node(it.key, it.value) }

        var counter = 0

        while (true) {
            instructions.forEach { instruction ->
                nodesToMove = nodesToMove.map { node ->
                    val nextMap = node.value[instruction]
                    return@map Node(nextMap, nodeMap.getValue(nextMap))
                }

                counter++

                if (nodesToMove.all { it.key.endsWith('Z') }) {
                    return counter
                }
            }
        }
    }

    // test if implementation meets criteria from the description
    check(part1(readInput("day08/Day08_test")) == 6)
//    check(part2(readInput("day08/Day08_test_part2")) == 6)

    val input = readInput("day08/Day08")
    part1(input).println() // 18727
    part2(input).println() // 
}

data class Node(val key: String, val value: List<String>)
