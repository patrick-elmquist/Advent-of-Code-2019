import util.Day

// Answer #1: 3334282
// Answer #2: 4998585

fun main(args: Array<String>) {
    Day(n = 1) {
        answer { floats.map { calculateFuel(it) }.sum() }
        answer { floats.map { calculateFuelRecursive(it) }.sum() }
    }
}

private fun calculateFuel(mass: Float): Float = (mass / 3f).toInt() - 2f
private fun calculateFuelRecursive(mass: Float): Float =
    calculateFuel(mass).let { if (it > 0f) it + calculateFuelRecursive(it) else 0f }