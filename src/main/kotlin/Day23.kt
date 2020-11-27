import extension.asLongs
import extension.csv
import util.Day
import util.IntCode
import java.lang.IllegalStateException

// Answer #1: 23057
// Answer #2:

fun main() {
    Day(n = 23) {
        answer {
            val program = lines.first().csv.asLongs()
            val nics = (0L until 50L).map { it to IntCode(program, debug = false).apply { run(it) } }.toMap()

            while (true) {
                val packets = nics.values.flatMap { it.outputs.chunked(3) }

                val deliveredAddresses = packets.map { (address, x, y) ->
                    if (address == 255L) return@answer "$y"
                    (nics[address] ?: error("wtf?")).run(x, y)
                    address
                }.toSet()

                nics.filter { it.key !in deliveredAddresses }
                        .forEach { (_, nic) -> nic.run(-1) }
            }

            ""
        }
        answer {
        }
    }
}
