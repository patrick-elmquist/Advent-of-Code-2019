import util.Day
import util.asInts
import util.csv

// Answer #1: 11590668
// Answer #2: 2254

fun main(args: Array<String>) {
    Day(n = 2) {
        answer {
            val program = line.csv.asInts().toMutableList()
            run(program, 12, 2)
        }

        answer {
            val program = line.csv.asInts()
            var result = -1 to -1
            outer@ for (noun in 0..99) {
                for (verb in 0..99) {
                    if (run(program.toMutableList(), noun, verb) == 19690720) {
                        result = noun to verb
                        break@outer
                    }
                }
            }
            100 * result.first + result.second
        }
    }
}

private fun run(program: MutableList<Int>, noun: Int, verb: Int): Int {
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
