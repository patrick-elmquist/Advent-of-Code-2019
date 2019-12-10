import util.Day
import kotlin.math.atan2

// Answer #1:
// Answer #2:

fun main() {
    val test1 = (".#..#\n" +
            ".....\n" +
            "#####\n" +
            "....#\n" +
            "...##").split("\n")
    val test2 = ("......#.#.\n" +
            "#..#.#....\n" +
            "..#######.\n" +
            ".#.#.###..\n" +
            ".#..#.....\n" +
            "..#....#.#\n" +
            "#..#....#.\n" +
            ".##.#..###\n" +
            "##...#..#.\n" +
            ".#....####").split("\n")

    val test3 = ("#.#...#.#.\n" +
            ".###....#.\n" +
            ".#....#...\n" +
            "##.#.#.#.#\n" +
            "....#.#.#.\n" +
            ".##..###.#\n" +
            "..#...##..\n" +
            "..##....##\n" +
            "......#...\n" +
            ".####.###.").split("\n")

    val test4 = (".#..#..###\n" +
            "####.###.#\n" +
            "....###.#.\n" +
            "..###.##.#\n" +
            "##.##.#.#.\n" +
            "....###..#\n" +
            "..#.#..#.#\n" +
            "#..#.#.###\n" +
            ".##...##.#\n" +
            ".....#.#..").split("\n")

    val test5 = (".#..##.###...#######\n" +
            "##.############..##.\n" +
            ".#.######.########.#\n" +
            ".###.#######.####.#.\n" +
            "#####.##.#.##.###.##\n" +
            "..#####..#.#########\n" +
            "####################\n" +
            "#.####....###.#.#.##\n" +
            "##.#################\n" +
            "#####.##.###..####..\n" +
            "..######..##.#######\n" +
            "####.##.####...##..#\n" +
            ".#####..#.######.###\n" +
            "##...#.##########...\n" +
            "#.##########.#######\n" +
            ".####.#.###.###.#.##\n" +
            "....##.##.###..#####\n" +
            ".#.#.###########.###\n" +
            "#.#.#.#####.####.###\n" +
            "###.##.####.##.#..##").split("\n")

    Day(n = 10) {
        answer {
            println(part1(test1))
            check(part1(test1)?.first == Asteroid(3f, 4f), { "Test 1 failed" })
            check(part1(test2)?.first == Asteroid(5f, 8f), { "Test 2 failed" })
            check(part1(test3)?.first == Asteroid(1f, 2f), { "Test 3 failed" })
            check(part1(test4)?.first == Asteroid(6f, 3f), { "Test 4 failed" })
            check(part1(test5)?.first == Asteroid(11f, 13f), { "Test 5 failed" })
            part1(lines)
        }
        answer {

        }
    }
}

private fun part1(input: List<String>): Pair<Asteroid, Int>? {
    val asteroids = input.mapIndexedNotNull { y, row ->
        row.mapIndexedNotNull { x, column ->
            if (column == '#') {
                Asteroid(x.toFloat(), y.toFloat())
            } else {
                null
            }
        }
    }.flatten()

    val asteroidsMap = asteroids.map { outer ->
        outer to asteroids.map { inner ->
            inner to atan2(outer.y - inner.y, outer.x - inner.x)
        }.filter { it.first != outer }
    }

    val asteroidCount = asteroidsMap.map {
        it.first to it.second.fold(mutableSetOf<Float>()) { set, input ->
            set.apply { add(input.second) }
        }.count()
    }
    return asteroidCount.maxBy { it.second }
}

data class Asteroid(val x: Float, val y: Float)