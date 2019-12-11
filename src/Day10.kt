import util.Day
import util.Point
import util.angleTo
import util.distanceTo

// Answer #1: (Asteroid(x=26.0, y=29.0), 303)
// Answer #2: (Asteroid(x=4.0, y=8.0), 408.0)

fun main() {
    Day(n = 10) {
        answer { findOptimalAsteroidForStation(parseList(lines)) }
        answer { vaporizeAsteroids(parseList(lines)) }
    }
}

private fun findOptimalAsteroidForStation(asteroids: List<Point>) =
    asteroids
        .map { origin ->
            origin to asteroids
                .filterNot { it == origin }
                .map { origin.angleTo(it) }
                .distinct()
                .count()
        }
        .maxBy { (_, count) -> count }

private fun vaporizeAsteroids(asteroids: List<Point>): Pair<Point, Float>? {
    val station = findOptimalAsteroidForStation(asteroids)?.first
        ?: throw IllegalStateException("Sink ship")

    val asteroidsForAngle = asteroids.asSequence()
        .filter { target -> target != station }
        .groupBy { target -> (station.angleTo(target) + 270) % 360 }
        .map { it.key to it.value.sortedBy { target -> target.distanceTo(station) }.toMutableList() }
        .sortedBy { it.first }

    var index = 0
    var vaporized = 0
    while(true) {
        val list = asteroidsForAngle[index].second
        if (list.isNotEmpty()) {
            if (++vaporized == 200) break else list.removeAt(0)
        }
        index = ++index % asteroidsForAngle.size
    }
    return asteroidsForAngle[index].second.first().let { it to (it.x * 100f + it.y) }
}

private fun parseList(input: List<String>) = input
    .mapIndexed { y, row -> row.mapIndexedNotNull { x, c -> if (c == '#') Point(x, y) else null } }
    .flatten()
