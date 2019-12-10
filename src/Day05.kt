import util.Day
import util.IntCode
import extension.asLongs
import extension.csv

// Answer #1: 14155342
// Answer #2: 8684145

fun main() {
    Day(n = 5) {
        answer { IntCode(lines.first().csv.asLongs()).run(1) }
        answer { IntCode(lines.first().csv.asLongs()).run(5) }
    }
}

