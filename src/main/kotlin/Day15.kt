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

private fun createMap(program: IntCode): Map<Point, Char> {
    val start = Point(0, 0)
    var oxygen: Point? = null

    val map = mutableMapOf<Point, Char>()
    map[start] = 'S'

    var point = start
    var direction = CardinalDirection.EAST
    while (true) {
        val nextPoint = point.next(direction).takeIf { it != start } ?: break

        map[nextPoint] = when (val status = program.run(direction.n)) {
            0L -> {
                direction = direction.ccw()
                '#'
            }

            1L -> {
                if (point != oxygen && point != start) {
                    map[point] = '.'
                }
                point = nextPoint
                direction = direction.cw()
                'D'
            }

            2L -> {
                map[point] = '.'
                oxygen = nextPoint
                point = nextPoint
                direction = direction.cw()
                'O'
            }

            else -> error("Not sure what to do with: $status")
        }
    }
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
