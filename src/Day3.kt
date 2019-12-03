import util.Day
import util.csv
import kotlin.math.abs

// Answer #1: (Point(x=-325, y=-384), 709)
// Answer #2: (Point(x=-161, y=-1208), 13836)

private val origin = Point(0, 0)

fun main(args: Array<String>) {
    Day(n = 3) {
        answer {
            val firstWire = createPath(origin, lines[0].csv)
            val secondWire = createPath(origin, lines[1].csv)
            firstWire.intersect(secondWire)
                .filter { it != origin }
                .map { it to (abs(it.x) + abs(it.y)) }
                .minBy { it.second }
        }
        answer {
            val firstWire = createPath(origin, lines[0].csv)
            val secondWire = createPath(origin, lines[1].csv)
            firstWire.intersect(secondWire)
                .filter { it != origin }
                .map { it to (firstWire.indexOf(it) + secondWire.indexOf(it)) }
                .minBy { it.second }
        }
    }
}

private fun createPath(origin: Point, input: List<String>): List<Point> {
    val points = mutableListOf<Point>()
    var start = origin
    input.forEach { instruction ->
        val value = instruction.drop(1).toInt()
        val end = when(instruction.first()) {
            'L' -> Point(start.x - value, start.y)
            'U' -> Point(start.x, start.y + value)
            'R' -> Point(start.x + value, start.y)
            'D' -> Point(start.x, start.y - value)
            else -> TODO("Sink ship")
        }

        var last = points.lastOrNull()
        for (x in if (start.x < end.x) start.x..end.x else start.x downTo end.x) {
            for (y in if (start.y < end.y) start.y..end.y else start.y downTo end.y) {
                val point = Point(x, y)
                if (point == last) {
                    last = null
                } else {
                    points.add(point)
                }
            }
        }
        start = end
    }
    return points
}

data class Point(val x: Int = 0, val y: Int = 0)