import util.Day
import util.IntCode
import extension.asLongs
import extension.csv

// Answer #1: 2316632620
// Answer #2: 78869

fun main() {
    Day(n = 9) {
        IntCode.verify()

        answer {
            val instructions = lines.first().csv.asLongs()
            IntCode(instructions).run(1).also { check(it == 2316632620L) }
        }
        answer {
            val instructions = lines.first().csv.asLongs()
            IntCode(instructions).run(2).also { check(it == 78869L) }
        }
    }
}
