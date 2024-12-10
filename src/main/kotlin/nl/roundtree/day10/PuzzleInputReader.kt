package nl.roundtree.day10

import readInput

class PuzzleInputReader {
    companion object {
        fun readPuzzleInput(fileName: String): PipeMap {
            val input = readInput("Day10/${fileName}");

            val pipePartMap = Array<Array<PipeMap.PipePart?>>(input.size) { Array(input.first().length) { null } }
            val pipeParts = mutableListOf<PipeMap.PipePart>()
            
            input.forEachIndexed { row, line ->
                line.forEachIndexed { column, symbol ->
                    val direction = PipeMap.Type.fromSymbol(symbol)
                    val northPart = getPositionAtIndex(pipePartMap, row - 1, column);
                    val eastPart = getPositionAtIndex(pipePartMap, row, column + 1);
                    val southPart = getPositionAtIndex(pipePartMap, row + 1, column);
                    val westPart = getPositionAtIndex(pipePartMap, row, column - 1);
                    
                    val part = PipeMap.PipePart(direction, row, column, northPart, eastPart, southPart, westPart)
                    pipePartMap[row][column] = part
                    
                    if (northPart != null) {
                        northPart.southPart = part
                    }
                    
                    if (westPart != null) {
                        westPart.eastPart = part
                    }

                    pipeParts.add(part);
                }
            }

            return PipeMap(pipeParts)
        }

        private fun getPositionAtIndex(
            map: Array<Array<PipeMap.PipePart?>>,
            row: Int,
            column: Int
        ): PipeMap.PipePart? {
            return try {
                map[row][column]
            } catch (e: Exception) {
                null
            }
        }
    }
}