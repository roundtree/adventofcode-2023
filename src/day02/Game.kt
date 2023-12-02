package day02

class Game(val id: Int,
           val sets: List<Set>,
           val maxNumberOfRedCubes: Int = 12,
           val maxNumberOfBlueCubes: Int = 14,
           val maxNumberOfGreenCubes: Int = 13 ) {
    
    fun isGamePossible(): Boolean {
        return sets
                .filter { it.containsTooManyCubes(maxNumberOfRedCubes, maxNumberOfBlueCubes, maxNumberOfGreenCubes) }
                .isEmpty()
    }
    
    fun powerOfFewestRequiredCubesPerGame(): Int {
        val maxNumberOfRedCubes = sets.maxByOrNull { s -> s.numberOfRedCubes }?.numberOfRedCubes ?: 0
        val maxNumberOfBlueCubes = sets.maxByOrNull { s -> s.numberOfBlueCubes }?.numberOfBlueCubes ?: 0
        val maxNumberOfGreenCubes = sets.maxByOrNull { s -> s.numberOfGreenCubes }?.numberOfGreenCubes ?: 0
        
        return maxNumberOfRedCubes * maxNumberOfBlueCubes * maxNumberOfGreenCubes
    }
}