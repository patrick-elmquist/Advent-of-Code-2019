import util.Day
import extension.asLongs
import extension.csv
import util.IntCode

// Answer #1: 11590668
// Answer #2: 2254

fun main() {
    Day(n = 2) {
        answer {
            val input = lines.first().csv.asLongs()
            run(input, 12, 2)
        }

        answer {
            val input = lines.first().csv.asLongs()
            (0L..99L).flatMap { noun -> (0L..99L).map { verb -> noun to verb } }
                .first { (noun, verb) -> run(input, noun, verb) == 19690720L }
                .let { (noun, verb) -> 100L * noun + verb }
        }
    }
}

private fun run(input: List<Long>, noun: Long, verb: Long): Long {
    val instructions = input.toMutableList().also {
        it[1] = noun
        it[2] = verb
    }
    return IntCode(instructions).apply { run() }.memory[0]
}
