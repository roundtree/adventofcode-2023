package nl.roundtree.day10

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class Day10SolutionsTest {
    
    @Test
    fun testGetAmountOfPathParts() {
        val pipeMap = PuzzleInputReader.readPuzzleInput("Day10_part1_test_puzzleinput")
        
        assertThat(pipeMap.getAmountOfPathParts()).isEqualTo(8)
    }

    @Test
    fun testDay10PuzzleSolutionToPart1() {
        val pipeMap = PuzzleInputReader.readPuzzleInput("Day10_puzzleinput")
        
        assertThat(pipeMap.getAmountOfPathParts()).isEqualTo(7063)
    }
}