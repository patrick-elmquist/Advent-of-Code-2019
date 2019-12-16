import Status.*
import extension.asLongs
import extension.csv
import util.Day
import util.CardinalDirection
import util.IntCode
import util.Point

// Answer #1: 240
// Answer #2: 322

fun main() {
    Day(n = 15) {
        answer {
            val map = createMap(IntCode(lines.first().csv.asLongs()))
            val start = map.entries.first { it.value == 'S' }.key
            val oxygenSystem = map.entries.first { it.value == 'O' }.key
            findShortestPath(map = map, from = start, to = oxygenSystem)
        }
        answer {
            val map = createMap(IntCode(lines.first().csv.asLongs()))
            val oxygenSystem = map.entries.first { it.value == 'O' }.key
            observeOxygen(map = map, source = oxygenSystem)
        }
    }
}

private fun observeOxygen(map: Map<Point, Char>, source: Point): Int {
    val tiles = map.filter { it.value != '#' }.mapValues { -1 }.toMutableMap()
    val queue = mutableListOf<Point>()
    queue.add(source)
    tiles[source] = 0

    while(tiles.values.filter { it != -1 }.size != tiles.size || queue.isNotEmpty()) {
        visitAndQueueNeighbours(queue.removeAt(0), tiles, queue)
    }
    return tiles.values.max() ?: error("")
}

private fun findShortestPath(map: Map<Point, Char>, from: Point, to: Point): Int {
    val tiles = map.filter { it.value != '#' }.mapValues { -1 }.toMutableMap()
    val queue = mutableListOf<Point>()
    queue.add(from)
    tiles[from] = 0

    var point: Point? = null
    while(point != to) {
        point = queue.removeAt(0)
        visitAndQueueNeighbours(point, tiles, queue)
    }
    return tiles[point] ?: error("")
}

private fun visitAndQueueNeighbours(point: Point, tiles: MutableMap<Point, Int>, queue: MutableList<Point>) {
    point.neighbours
        .filter { it in tiles && tiles[it] == -1 }
        .forEach {
            tiles[it] = (tiles[point] ?: error("You cocked up!")) + 1
            queue.add(it)
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
    var direction = CardinalDirection.EAST
    while (true) {
        val nextPoint = point.next(direction)
        if (nextPoint == start) {
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

private fun Point.next(dir: CardinalDirection) = when (dir) {
    CardinalDirection.NORTH -> copy(y = y + 1)
    CardinalDirection.SOUTH -> copy(y = y - 1)
    CardinalDirection.WEST -> copy(x = x - 1)
    CardinalDirection.EAST -> copy(x = x + 1)
}

private enum class Status(val n: Long) {
    WALL(0), OK(1), FOUND(2);
    companion object {
        fun from(n: Long) = values().find { it.n == n } ?: error("What did you do? $n")
    }
}