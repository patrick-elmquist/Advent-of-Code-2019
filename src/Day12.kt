import util.Day
import util.Point3d
import util.lcm
import kotlin.math.abs

// Answer #1: 10944
// Answer #2: 484244804958744

fun main() {
    Day(n = 12) {
        answer {
            val steps = generate1000Steps(moons = lines.map { Moon.parse(it) })
            steps.last().second.map { moon -> moon.pos.absSum() * moon.vel.absSum() }.sum()
        }
        answer {
            val state = generateRepeatedSequence(moons = lines.map { Moon.parse(it) }).last()
            lcm(state.x ?: 0, state.y ?: 0, state.z ?: 0)
        }
    }
}

private fun generate1000Steps(moons: List<Moon>) =
    generateSequence(0 to moons) { (n, moons) -> if (n < 1000) n + 1 to step(moons) else null }

private fun generateRepeatedSequence(moons: List<Moon>) =
    generateSequence(State(moons, 1)) { state ->
        val step = step(state.moons)

        val x = step.mapIndexed { i, moon -> moon.pos.x == moons[i].pos.x && moon.vel.x == moons[i].vel.x }.all { it }
        if (x && state.x == null) state.x = state.n

        val y = step.mapIndexed { i, moon -> moon.pos.y == moons[i].pos.y && moon.vel.y == moons[i].vel.y }.all { it }
        if (y && state.y == null) state.y = state.n

        val z = step.mapIndexed { i, moon -> moon.pos.z == moons[i].pos.z && moon.vel.z == moons[i].vel.z }.all { it }
        if (z && state.z == null) state.z = state.n

        state.copy(moons = step, n = state.n + 1).takeIf { state.x == null || state.y == null || state.z == null }
    }

private fun step(moons: List<Moon>) =
    moons.map { moon -> moon to moons.filter { it != moon } }
        .map { (moon, moons) ->
            // Update velocity
            val positions = moons.map { it.pos }
            val velX = positions.map { comparePosition(moon.pos.x, it.x) }.sum()
            val velY = positions.map { comparePosition(moon.pos.y, it.y) }.sum()
            val velZ = positions.map { comparePosition(moon.pos.z, it.z) }.sum()
            val vel = moon.vel.translateBy(velX, velY, velZ)
            val pos = moon.pos.translateBy(vel.x, vel.y, vel.z)
            Moon(pos = pos, vel = vel)
        }

private fun comparePosition(origin: Int, target: Int) =
    when {
        origin < target -> 1
        origin > target -> -1
        else -> 0
    }

private fun Point3d.absSum() = abs(x) + abs(y) + abs(z)

private data class State(val moons: List<Moon>, val n: Int, var x: Int? = null, var y: Int? = null, var z: Int? = null)

private data class Moon(val pos: Point3d, val vel: Point3d = Point3d()) {
    companion object {
        private val trash = listOf('<', '>', ' ')
        fun parse(input: String) =
            input.filterNot { it in trash }
                .split(",")
                .map { it.split("=")[1].toInt() }
                .let { Moon(Point3d(it[0], it[1], it[2])) }
    }
}