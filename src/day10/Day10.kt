package day10

import println
import readInput

fun main() {

    fun createPipeMap(input: List<String>): PipeMap {
        val characterMap = mutableListOf<CharArray>()
        input.forEach { characterMap.add(it.toCharArray()) }

        val pipePartMap = mutableListOf<MutableList<PipePart>>()

        for (i in characterMap.indices) {
            val currentRow = mutableListOf<PipePart>()
            pipePartMap.add(currentRow)

            for (j in characterMap[i].indices) {
                currentRow.add(PipePart.createPipePartFromSymbol(characterMap[i][j], i, j))
            }
        }

        return PipeMap(pipePartMap)
    }
    
    fun getPathParts(pipeMap: PipeMap): List<PipePart> {
        val startingPart = pipeMap.getStartingPart()
        var currentPart = startingPart
        var previousPart = startingPart

        val pipeParts = mutableListOf(startingPart)

        do {
            val nextPart = pipeMap.getNextPart(currentPart, previousPart)
            previousPart = currentPart
            currentPart = nextPart
            pipeParts.add(nextPart)
        } while (currentPart != startingPart)
        
        return pipeParts
    }

    fun part1(input: List<String>): Int {
        val pipeMap = createPipeMap(input)

        return getPathParts(pipeMap).size / 2
    }

    fun part2(input: List<String>): Int {
        val pipeMap = createPipeMap(input)
        pipeMap.markPathParts(getPathParts(pipeMap))

        val partsEnclosedByPath = pipeMap.getPartsEnclosedByPath()

        pipeMap.pipeMap.forEach { row ->
            var currentLineStr = ""
            row.forEach { part ->
                if (part.isPath) {
                    currentLineStr += "\u001b[31m" + part.symbol + "\u001b[0m"
                } else if (partsEnclosedByPath.contains(part)) {
                    currentLineStr += "\u001b[93m" + part.symbol + "\u001b[0m"
                } else {
                    currentLineStr += part.symbol
                }
            }

            println(currentLineStr)
        }
        
        return partsEnclosedByPath.size
    }

    // test if implementation meets criteria from the description
    check(part1(readInput("day10/Day10_test")) == 8)
    check(part2(readInput("day10/Day10_test_part2")) == 4)
    
    val input = readInput("day10/Day10")
    part1(input).println() // 7063
    part2(input).println() // 910 - incorrect
}

class PipeMap(val pipeMap: MutableList<MutableList<PipePart>>) {
    
    private lateinit var pathParts: List<PipePart>
    
    fun getStartingPart(): PipePart {

        pipeMap.forEach { row ->
            row.forEach { column ->
                if (column is StartingPositionPipePart) {
                    return column
                }
            }
        }

        throw Exception("Starting part not found")
    }

    fun getNextPart(pipePart: PipePart, previousPipePart: PipePart): PipePart {
        val y = pipePart.yIndex
        val x = pipePart.xIndex

        val leftPart = getPartAt(y, x - 1)
        val upperPart = getPartAt(y - 1, x)
        val rightPart = getPartAt(y, x + 1)
        val lowerPart = getPartAt(y + 1, x)

        listOf(leftPart, upperPart, rightPart, lowerPart).filter { it != previousPipePart }.forEach {
            if (it?.canBeMovedToFrom(pipePart) == true) {
                return it
            }
        }

        throw Exception("Nowhere to go from this part")
    }

    private fun getPartAt(y: Int, x: Int): PipePart? {
        return try {
            pipeMap[y][x]
        } catch (e: Exception) {
            null
        }
    }

    fun markPathParts(pathParts: List<PipePart>) {
        pipeMap.forEach { row ->
            row.forEach { part ->
                if (pathParts.contains(part)) {
                    part.isPath = true
                }
            }
        }
        
        this.pathParts = pathParts
    }

    fun getPartsEnclosedByPath(): List<PipePart> {
        val minX = pathParts.map { it.xIndex }.min()
        val maxX = pathParts.map { it.xIndex }.max()
        val minY = pathParts.map { it.yIndex }.min()
        val maxY = pathParts.map { it.yIndex }.max()
        
        val partsEnclosedByPath = mutableListOf<PipePart>()
        pipeMap.subList(minY, maxY + 1).forEach { row ->
            row.subList(minX, maxX + 1).filter { !it.isPath }.forEach { part ->
                val pathPartsSameY = pathParts.filter { it.yIndex == part.yIndex }
                val pathPartsSameX = pathParts.filter { it.xIndex == part.xIndex }
                
                val enclosedOnY = pathPartsSameY.size > 1 && pathPartsSameY.any { it.xIndex < part.xIndex } && pathPartsSameY.any { it.xIndex > part.xIndex }
                val enclosedOnX = pathPartsSameX.size > 1 && pathPartsSameX.any { it.yIndex < part.yIndex } && pathPartsSameX.any { it.yIndex > part.yIndex }
                if (enclosedOnY && enclosedOnX) {
                    partsEnclosedByPath.add(part)
                }
            }
        }
        
        return partsEnclosedByPath;
    }
}

abstract class PipePart(val symbol: Char,
                        val yIndex: Int,
                        val xIndex: Int) {
    
    var isPath = false
        get() = field

    companion object {
        fun createPipePartFromSymbol(symbol: Char,

                                     yIndex: Int,
                                     xIndex: Int): PipePart {
            return when (symbol) {
                '|' -> VerticalPipePart(symbol, yIndex, xIndex)
                '-' -> HorizontalPipePart(symbol, yIndex, xIndex)
                'L' -> NorthEast90DegreeBendPipePart(symbol, yIndex, xIndex)
                'J' -> NorthWest90DegreeBendPipePart(symbol, yIndex, xIndex)
                '7' -> SouthWest90DegreeBendPipePart(symbol, yIndex, xIndex)
                'F' -> SouthEast90DegreeBendPipePart(symbol, yIndex, xIndex)
                'S' -> StartingPositionPipePart(symbol, yIndex, xIndex)
                else -> BlockedPipePart(symbol, yIndex, xIndex)
            }
        }
    }

    abstract fun canBeMovedToFrom(other: PipePart): Boolean;

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PipePart

        if (yIndex != other.yIndex) return false
        return xIndex == other.xIndex
    }

    override fun hashCode(): Int {
        var result = yIndex
        result = 31 * result + xIndex
        return result
    }

    override fun toString(): String {
        return "PipePart(symbol= $symbol, yIndex=$yIndex, xIndex=$xIndex)"
    }
}

class VerticalPipePart(symbol: Char,
                       yIndex: Int,
                       xIndex: Int) : PipePart(symbol, yIndex, xIndex) {
    override fun canBeMovedToFrom(other: PipePart): Boolean {
        return ((other is NorthEast90DegreeBendPipePart || other is NorthWest90DegreeBendPipePart) && other.yIndex > yIndex)
                || ((other is SouthWest90DegreeBendPipePart || other is SouthEast90DegreeBendPipePart) && other.yIndex < yIndex)
                || (other is VerticalPipePart && other.xIndex == xIndex)
                || other is StartingPositionPipePart
    }
}

class HorizontalPipePart(symbol: Char,
                         yIndex: Int,
                         xIndex: Int) : PipePart(symbol, yIndex, xIndex) {
    override fun canBeMovedToFrom(other: PipePart): Boolean {
        return ((other is SouthEast90DegreeBendPipePart || other is NorthEast90DegreeBendPipePart) && other.xIndex < xIndex)
                || ((other is SouthWest90DegreeBendPipePart || other is NorthWest90DegreeBendPipePart) && other.xIndex > xIndex)
                || (other is HorizontalPipePart && other.yIndex == yIndex)
                || other is StartingPositionPipePart
    }
}

class NorthEast90DegreeBendPipePart(symbol: Char,
                                    yIndex: Int,
                                    xIndex: Int) : PipePart(symbol, yIndex, xIndex) {
    override fun canBeMovedToFrom(other: PipePart): Boolean {
        return ((other is HorizontalPipePart || other is NorthWest90DegreeBendPipePart || other is SouthWest90DegreeBendPipePart) && other.xIndex > xIndex)
                || ((other is VerticalPipePart || other is SouthWest90DegreeBendPipePart || other is SouthEast90DegreeBendPipePart) && other.yIndex < yIndex)
                || other is StartingPositionPipePart
    }
}

class NorthWest90DegreeBendPipePart(symbol: Char,
                                    yIndex: Int,
                                    xIndex: Int) : PipePart(symbol, yIndex, xIndex) {
    override fun canBeMovedToFrom(other: PipePart): Boolean {
        return ((other is HorizontalPipePart || other is NorthEast90DegreeBendPipePart || other is SouthEast90DegreeBendPipePart) && other.xIndex < xIndex)
                || ((other is VerticalPipePart || other is SouthWest90DegreeBendPipePart || other is SouthEast90DegreeBendPipePart) && other.yIndex < yIndex)
                || other is StartingPositionPipePart
    }
}

class SouthWest90DegreeBendPipePart(symbol: Char,
                                    yIndex: Int,
                                    xIndex: Int) : PipePart(symbol, yIndex, xIndex) {
    override fun canBeMovedToFrom(other: PipePart): Boolean {
        return ((other is HorizontalPipePart || other is SouthEast90DegreeBendPipePart || other is NorthEast90DegreeBendPipePart) && other.xIndex < xIndex)
                || ((other is VerticalPipePart || other is NorthWest90DegreeBendPipePart || other is NorthEast90DegreeBendPipePart) && other.yIndex > yIndex)
                || other is StartingPositionPipePart
    }
}

class SouthEast90DegreeBendPipePart(symbol: Char,
                                    yIndex: Int,
                                    xIndex: Int) : PipePart(symbol, yIndex, xIndex) {
    override fun canBeMovedToFrom(other: PipePart): Boolean {
        return ((other is HorizontalPipePart || other is NorthWest90DegreeBendPipePart || other is SouthWest90DegreeBendPipePart) && other.xIndex > xIndex)
                || ((other is VerticalPipePart || other is NorthEast90DegreeBendPipePart || other is NorthWest90DegreeBendPipePart) && other.yIndex > yIndex)
                || other is StartingPositionPipePart
    }

}

class StartingPositionPipePart(symbol: Char,
                               yIndex: Int,
                               xIndex: Int) : PipePart(symbol, yIndex, xIndex) {
    override fun canBeMovedToFrom(other: PipePart): Boolean {
        return ((other is HorizontalPipePart || other is NorthEast90DegreeBendPipePart || other is SouthEast90DegreeBendPipePart) && other.xIndex < xIndex)
                || ((other is HorizontalPipePart || other is SouthWest90DegreeBendPipePart || other is NorthWest90DegreeBendPipePart) && other.xIndex > xIndex)
                || ((other is VerticalPipePart || other is SouthEast90DegreeBendPipePart || other is SouthWest90DegreeBendPipePart) && other.yIndex < yIndex)
                || ((other is VerticalPipePart || other is NorthWest90DegreeBendPipePart || other is NorthEast90DegreeBendPipePart) && other.yIndex > yIndex)
    }
}

class BlockedPipePart(symbol: Char,
                      yIndex: Int,
                      xIndex: Int) : PipePart(symbol, yIndex, xIndex) {
    override fun canBeMovedToFrom(other: PipePart): Boolean {
        return false
    }
}
