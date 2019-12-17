import util.Day
import kotlin.math.abs
import kotlin.math.ceil

// Answer #1: 74369033
// Answer #2: 19903864

private val basePattern = listOf(0, 1, 0, -1)

fun main() {
    Day(n = 16) {
        answer { generateFftSequence(lines.first(), basePattern).take(100).last().take(8) }
        answer { answer2(lines.first()).take(8).joinToString("") }
    }
}

private fun answer2(input: String): List<Int> {
    val len = input.length * 10000 - input.take(7).toInt()
    val start = (0 until 10000).joinToString("") { input }.takeLast(len).map { Character.getNumericValue(it) }.reversed()
    return (0 until 100)
        .fold(start) { out, _ -> someSequence(out).last() }
        .reversed()
}

private fun someSequence(input: List<Int>) =
    generateSequence(mutableListOf<Int>()) { output ->
        if (output.size == input.size) return@generateSequence null
        else output.apply { add(((output.getOrNull(output.size - 1) ?: 0) + input[output.size]) % 10) }
    }

private fun generateFftSequence(input: String, base: List<Int>) =
    generateSequence(applyFft(input, base)) {
        applyFft(it, base)
    }

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
    return (0 until repeats).flatMap { pattern }
        .drop(1)
}