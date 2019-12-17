import util.Day
import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.ceil

// Answer #1: 74369033
// Answer #2:

private val basePattern = listOf(0, 1, 0, -1)

fun main() {
    Day(n = 16) {

        answer("00000000000012345678") {
            generateFftSequence(lines.first(), basePattern).take(1).last().take(8)
        }
        answer("03081770884921959731165446850517") {
            println(answer2(lines.first()).take(8))
            //answer2(lines.first(), basePattern)
        }
    }
}

private fun answer2(input: String): String {
    val inputSize = input.length * 10000
    val skip = input.take(7).toInt()
    val range = (inputSize - 1) downTo (skip + 1)
    println("size:$inputSize skip:$skip range:$range")
    val len = inputSize - skip
    var put = (0 until 10000).map { input }.joinToString("").takeLast(len)
    for (i in 0 until 100) {
        val seq = someSequence(put).last()
        put = seq.second.second.toString().reversed()
    }
    return put
}

private fun someSequence(input: String) =
    generateSequence(input to (0 to StringBuilder())) { (input, output) ->
        val sum = output.first
        val out = output.second
        if (input.isEmpty()) {
            return@generateSequence null
        }

        val char = Character.getNumericValue(input.last())
        val newInput = input.dropLast(1)
        val newSum = ((sum + char) % 10) // .also { println("in:$char out:$it") }
        out.append(newSum)
        newInput to (newSum to out)
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
        .onEach {
            if (it < 0) {
                // print(" " + it)
            } else {
                // print("  " + it)
            }
        }
        // .println()
}