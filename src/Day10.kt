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
        answer { findOptimalAsteroidForStation(Asteroid.parseList(lines)) }
        answer { vaporizeAsteroids(Asteroid.parseList(lines)) }
    }
}

private fun findOptimalAsteroidForStation(asteroids: List<Asteroid>) =
    asteroids
        .map { origin ->
            origin to asteroids.asSequence()
                .filter { it != origin }
                .map { origin.angleTo(it) }
                .distinct()
                .count()
        }
        .maxBy { (_, count) -> count }

private fun vaporizeAsteroids(asteroids: List<Asteroid>): Pair<Asteroid, Float>? {
    val station = findOptimalAsteroidForStation(asteroids)?.first ?: throw IllegalStateException("Sink ship")
    val asteroidsForAngle = asteroids.asSequence()
        .filter { it != station }
        .map { it to station.angleTo(it) }
        .groupBy { (_, angle) -> angle }
        .map { (angle, asteroids) ->
            angle to asteroids
                .map { it.first }
                .sortedBy { station.distanceTo(it) }
        }
        .sortedBy { (angle, _) -> angle }
        .map { (_, asteroids) -> asteroids }
        .toMutableList()

    var index = 0
    var vaporized = 0
    while(true) {
        val pair = asteroidsForAngle[index]
        if (pair.isNotEmpty()) {
            vaporized++
            if (vaporized == 200) break else asteroidsForAngle[index] = pair.drop(1)
        }
        index = (index + 1) % asteroidsForAngle.size
    }
    return asteroidsForAngle[index].first() to asteroidsForAngle[index].first().let { it.x * 100f + it.y }
}

data class Asteroid(val x: Float, val y: Float) {
    fun angleTo(asteroid: Asteroid) = (atan2(y - asteroid.y, x - asteroid.x) * 180f / PI.f + 270) % 360
    fun distanceTo(asteroid: Asteroid) = sqrt((x - asteroid.x).pow(2) + (y - asteroid.y).pow(2))

    companion object {
        fun parseList(input: List<String>) = input
            .mapIndexed { y, row -> row.mapIndexedNotNull { x, c -> if (c == '#') Asteroid(x.f, y.f) else null } }
            .flatten()
    }
}
