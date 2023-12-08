package day05

import println
import readInput

fun main() {
    fun readMap(name: String, input: List<String>, nextMap: AlmanacMap? = null): AlmanacMap {

        val startIndex = input.withIndex()
                .filter { name.toRegex().containsMatchIn(it.value) }
                .map { it.index }
                .first()

        val endIndex = input.withIndex()
                .filter { "^\\s*$".toRegex().containsMatchIn(it.value) }
                .map { it.index }
                .firstOrNull { it > startIndex } ?: (input.lastIndex + 1)

        val almanacMapEntries = input.subList(startIndex + 1, endIndex)
                .map { line ->
                    val numbers = "\\d+".toRegex().findAll(line).map { (it.value).toLong() }.toList()
                    return@map AlmanacMapEntry(numbers[0], numbers[1], numbers[2])
                }

        return AlmanacMap(almanacMapEntries, nextMap)
    }
    
    fun createMapStructure(input: List<String>): AlmanacMap {
        val humidityToLocationMap = readMap("humidity-to-location map", input)
        val temperatureToHumidityMap = readMap("temperature-to-humidity map", input, humidityToLocationMap)
        val lightToTemperatureMap = readMap("light-to-temperature map", input, temperatureToHumidityMap)
        val waterToLightMap = readMap("water-to-light map", input, lightToTemperatureMap)
        val fertilizerToWaterMap = readMap("fertilizer-to-water map", input, waterToLightMap)
        val soilToFertilizerMap = readMap("soil-to-fertilizer map", input, fertilizerToWaterMap)
        return readMap("seed-to-soil map", input, soilToFertilizerMap)
    }

    fun part1(input: List<String>): Long {
        val seedNumbers = "\\d+".toRegex().findAll(input[0]).map { (it.value.toLong()) }.toList()
        val seedToSoilMap = createMapStructure(input)
        return seedNumbers.minOf { seedToSoilMap.getFinalDestination(it) }
    }

    fun part2(input: List<String>): Long {
        val seedPairs = "\\d+".toRegex().findAll(input[0]).map { (it.value.toLong()) }
                .withIndex()
                .groupBy { it.index / 2 }
                .map { pair -> pair.value.map { it.value } }

        val seedToSoilMap = createMapStructure(input)

        var lowestLocation = Long.MAX_VALUE

        seedPairs.forEach { seedPair ->
            val seedRangeStart = seedPair[0]
            val length = seedPair[1]

            for (seed in seedRangeStart ..< seedRangeStart + length) {
                val location = seedToSoilMap.getFinalDestination(seed)
                if (location < lowestLocation) {
                    lowestLocation = location
                }
            }
        }

        return lowestLocation
    }

    // test if implementation meets criteria from the description
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("day05/Day05")
    part1(input).println() // 650599855
    part2(input).println() // 1240035
}

class AlmanacMap(private val entries: List<AlmanacMapEntry>, private val nextMap: AlmanacMap?) {

    private fun getDestination(source: Long): Long {
        return entries.firstNotNullOfOrNull { it.getDestination(source) } ?: source
    }

    fun getFinalDestination(source: Long): Long {
        var currentMap: AlmanacMap? = this
        var destination: Long = source

        do {
            destination = currentMap!!.getDestination(destination)
            currentMap = currentMap.nextMap
        } while (currentMap != null)

        return destination
    }

    override fun toString(): String {
        return "AlmanacMap(entries=$entries)"
    }
}

class AlmanacMapEntry(private val destination: Long, private val source: Long, private val range: Long) {

    fun getDestination(sourceToCheck: Long): Long? {
        if (sourceToCheck >= source && sourceToCheck < source + range) {
            val indexInSource = sourceToCheck - source

            val maxDest = destination + range
            if (indexInSource < maxDest) {
                return destination + indexInSource
            }
        }

        return null
    }

    override fun toString(): String {
        return "AlmanacMapEntry(destination=$destination, source=$source, range=$range)"
    }
}