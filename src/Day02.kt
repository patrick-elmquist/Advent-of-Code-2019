import util.Day
import extension.asInts
import extension.csv

// Answer #1: 11590668
// Answer #2: 2254

fun main() {
    Day(n = 2) {
        answer {
            val program = lines.first().csv.asInts()
            run(program, 12, 2)
        }

        answer {
            val program = lines.first().csv.asInts()
            for (noun in 0..99) {
                for (verb in 0..99) {
                    if (run(program, noun, verb) == 19690720) return@answer 100 * noun + verb
                }
            }
        }
    }
}

private fun run(program: List<Int>, noun: Int, verb: Int): Int {
    val program = program.toMutableList()
    program[1] = noun
    program[2] = verb

    var opIndex = 0
    while (true) {
        val opCode = program[opIndex]

        if (opCode != 1 && opCode != 2) break

        val r1 = program[program[opIndex + 1]]
        val r2 = program[program[opIndex + 2]]
        val r3 = program[opIndex + 3]
        program[r3] = if (opCode == 1) r1 + r2 else r1 * r2

        opIndex += 4
    }
    return program[0]
}
