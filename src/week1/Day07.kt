package week1

import util.Day
import util.IntCode
import util.asInts
import util.csv
import util.permutations

// Answer #1: 118936
// Answer #2: 57660948

fun main() {
    Day(n = 7) {
        answer {
            val memory = lines.first().csv.asInts()
             (0..4).permutations()
                .map { phases -> phases.fold(0) { output, phase -> IntCode(memory).run(listOf(phase, output)) } }
                .max()
        }
        answer {
            val memory = lines.first().csv.asInts()
            (5..9).permutations()
                .map { phases -> phases.map { phase -> IntCode(memory).apply { run(listOf(phase)) } } }
                .map { amps -> createFeedbackLoop(amps).last().second }
                .max()
        }
    }
}

private fun createFeedbackLoop(amps: List<IntCode>) = generateSequence(0 to 0) { (index, input) ->
    val amp = amps[index]
    if (amp.isFinished && amp == amps.first()) null else (index + 1) % amps.size to amp.run(listOf(input))
}
