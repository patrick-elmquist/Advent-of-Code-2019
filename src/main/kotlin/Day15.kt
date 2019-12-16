import Status.*
import extension.asLongs
import extension.csv
import util.Day
import util.Direction
import util.IntCode
import util.Point

// Answer #1: 240
// Answer #2:

fun main() {
    Day(n = 15) {
        answer {
            val map = createMap(IntCode(lines.first().csv.asLongs()))
            val start = map.entries.first { it.value == 'S' }.key
            val oxygen = map.entries.first { it.value == 'O' }.key
            findShortestPath(map = map, from = start, to = oxygen)
        }
        answer {
            "Not implemented"
        }
    }
}

private fun findShortestPath(map: Map<Point, Char>, from: Point, to: Point): Int {
    val map = map.filter { it.value != '#' }

    val visited = mutableMapOf<Point, Int>()
    val queue = mutableListOf<Point>()
    queue.add(from)
    visited[from] = 0

    while(true) {
        val point = queue.removeAt(0)
        val distance = visited[point] ?: error("You cocked up!")

        if (point == to) return distance

        point.neighbours
            .filter { it in map }
            .filter { it !in visited }
            .forEach {
                visited[it] = distance + 1
                queue.add(it)
            }

    }
}

private val Point.neighbours get() = listOf(
    copy(x = x - 1),
    copy(x = x + 1),
    copy(y = y - 1),
    copy(y = y + 1)
)

private fun createMap(program: IntCode): Map<Point, Char> {
    val start = Point(0, 0)
    var oxygen: Point? = null

    val map = mutableMapOf<Point, Char>()
    map[start] = 'S'

    var point = start
    var direction = Direction.EAST
    while (true) {
        val nextPoint = point.next(direction)
        if (nextPoint == start) {
            println("DONE")
            break
        }
        when (Status.from(program.run(direction.n))) {
            WALL -> {
                map[nextPoint] = '#'
                direction = direction.ccw()
            }

            OK -> {
                if (point != oxygen && point != start) {
                    map[point] = '.'
                }
                map[nextPoint] = 'D'
                point = nextPoint
                direction = direction.cw()
            }

            FOUND -> {
                map[point] = '.'
                map[nextPoint] = 'O'
                oxygen = nextPoint
                point = nextPoint
                direction = direction.cw()
            }
        }
    }
    map[start] = 'S'
    return map
}

private fun render(map: Map<Point, Char>) {
    val allX = map.map { it.key.x }
    val minX = allX.min() ?: 0
    val maxX = allX.max() ?: 0

    val allY = map.map { it.key.y }
    val minY = allY.min() ?: 0
    val maxY = allY.max() ?: 0

    val topBottom = (minX..maxX).joinToString("", prefix = "+-", postfix = "-+") { "-" }
    val render = (minY..maxY).joinToString("\n") { y ->
        (minX..maxX).map { x -> map[Point(x, y)] ?: ' ' }.joinToString("", prefix = "| ", postfix = " |")
    }
    println(topBottom)
    println(render)
    println(topBottom)
    println()
}

private fun Point.next(dir: Direction) = when (dir) {
    Direction.NORTH -> copy(y = y + 1)
    Direction.SOUTH -> copy(y = y - 1)
    Direction.WEST -> copy(x = x - 1)
    Direction.EAST -> copy(x = x + 1)
}

private enum class Status(val n: Long) {
    WALL(0), OK(1), FOUND(2);
    companion object {
        fun from(n: Long) = values().find { it.n == n } ?: error("What did you do? $n")
    }
}