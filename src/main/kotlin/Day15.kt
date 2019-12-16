import Direction.*
import Status.*
import extension.asLongs
import extension.csv
import util.Day
import util.IntCode
import util.Point

// Answer #1:
// Answer #2:

fun main() {
    Day(n = 15) {
        answer {
            val program = IntCode(lines.first().csv.asLongs(), debug = false)
            attempt2(program)
            ""
        }
        answer { }
    }
}

private fun attempt2(program: IntCode) {
    val map = mutableMapOf<Point, Char>()
    val start = Point(0, 0)
    map[start] = 'D'
    var point = start
    var dir = EAST
    while (!program.hasTerminated) {
        val suggestedPoint = point.next(dir)
        dir = when (val tile = map[suggestedPoint]) {
            null -> dir
            '.' -> {
                point.tryFindMoreInterestingDirection(map, dir)
            }
            '#' -> {
                point.unblock(map, dir)
            }
            else -> error("Not sure how to handle: $tile")
        }

        val nextPoint = point.next(dir)
        when (val status = Status.from(program.run(dir.n))) {
            Status.WALL -> map[nextPoint] = '#'

            Status.OK -> {
                map[nextPoint] = 'D'
                map[point] = '.'
                point = nextPoint
            }

            Status.FOUND -> {
                map[nextPoint] = 'X'
            }
        }

        println(render(map))
        println()
        Thread.sleep(250L)

    }
}

private fun Point.tryFindMoreInterestingDirection(map: Map<Point, Char>, dir: Direction) =
    dir.perpendicular()
        .map { it to map[this.next(it)] }
        .firstOrNull { it.second == null }
        ?.first ?: (listOf(dir) + dir.perpendicular()).random()

private fun Point.unblock(map: Map<Point, Char>, dir: Direction): Direction {
    return Direction.values()
        .filterNot { it == dir }
        .map { it to map[this.next(it)] }
        .filterNot { it.second == '#' }
        .minBy {
            when (it.second) {
                null -> 0
                '.', 'D' -> {
                    if (it.first.isOpposite(dir)) {
                        2
                    } else {
                        1
                    }
                }
                else -> error("Trying to sort: ${it.second}")
            }
        }!!.first
}

private fun attempt1(program: IntCode) {
    val map = mutableMapOf<Point, Char>()
    val start = Point(0, 0)
    map[start] = 'D'

    var point = start
    var dir = EAST
    var prevDir: Direction? = null
    while (!program.hasTerminated) {
        val output = program.run(dir.n)
        val new = getPoint(point, dir)

        val char = when (Status.from(output)) {
            WALL -> {
                val before = dir
                dir = prevDir ?: Direction.values()
                    .filterNot { it == dir }
                    .map { it to getPoint(point, it) }
                    .filterNot { map[it.second] == '#' }
                    .minBy {
                        map[it.second]?.let { tile ->
                            if (tile == '#') {
                                10
                            } else {
                                if (dir.isOpposite(it.first)) {
                                    2
                                } else {
                                    1
                                }
                            }
                        } ?: 0
                    }!!.first

                println("hit wall at point$new, change dir from:$before to:${dir}")
                prevDir = null
                '#'
            }
            OK -> {
                prevDir = null
                var extra = ""
                map[point] = '.'
                point = new
                if (map[new] == '.') {
                    dir = Direction.values()
                        .map {
                            getPoint(new, it) to it
                        }
                        .find { map[it.first] == null }
                        ?.second?.also {
                        extra = "changed dir to:$it"
                        prevDir = dir
                    }
                        ?: dir
                }

                println("no wall, moving to:$new extra:$extra")
                'D'
            }
            FOUND -> {
                println("found source at:$new, terminating")
                'X'
            }
        }
        map[new] = char
        println(render(map))
        println()
        Thread.sleep(100L)
    }
}

private fun render(map: Map<Point, Char>): String {
    val x = map.map { it.key.x }
    val minX = x.min() ?: 0
    val maxX = x.max() ?: 0

    val y = map.map { it.key.y }
    val minY = y.min() ?: 0
    val maxY = y.max() ?: 0

    return (minY..maxY).joinToString("\n") { y ->
        (minX..maxX).map { x -> map[Point(x, y)] ?: ' ' }.joinToString("")
    }
}

private fun Point.next(dir: Direction) = when (dir) {
    NORTH -> copy(y = y + 1)
    SOUTH -> copy(y = y - 1)
    WEST -> copy(x = x - 1)
    EAST -> copy(x = x + 1)
}

private fun getPoint(previous: Point, dir: Direction): Point {
    return when (dir) {
        NORTH -> previous.copy(y = previous.y + 1)
        SOUTH -> previous.copy(y = previous.y - 1)
        WEST -> previous.copy(x = previous.x - 1)
        EAST -> previous.copy(x = previous.x + 1)
    }
}

private fun Direction.isOpposite(other: Direction) = when (this) {
    other -> false
    NORTH -> other == SOUTH
    SOUTH -> other == NORTH
    WEST -> other == EAST
    EAST -> other == WEST
}
private fun Direction.perpendicular() = when (this) {
    NORTH -> listOf(WEST, EAST)
    SOUTH -> listOf(WEST, EAST)
    WEST -> listOf(NORTH, SOUTH)
    EAST -> listOf(NORTH, SOUTH)
}
private enum class Direction(val n: Long) {
    NORTH(1), SOUTH(2), WEST(3), EAST(4);

    // fun next() = values().filterNot { it == this }.random()
    fun next() = when (this) {
        NORTH -> SOUTH
        SOUTH -> EAST
        WEST -> NORTH
        EAST -> WEST
    }
    companion object {
        fun from(n: Long) = values().find { it.n == n } ?: error("What did you do? $n")
    }
}
private enum class Status(val n: Long) {
    WALL(0), OK(1), FOUND(2);
    companion object {
        fun from(n: Long) = values().find { it.n == n } ?: error("What did you do? $n")
    }
}