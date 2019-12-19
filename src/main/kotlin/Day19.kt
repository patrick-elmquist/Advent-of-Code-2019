import extension.asLongs
import extension.csv
import util.Day
import util.IntCode
import util.Point

// Answer #1: 211
// Answer #2: 8071006

fun main() {
    Day(n = 19) {
        answer {
            val program = lines.first().csv.asLongs()
            val range = 0L until 50L
            range.map { x -> range.map { y -> IntCode(program).run(x, y) }.sum() }.sum()
        }
        answer {
            val program = lines.first().csv.asLongs()
            var x = 500L
            var y = 0L
            while (true) {
                if (detectBeam(program, x, y)) {
                    if (detectBeam(program, x - 99, y + 99)) {
                        break
                    } else {
                        x++
                    }
                } else {
                    y++
                }
            }
            10000 * (x - 99) + y
        }
    }
}

private fun detectBeam(instructions: List<Long>, x: Long, y: Long) = IntCode(instructions).run(x, y) == 1L
