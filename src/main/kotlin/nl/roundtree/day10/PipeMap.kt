package nl.roundtree.day10

import nl.roundtree.day10.PipeMap.Type.*

class PipeMap(private val pipeMap: MutableList<PipePart>) {

    fun getAmountOfPathParts(): Int {
        val startPart = pipeMap.first { it.type == UNKNOWN }
        val partsOfLoop = getPipeSymbolsOfLoop(startPart)
        return partsOfLoop.size / 2
    }

    private fun getPipeSymbolsOfLoop(start: PipePart): MutableList<PipePart> {
        val symbolsInLoop = mutableListOf(start)

        var previous = start
        var next = start.getNext(previous)
        while (next != start) {
            val current = next
            symbolsInLoop.add(current)
            next = current.getNext(previous)
            previous = current
        }

        return symbolsInLoop;
    }

    data class PipePart(
        val type: Type,
        val row: Int,
        val colum: Int,
        val northPart: PipePart?,
        var eastPart: PipePart?,
        var southPart: PipePart?,
        val westPart: PipePart?
    ) {
        fun getNext(previous: PipePart): PipePart {
            return when {
                previous != northPart && canMoveToNorth(northPart) -> northPart!!
                previous != eastPart && canMoveToEast(eastPart) -> eastPart!!
                previous != southPart && canMoveToSouth(southPart) -> southPart!!
                previous != westPart && canMoveToWest(westPart) -> westPart!!
                else -> this // should never occur
            }
        }

        private fun canMoveToNorth(other: PipePart?): Boolean {
            return other != null && listOf(VERTICAL, NE_90_DEGREE, NW_90_DEGREE, UNKNOWN).contains(type)
                    && listOf(VERTICAL, SW_90_DEGREE, SE_90_DEGREE, UNKNOWN).contains(other.type)
        }

        private fun canMoveToEast(other: PipePart?): Boolean {
            return other != null && listOf(HORIZONTAL, NE_90_DEGREE, SE_90_DEGREE, UNKNOWN).contains(type)
                    && listOf(HORIZONTAL, NW_90_DEGREE, SW_90_DEGREE, UNKNOWN).contains(other.type)
        }

        private fun canMoveToSouth(other: PipePart?): Boolean {
            return other != null && listOf(VERTICAL, SW_90_DEGREE, SE_90_DEGREE, UNKNOWN).contains(type)
                    && listOf(VERTICAL, NE_90_DEGREE, NW_90_DEGREE, UNKNOWN).contains(other.type)
        }

        private fun canMoveToWest(other: PipePart?): Boolean {
            return other != null && listOf(HORIZONTAL, NW_90_DEGREE, SW_90_DEGREE, UNKNOWN).contains(type)
                    && listOf(HORIZONTAL, NE_90_DEGREE, SE_90_DEGREE, UNKNOWN).contains(other.type)
        }

        override fun toString(): String {
            return "PipePart(type=$type, row=$row, colum=$colum)"
        }
    }

    enum class Type(val symbol: Char) {
        VERTICAL('|'),
        HORIZONTAL('-'),
        NE_90_DEGREE('L'),
        NW_90_DEGREE('J'),
        SW_90_DEGREE('7'),
        SE_90_DEGREE('F'),
        GROUND('.'),
        UNKNOWN('S');

        companion object {
            fun fromSymbol(symbol: Char): Type {
                return entries.find { it.symbol == symbol } ?: UNKNOWN
            }
        }
    }
}