import util.Day
import util.Matrix
import util.Point
import util.render
import util.toMatrix

// Answer #1: 5964
// Answer #2: 1996

private val keySet = 'a'..'z'
private val robotSet = "@1234"
private val keysAndDoorsSet = keySet + ('A'..'Z')

fun main() {
    Day(n = 18) {
        answer {
            val routeInfo = findRouteInfo(lines.toMatrix(default = '-'))
            val keys = routeInfo.filterKeys { it in keySet }.keys

            var info = mutableMapOf(('@' to emptySet<Char>()) to 0)
            repeat(keys.size) {
                val nextInfo = mutableMapOf<Pair<Char, Set<Char>>, Int>()
                info.forEach { item ->
                    val curLoc = item.key.first
                    val curKeys = item.key.second
                    val curDist = item.value

                    keys.filter { it !in curKeys }
                        .forEach { newKey ->
                            val (dist, route) = routeInfo[curLoc]!![newKey] ?: error("")
                            if (route.all { it.toLowerCase() in curKeys }) {
                                val newDist = curDist + dist
                                val pair = newKey to curKeys + newKey
                                if (pair !in nextInfo || newDist < nextInfo[pair] ?: 0) {
                                    nextInfo[pair] = newDist
                                }
                            }
                        }
                    info = nextInfo
                }
            }
            info.values.min()
        }

        answer {
            val matrix = lines.toMatrix(default = '-').also { applyVaults(it) }
            matrix.render()
            val routeInfo = findRouteInfo(matrix)
            val keys = routeInfo.filterKeys { it in keySet }.keys

            var info = mutableMapOf((listOf('1', '2', '3', '4') to emptySet<Char>()) to 0)
            repeat(keys.size) {
                val nextInfo = mutableMapOf<Pair<List<Char>, Set<Char>>, Int>()
                info.forEach { item ->
                    val curLocs = item.key.first
                    val curKeys = item.key.second
                    val curDist = item.value

                    keys.filter { it !in curKeys }
                        .forEach { newKey ->
                            (0 until 4).forEach { robot ->
                                val curLoc = curLocs[robot]
                                if (newKey in routeInfo[curLoc] ?: error("cocked up:$curLoc")) {
                                    val (dist, route) = routeInfo[curLoc]!![newKey] ?: error("")
                                    if (route.all { it.toLowerCase() in curKeys }) {
                                        val newDist = curDist + dist
                                        val newKeys = curKeys + newKey
                                        val newLocs = curLocs.toMutableList()
                                        newLocs[robot] = newKey

                                        val pair = newLocs to newKeys
                                        if (pair !in nextInfo || newDist < nextInfo[pair] ?: 0) {
                                            nextInfo[pair] = newDist
                                        }
                                    }
                                }
                            }
                        }
                    info = nextInfo
                }
            }
            info.values.min()
        }
    }
}

private fun applyVaults(matrix: Array<Array<Char>>) {
    val start = matrix.withIndex()
        .flatMap { (x, column) -> column.mapIndexed { y, c -> Point(x, y) to c } }
        .first { (_, c) -> c == '@' }
        .first

    (start.neighbours + start).forEach { (x, y) -> matrix[x][y] = '#' }
    matrix[start.x - 1][start.y - 1] = '1'
    matrix[start.x - 1][start.y + 1] = '2'
    matrix[start.x + 1][start.y - 1] = '3'
    matrix[start.x + 1][start.y + 1] = '4'
}

private fun findRouteInfo(matrix: Matrix<Char>): Map<Char, Map<Char, Pair<Int, String>>> =
    matrix.withIndex()
        .flatMap { (x, column) ->
            column.withIndex()
                .filter { (_, c) -> c in robotSet || c in keysAndDoorsSet }
                .map { (y, c) -> c to distance(Point(x, y), matrix) }
        }.toMap()

private fun distance(from: Point, matrix: Matrix<Char>) =
    mutableMapOf<Char, Pair<Int, String>>().also { routeInfo ->
        val visited = mutableSetOf(from)
        val start = Poi(from, 0, "")
        val queue = mutableListOf(start)
        while (queue.isNotEmpty()) {
            val poi = queue.removeAt(0)
            val (x, y) = poi.location
            val c = matrix[x][y]
            var route = poi.route
            if (c in keysAndDoorsSet) {
                routeInfo[c] = poi.distance to route
                route += c
            }
            visited.add(poi.location)

            poi.location.neighbours
                .filter { it !in visited && matrix[it.x][it.y] != '#' }
                .fold(queue) { q, i -> q.apply { add(Poi(i, poi.distance + 1, route)) } }
        }
    }

class Poi(val location: Point, val distance: Int, val route: String)

