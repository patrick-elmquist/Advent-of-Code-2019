import util.Day
import util.IntCode
import util.asInts
import util.asLongs
import util.csv

// Answer #1: 14155342
// Answer #2: 8684145

fun main() {
    Day(n = 5) {
        answer { IntCode(lines.first().csv.asLongs()).run(1) }
        answer { IntCode(lines.first().csv.asLongs()).run(5) }
    }
}

