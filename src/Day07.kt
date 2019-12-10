import util.Day
import util.IntCode
import util.asLongs
import util.csv
import util.permutations

// Answer #1: 118936
// Answer #2: 57660948

fun main() {
    Day(n = 7) {
        answer {
            val memory = lines.first().csv.asLongs()
             (0..4).permutations()
                .map { phases ->
                    phases
                        .map { it.toLong() }
                        .fold(0L) { output, phase -> IntCode(memory).run(phase, output) }
                }
                .max()
        }
        answer {
            val memory = lines.first().csv.asLongs()
            (5..9).permutations()
                .map { phases ->
                    phases
                        .map { it.toLong() }
                        .map { phase -> IntCode(memory).apply { run(phase) } }
                }
                .map { amps -> createFeedbackLoop(amps).last().second }
                .max()
        }
    }
}

private fun createFeedbackLoop(amps: List<IntCode>) = generateSequence(0 to 0L) { (index, input) ->
    val amp = amps[index]
    if (amp.hasTerminated && amp == amps.first()) null else (index + 1) % amps.size to amp.run(input)
}
