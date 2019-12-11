import util.Day
import extension.asLongs
import extension.csv
import util.IntCode

// Answer #1: 11590668
// Answer #2: 2254

fun main() {
    Day(n = 2) {
        answer {
            val instructions = lines.first().csv.asLongs().toMutableList().also {
                it[1] = 12
                it[2] = 2
            }
            IntCode(instructions).apply { run() }.memory[0]
        }

        answer {
            val input = lines.first().csv.asLongs()
            for (noun in 0L..99L) {
                for (verb in 0L..99L) {
                    val instructions = input.toMutableList().also {
                        it[1] = noun
                        it[2] = verb
                    }
                    val program = IntCode(instructions).apply { run() }
                    if (program.memory[0] == 19690720L) return@answer 100L * noun + verb
                }
            }
        }
    }
}
