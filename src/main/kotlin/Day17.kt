import extension.asLongs
import extension.csv
import util.Day
import util.IntCode
import util.Point

// Answer #1: 6212
// Answer #2:

private val scaffold = listOf('#', '^', 'V', '<', '>')

fun main() {
    Day(n = 17) {
        answer {
            val program = IntCode(lines.first().csv.asLongs(), debug = false).apply { run() }
            val tiles = program.outputs.map { it.toChar() }.joinToString("")
            sumAlignmentParameters(tiles)
        }
        answer { "Not implemented" }
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

private val Point.neighbours get() = listOf(
    copy(x = x - 1),
    copy(x = x + 1),
    copy(y = y - 1),
    copy(y = y + 1)
)
