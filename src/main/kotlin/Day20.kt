import util.Day
import util.Matrix
import util.Point
import util.toMatrix
import java.util.ArrayDeque

// Answer #1: 696
// Answer #2: 7538

fun main() {
    Day(n = 20) {
        answer { findShortestPath(lines) }
        answer {
            val grid = lines.toMatrix()
            val (start, end, portals) = findAllPortals(grid)
            getDistance(grid, start.pos, end.pos, portals)
        }
    }
}

data class Position(val point: Point, val level: Int)
data class BfsNode(val pos: Position, val dist: Int)

private fun getDistance(
    grid: Matrix<Char>,
    from: Point,
    to: Point,
    portals: Map<String, List<Portal>>
): Int {
    val visitQueue = ArrayDeque<BfsNode>()
    visitQueue.add(BfsNode(Position(from, 0), 0))
    val target = Position(to, 0)
    val visited = mutableSetOf<Position>()
    while (visitQueue.isNotEmpty()) {
        val current = visitQueue.poll()
        if (current.pos == target) return current.dist

        visited.add(current.pos)
        getNeighbours(grid, portals, current.pos)
            .filter { it !in visited }
            .forEach { visitQueue.add(BfsNode(it, current.dist + 1)) }
    }
    return -1
}

private val cache = mutableMapOf<Position, List<Position>>()
private fun getNeighbours(
    grid: Matrix<Char>,
    portals: Map<String, List<Portal>>,
    pos: Position
): List<Position> {
    cache[pos]?.let { return it }

    val neighbours = pos.point.neighbours
        .filter { grid[it.y][it.x] == '.' }
        .map { Position(it, pos.level) }
        .toMutableList()

    val port = portals.entries.find { pos.point in it.value.map { it.pos } }

    val portal = port?.value?.find { it.pos == pos.point }

    if (port != null && port.key != "AA" && portal != null) {
        val level: Int
        if (portal.isOuter) {
            if (pos.level != 0) {
                level = pos.level - 1
            } else {
                cache[pos] = neighbours
                return neighbours
            }
        } else {
            level = pos.level + 1
        }
        port.value.find { it.pos != pos.point }
            ?.let { neighbours.add(Position(it.pos, level)) }
    }
    cache[pos] = neighbours
    return neighbours
}

private fun findShortestPath(lines: List<String>): Int? {
    val grid = lines.toMatrix()
    val (start, end, portals) = findAllPortals(grid)
    val travelled = initTravelled(grid).also { it[start.pos] = 0 }

    val pointToPortal = portals.flatMap { (key, value) -> value.map { it.pos to key } }.toMap()

    val queue = mutableListOf<Point>().apply { add(start.pos) }
    while (queue.isNotEmpty()) {
        val location = queue.removeAt(0)
        val steps = travelled[location] ?: error("")
        val portalName = pointToPortal[location]
        location.neighbours
            .forEach { point ->
                val c = grid[point.y][point.x]
                if (c == '.') {
                    val previousSteps = travelled[point] ?: error("")
                    if (previousSteps == -1 || previousSteps > steps + 1) {
                        travelled[point] = steps + 1
                        queue.add(point)
                    }
                } else if (c.isLetter() && portalName != null) {
                    val next = (portals[portalName] ?: error("")).find { location != it.pos }!!.pos
                    val previousSteps = travelled[next] ?: error("")
                    if (previousSteps == -1 || previousSteps > steps + 1) {
                        travelled[next] = steps + 1
                        queue.add(next)
                    }
                }
            }
    }
    return travelled[end.pos]
}

private fun initTravelled(grid: Array<Array<Char>>): MutableMap<Point, Int> {
    return mutableMapOf<Point, Int>().apply {
        grid.forEachIndexed { y, row -> row.forEachIndexed { x, c -> if (c == '.') set(Point(x, y), -1) } }
    }
}

private fun findAllPortals(grid: Matrix<Char>) = MazePoi().apply {
    val (innerTop, innerBottom) = findVerticalWhiteSpace(grid)
    val (innerStart, innerEnd) = findHorizontalWhiteSpace(grid)

    findPortalsAbove(grid, innerBottom, false)
    findPortalsAbove(grid, 2, true)

    findPortalsStart(grid, innerEnd, false)
    findPortalsStart(grid, 2, true)

    findPortalsBelow(grid, innerTop, false)
    findPortalsBelow(grid, grid.size - 3, true)

    findPortalsEnd(grid, innerStart, false)
    findPortalsEnd(grid, grid.first().size - 3, true)
}

private fun MazePoi.findPortalsStart(grid: Matrix<Char>, x: Int, isOuter: Boolean) {
    grid.forEachIndexed { y, row ->
        if (row[x] == '.' && grid[y][x - 1].isLetter()) {
            addPortal(grid[y][x - 2], grid[y][x - 1], Point(x, y), isOuter)
        }
    }
}

private fun MazePoi.findPortalsEnd(grid: Matrix<Char>, x: Int, isOuter: Boolean) {
    grid.forEachIndexed { y, row ->
        if (row[x] == '.' && grid[y][x + 1].isLetter()) {
            addPortal(grid[y][x + 1], grid[y][x + 2], Point(x, y), isOuter)
        }
    }
}

private fun MazePoi.findPortalsAbove(grid: Matrix<Char>, y: Int, isOuter: Boolean) {
    grid[y].forEachIndexed { x, c ->
        if (c == '.' && grid[y - 1][x].isLetter()) {
            addPortal(grid[y - 2][x], grid[y - 1][x], Point(x, y), isOuter)
        }
    }
}

private fun MazePoi.findPortalsBelow(grid: Matrix<Char>, y: Int, isOuter: Boolean) {
    grid[y].forEachIndexed { x, c ->
        if (c == '.' && grid[y + 1][x].isLetter()) {
            addPortal(grid[y + 1][x], grid[y + 2][x], Point(x, y), isOuter)
        }
    }
}

private fun findVerticalWhiteSpace(grid: Matrix<Char>) =
    grid.withIndex()
        .drop(2)
        .dropLast(2)
        .dropWhile { (_, row) -> row[row.size / 2].let { r -> r == '#' || r == '.' } }
        .dropLastWhile { (_, row) -> row[row.size / 2].let { r -> r == '#' || r == '.' } }
        .let { g -> g.first().index - 1 to g.last().index + 1 }

private fun findHorizontalWhiteSpace(grid: Matrix<Char>) =
    grid[grid.size / 2]
        .withIndex()
        .drop(2)
        .dropLast(2)
        .dropWhile { (_, c) -> c == '#' || c == '.' }
        .dropLastWhile { (_, c) -> c == '#' || c == '.' }
        .let { g -> g.first().index - 1 to g.last().index + 1 }

private data class Portal(val pos: Point, val isOuter: Boolean)

private data class MazePoi(
    var start: Portal = Portal(Point(), false),
    var end: Portal = Portal(Point(), false),
    val portals: MutableMap<String, List<Portal>> = mutableMapOf()
) {
    fun addPortal(a: Char, b: Char, pos: Point, isOuter: Boolean) {
        when (val key = "$a$b") {
            "AA" -> start = Portal(pos, isOuter)
            "ZZ" -> end = Portal(pos, isOuter)
            else -> portals.merge(key, listOf(Portal(pos, isOuter))) { o, n -> o + n }
        }
    }
}

val input = (
        "         A           \n" +
        "         A           \n" +
        "  #######.#########  \n" +
        "  #######.........#  \n" +
        "  #######.#######.#  \n" +
        "  #######.#######.#  \n" +
        "  #######.#######.#  \n" +
        "  #####  B    ###.#  \n" +
        "BC...##  C    ###.#  \n" +
        "  ##.##       ###.#  \n" +
        "  ##...DE  F  ###.#  \n" +
        "  #####    G  ###.#  \n" +
        "  #########.#####.#\n" +
        "DE..#######...###.#  \n" +
        "  #.#########.###.#  \n" +
        "FG..#########.....#  \n" +
        "  ###########.#####  \n" +
        "             Z       \n" +
        "             Z       ").split("\n")