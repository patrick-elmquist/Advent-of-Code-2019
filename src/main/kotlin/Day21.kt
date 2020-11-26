import extension.asLongs
import extension.csv
import util.Day
import util.IntCode

// Answer #1: 19357180
// Answer #2: 1139793906

fun main() {
    Day(n = 21) {
        answer {
            val program = lines.first().csv.asLongs()
            val instructions =
                    """
                        OR A T
                        AND B T
                        AND C T
                        NOT T J
                        AND D J
                        WALK
                    """.trimIndent().map(Char::toLong).toLongArray() + 10
            IntCode(program).apply { run(*instructions) }.output
        }
        answer {
            val program = lines.first().csv.asLongs()
            val instructions =
                    """
                        OR A T
                        AND B T
                        AND C T
                        NOT T J
                        AND D J
                        NOT J T
                        OR E T
                        OR H T
                        AND T J
                        RUN
                    """.trimIndent().map(Char::toLong).toLongArray() + 10
            IntCode(program).apply { run(*instructions) }.output
        }
    }
}

