import util.Day
import extension.f
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

// Answer #1: (Asteroid(x=26.0, y=29.0), 303)
// Answer #2: (Asteroid(x=4.0, y=8.0), 408.0)

fun main() {
    Day(n = 10) {
        answer { findOptimalAsteroidForStation(parseAsteroids(lines)) }
        answer { part2(parseAsteroids(lines)) }
    }
}

data class Asteroid(val x: Float, val y: Float)

private fun findOptimalAsteroidForStation(asteroids: List<Asteroid>) =
    asteroids
        .map { origin ->
            origin to asteroids.asSequence()
                .filter { it != origin }
                .map { angleBetween(origin, it) }
                .distinct()
                .count()
        }
        .maxBy { (_, count) -> count }

private fun part2(asteroids: List<Asteroid>): Pair<Asteroid, Float>? {
    val station = findOptimalAsteroidForStation(asteroids)?.first ?: throw IllegalStateException()
    val stuff = asteroids.asSequence()
        .filter { it != station }
        .map { it to angleBetween(station, it) }
        .groupBy { it.second }
        .map { (angle, asteroids) ->
            angle to asteroids
                .map { it.first }
                .sortedBy { distanceBetween(station, it) }
        }
        .sortedBy { (angle, _) -> angle }
        .toMutableList()

    var index = 0
    var n = 1
    while(true) {
        val pair = stuff[index]
        if (pair.second.isNotEmpty()) {
            if (n == 200) {
                return stuff[index].second.first() to stuff[index].second.first().let { it.x * 100f + it.y }
            }
            stuff[index] = pair.copy(second = pair.second.drop(1))
            n++
        }
        index = (index + 1) % stuff.size
    }
}

private fun parseAsteroids(input: List<String>) =
    input.mapIndexed { y, row -> row.mapIndexedNotNull { x, c -> if (c == '#') Asteroid(x.f, y.f) else null } }
        .flatten()

private fun angleBetween(origin: Asteroid, asteroid: Asteroid) =
    (atan2(origin.y - asteroid.y, origin.x - asteroid.x) * 180f / PI.f + 270) % 360

private fun distanceBetween(origin: Asteroid, asteroid: Asteroid) =
    sqrt((origin.x - asteroid.x).pow(2) + (origin.y - asteroid.y).pow(2))
