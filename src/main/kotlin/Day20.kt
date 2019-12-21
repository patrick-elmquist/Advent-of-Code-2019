import extension.splitLines
import util.Day
import util.Matrix
import util.Point
import util.toMatrix

// Answer #1: 696
// Answer #2:

fun main() {
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

    val input2 = (
            "                   A               \n" +
            "                   A               \n" +
            "  #################.#############  \n" +
            "  #.#...#...................#.#.#  \n" +
            "  #.#.#.###.###.###.#########.#.#  \n" +
            "  #.#.#.......#...#.....#.#.#...#  \n" +
            "  #.#########.###.#####.#.#.###.#  \n" +
            "  #.............#.#.....#.......#  \n" +
            "  ###.###########.###.#####.#.#.#  \n" +
            "  #.....#        A   C    #.#.#.#  \n" +
            "  #######        S   P    #####.#  \n" +
            "  #.#...#                 #......VT\n" +
            "  #.#.#.#                 #.#####  \n" +
            "  #...#.#               YN....#.#  \n" +
            "  #.###.#                 #####.#  \n" +
            "DI....#.#                 #.....#  \n" +
            "  #####.#                 #.###.#  \n" +
            "ZZ......#               QG....#..AS\n" +
            "  ###.###                 #######  \n" +
            "JO..#.#.#                 #.....#  \n" +
            "  #.#.#.#                 ###.#.#  \n" +
            "  #...#..DI             BU....#..LF\n" +
            "  #####.#                 #.#####  \n" +
            "YN......#               VT..#....QG\n" +
            "  #.###.#                 #.###.#  \n" +
            "  #.#...#                 #.....#  \n" +
            "  ###.###    J L     J    #.#.###  \n" +
            "  #.....#    O F     P    #.#...#  \n" +
            "  #.###.#####.#.#####.#####.###.#  \n" +
            "  #...#.#.#...#.....#.....#.#...#  \n" +
            "  #.#####.###.###.#.#.#########.#  \n" +
            "  #...#.#.....#...#.#.#.#.....#.#  \n" +
            "  #.###.#####.###.###.#.#.#######  \n" +
            "  #.#.........#...#.............#  \n" +
            "  #########.###.###.#############  \n" +
            "           B   J   C               \n" +
            "           U   P   P               ").splitLines()

    Day(n = 20) {
        answer {
            val grid = lines.toMatrix()
            val (start, end, portals) = findAllPortals(grid)
            val travelled = initTravelled(grid).also { it[start.pos] = 0 }

            val pointToPortal = portals.flatMap { (key, value) -> value.map { it.pos to key } }.toMap()

            val queue = mutableListOf<Point>().apply { add(start.pos) }
            while(queue.isNotEmpty()) {
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
            travelled[end.pos]
        }
        answer {
            ""
        }
    }
}

private fun initTravelled(grid: Array<Array<Char>>): MutableMap<Point, Int> {
    return mutableMapOf<Point, Int>().apply {
        grid.forEachIndexed { y, row -> row.forEachIndexed { x, c -> if (c == '.') set(Point(x, y), -1) } }
    }
}

private fun findAllPortals(grid: Matrix<Char>) = MazePoi().apply {
    val (innerTop, innerBottom) = findVerticalWhiteSpace(grid)
    val (innerStart, innerEnd) = findHorizontalWhiteSpace(grid)

    findPortalsBelow(grid, innerTop)
    findPortalsBelow(grid, grid.size - 3)

    findPortalsAbove(grid, 2)
    findPortalsAbove(grid, innerBottom)

    findPortalsStart(grid, 2)
    findPortalsStart(grid, innerEnd)

    findPortalsEnd(grid, innerStart)
    findPortalsEnd(grid, grid.first().size - 3)
}

private fun MazePoi.findPortalsStart(grid: Matrix<Char>, x: Int) {
    grid.forEachIndexed { y, row ->
        if (row[x] == '.' && grid[y][x - 1].isLetter()) {
            addPortal(grid[y][x - 2], grid[y][x - 1], Point(x, y), false)
        }
    }
}

private fun MazePoi.findPortalsEnd(grid: Matrix<Char>, x: Int) {
    grid.forEachIndexed { y, row ->
        if (row[x] == '.' && grid[y][x + 1].isLetter()) {
            addPortal(grid[y][x + 1], grid[y][x + 2], Point(x, y), false)
        }
    }
}

private fun MazePoi.findPortalsAbove(grid: Matrix<Char>, y: Int) {
    grid[y].forEachIndexed { x, c ->
        if (c == '.' && grid[y - 1][x].isLetter()) {
            addPortal(grid[y - 2][x], grid[y - 1][x], Point(x, y), false)
        }
    }
}

private fun MazePoi.findPortalsBelow(grid: Matrix<Char>, y: Int) {
    grid[y].forEachIndexed { x, c ->
        if (c == '.' && grid[y + 1][x].isLetter()) {
            addPortal(grid[y + 1][x], grid[y + 2][x], Point(x, y), false)
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