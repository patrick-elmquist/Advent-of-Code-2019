package util

enum class Direction {
    LEFT, UP, RIGHT, DOWN;

    fun turn(turn: Int) = when (this) {
        LEFT -> if (turn == 0) DOWN else UP
        UP -> if (turn == 0) LEFT else RIGHT
        RIGHT -> if (turn == 0) UP else DOWN
        DOWN -> if (turn == 0) RIGHT else LEFT
    }

    fun turnLeft() = turn(0)
    fun turnRight() = turn(1)
}
