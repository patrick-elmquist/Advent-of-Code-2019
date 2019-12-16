import util.Day
import kotlin.math.abs
import kotlin.math.ceil

// Answer #1: 74369033
// Answer #2:

fun main() {
    Day(n = 16) {

        answer {
            generateFftSequence(lines.first(), listOf(0, 1, 0, -1)).take(100).last().take(8)
        }
        answer {
            "Not implemented"
        }
    }
}

private fun generateFftSequence(input: String, base: List<Int>) =
    generateSequence(applyFft(input, base)) { applyFft(it, base) }

private fun applyFft(input: String, base: List<Int>): String {
    return input.indices
        .map { n ->
            val pattern = generatePattern(base, n, input.length)
            input.map { Character.getNumericValue(it) }
                .mapIndexed { index, i -> i * pattern[index] }
                .sum()
                .let { abs(it) % 10 }
        }.joinToString("")
}

private fun generatePattern(base: List<Int>, n: Int, len: Int): List<Int> {
    val oneCycleLength = base.size * (n + 1) - 1
    val repeats = ceil(len / oneCycleLength.toFloat()).toInt()
    val pattern = base.flatMap { digit -> (0..n).map { digit } }
    return (0 until repeats).flatMap { pattern }.drop(1)
}