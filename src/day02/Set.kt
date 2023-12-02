package day02

class Set(val numberOfRedCubes: Int, val numberOfBlueCubes: Int, val numberOfGreenCubes: Int) {
    fun containsTooManyCubes(maxNumberOfRedCubes: Int, maxNumberOfBlueCubes: Int, maxNumberOfGreenCubes: Int): Boolean {
        return numberOfRedCubes > maxNumberOfRedCubes 
                || numberOfBlueCubes > maxNumberOfBlueCubes
                || numberOfGreenCubes > maxNumberOfGreenCubes
    }
}