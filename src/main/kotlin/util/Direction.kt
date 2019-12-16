package util

enum class Direction(val n: Long) {
    NORTH(1), SOUTH(2), WEST(3), EAST(4);

    fun cw() = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun ccw() = when (this) {
        NORTH -> WEST
        EAST -> NORTH
        SOUTH -> EAST
        WEST -> SOUTH
    }

    private fun perpendicular() = when (this) {
        NORTH -> listOf(WEST, EAST)
        SOUTH -> listOf(WEST, EAST)
        WEST -> listOf(NORTH, SOUTH)
        EAST -> listOf(NORTH, SOUTH)
    }

    companion object {
        fun from(n: Long) = values().find { it.n == n } ?: error("What did you do? $n")
    }
}
