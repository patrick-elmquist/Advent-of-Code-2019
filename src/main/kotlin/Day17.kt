import extension.asLongs
import extension.csv
import util.Day
import util.IntCode
import util.Point

// Answer #1: 6212
// Answer #2: 1016741

private val scaffold = listOf('#', '^', 'V', '<', '>')

fun main() {
    Day(n = 17) {
        answer {
            val program = IntCode(lines.first().csv.asLongs()).apply { run() }
            val tiles = program.outputs.map { it.toChar() }.joinToString("")
            sumAlignmentParameters(tiles)
        }
        answer {
            val enableInput = lines.first().csv.asLongs().toMutableList().apply { set(0, 2) }
            IntCode(enableInput).apply {
                run()
                run(*"A,A,B,C,B,C,B,C,C,A\n".toLongInput())
                run(*"L,10,R,8,R,8\n".toLongInput())
                run(*"L,10,L,12,R,8,R,10\n".toLongInput())
                run(*"R,10,L,12,R,10\n".toLongInput())
                run(*"n\n".toLongInput())
            }.output
        }
    }
}

private fun sumAlignmentParameters(tiles: String): Int {
    val scaffoldTiles = tiles.split("\n")
        .mapIndexed { y, row -> row.mapIndexed { x, c -> Point(x, y) to c } }
        .flatten()
        .toMap()
        .filterValues { it in scaffold }

    return scaffoldTiles
        .map { (point, _) -> if (point.scaffoldNeighbourCount(scaffoldTiles) > 2) point.x * point.y else 0 }
        .sum()
}

private fun Point.scaffoldNeighbourCount(pointToCharMap: Map<Point, Char>) =
    neighbours
        .filter { it in pointToCharMap }
        .map { pointToCharMap[it] }
        .sumBy { if (it in scaffold) 1 else 0 }

private fun String.toLongInput() = map { it.toLong() }.toLongArray()
