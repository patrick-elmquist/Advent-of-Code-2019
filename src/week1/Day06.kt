package week1

import util.Day

// Answer #1: 344238
// Answer #2: 436

fun main() {
    Day(n = 6) {
        answer {
            val orbits = lines.map { it.split(")") }.associate { it[1] to it[0] }
            orbits.keys.sumBy { planet -> pathToRoot(planet, orbits).count() - 1 }
        }
        answer {
            val orbits = lines.map { it.split(")") }.associate { it[1] to it[0] }
            val you = pathToRoot(orbits["YOU"], orbits).toList()
            val santa = pathToRoot(orbits["SAN"], orbits).toList()
            you.size + santa.size - 2 * you.intersect(santa).size
        }
    }
}

private fun pathToRoot(id: String?, map: Map<String, String>) = generateSequence(id) { map[it] }
