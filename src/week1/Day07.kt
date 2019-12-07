package week1

import util.Day
import util.IntCode
import util.asInts
import util.csv
import util.permutations

// Answer #1: 118936
// Answer #2:

fun main() {
    Day(n = 7) {
        answer {
            val memory = lines.first().csv.asInts()
            listOf(0,1,2,3,4)
                .permutations()
                .map { phases -> phases.fold(0) { output, phase -> IntCode(memory, listOf(phase, output)).run() } }
                .max()
        }
        answer {

        }
    }
}
