import extension.answer

fun main(args: Array<String>) {
    with(Data(day = 1)) {
        answer(1) { floats.map { calculateFuel(it) }.sum() }
        answer(2) { floats.map { calculateFuelRecursive(it) }.sum() }
    }
}

private fun calculateFuel(mass: Float): Float = (mass / 3f).toInt() - 2f
private fun calculateFuelRecursive(mass: Float): Float =
    calculateFuel(mass).let { if (it > 0f) it + calculateFuelRecursive(it) else 0f }