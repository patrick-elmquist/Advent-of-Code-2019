import util.Day

// Answer #1: 17321586
// Answer #2:

private const val COLS = 5

fun main() {
    Day(n = 24) {
        answer {
            var state = lines.joinToString(separator = "")
            val states = mutableSetOf<String>()
            while (state !in states) {
                states.add(state)
                state = state.evolve()
            }
            calculateBiodiversity(state)
        }
        answer {
        }
    }
}

private fun calculateBiodiversity(state: String) =
        state.foldIndexed(0) { index, value, c -> value + if (c == '#') 1 shl index else 0 }

private fun String.evolve() = mapIndexed { index, c ->
    val bugCount = countAdjacentBugs(this, index)
    if (bugCount == 1 || bugCount == 2 && c == '.') '#' else '.'
}.joinToString("")

private fun countAdjacentBugs(state: String, index: Int): Int {
    val row = index / COLS
    val column = index % COLS

    val indices = mutableListOf<Int>().apply {
        if (row - 1 >= 0) add((row - 1) * COLS + column) else null
        if (row + 1 < state.length / COLS) add((row + 1) * COLS + column) else null
        if (column - 1 >= 0) add(row * COLS + column - 1) else null
        if (column + 1 < COLS) add(row * COLS + column + 1) else null
    }

    return indices.map { if (state[it] == '#') 1 else 0 }.sum()
}
