package util

enum class Direction {
    LEFT, UP, RIGHT, DOWN;

    fun turn(turn: Int) = when (this) {
        LEFT -> if (turn == 0) DOWN else UP
        UP -> if (turn == 0) LEFT else RIGHT
        RIGHT -> if (turn == 0) UP else DOWN
        DOWN -> if (turn == 0) RIGHT else LEFT
    }
}


enum class CardinalDirection(val n: Long) {
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
        NORTH, SOUTH -> listOf(WEST, EAST)
        WEST, EAST -> listOf(NORTH, SOUTH)
    }

    companion object {
        fun from(n: Long) = values().find { it.n == n } ?: error("What did you do? $n")
    }
}
