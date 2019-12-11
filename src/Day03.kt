import util.Day
import extension.csv
import util.Point
import kotlin.math.abs

// Answer #1: (Point(x=-325, y=-384), 709)
// Answer #2: (Point(x=-161, y=-1208), 13836)

private val origin = Point(0, 0)

fun main() {
    Day(n = 3) {
        answer {
            val firstWire = createWirePath(origin, lines[0].csv)
            val secondWire = createWirePath(origin, lines[1].csv)
            firstWire.intersect(secondWire)
                .filter { it != origin }
                .map { it to (abs(it.x) + abs(it.y)) }
                .minBy { it.second }
        }
        answer {
            val firstWire = createWirePath(origin, lines[0].csv)
            val secondWire = createWirePath(origin, lines[1].csv)
            firstWire.intersect(secondWire)
                .filter { it != origin }
                .map { it to (firstWire.indexOf(it) + secondWire.indexOf(it)) }
                .minBy { it.second }
        }
    }
}

private fun createWirePath(origin: Point, input: List<String>) =
    input.fold(mutableListOf<Point>(origin)) { points, instruction ->
        val start = points.last()
        val end = when(instruction.first()) {
            'L' -> start.copy(x = start.x - instruction.drop(1).toInt())
            'U' -> start.copy(y = start.y + instruction.drop(1).toInt())
            'R' -> start.copy(x = start.x + instruction.drop(1).toInt())
            'D' -> start.copy(y = start.y - instruction.drop(1).toInt())
            else -> TODO("Sink ship")
        }
        points.apply {
            for (x in if (start.x < end.x) start.x..end.x else start.x downTo end.x) {
                for (y in if (start.y < end.y) start.y..end.y else start.y downTo end.y) {
                    Point(x, y).takeIf { it != start }?.let { add(it) }
                }
            }
        }
    }
