import extension.asLongs
import extension.csv
import util.Day
import util.IntCode
import util.Point

// Answer #1: 1909
// Answer #2:
//   ## #  # #### #### #  # #  # ###  #  #
//    # #  # #    #    # #  #  # #  # #  #
//    # #  # ###  ###  ##   #### #  # ####
//    # #  # #    #    # #  #  # ###  #  #
// #  # #  # #    #    # #  #  # #    #  #
//  ##   ##  #    #### #  # #  # #    #  #

private const val BLACK = 0
private const val WHITE = 1

fun main() {
    Day(n = 11) {
        answer {
            val program = IntCode(lines.first().csv.asLongs())
            val canvas = mutableMapOf<Point, Int>()
            draw(canvas, program, Point(0, 0))
            canvas.keys.count()
        }
        answer {
            val program = IntCode(lines.first().csv.asLongs())
            val start = Point(0, 0)
            val canvas = mutableMapOf<Point, Int>().apply { set(start, WHITE) }
            draw(canvas, program, start)
            render(canvas)
        }
    }
}

private fun draw(canvas: MutableMap<Point, Int>, program: IntCode, start: Point) {
    var position = start
    var direction = Direction.UP
    while (!program.hasTerminated) {
        program.run(canvas.getOrPut(position, { BLACK }).toLong())
        canvas[position] = program.outputs[0].toInt()
        direction = direction.turn(program.outputs[1].toInt())
        position = position.move(direction)
    }
}

private fun render(canvas: MutableMap<Point, Int>): String = StringBuilder("\n").apply {
    val (minX, maxX) = canvas.keys.map { it.x }.let { (it.min() ?: 0) to (it.max() ?: 0) }
    val (minY, maxY) = canvas.keys.map { it.y }.let { (it.min() ?: 0) to (it.max() ?: 0) }
    for (y in minY..maxY) {
        for (x in minX..maxX) append(if (canvas[Point(x, y)] == 1) '#' else ' ')
        append("\n")
    }
}.toString()

private enum class Direction {
    LEFT, UP, RIGHT, DOWN;

    fun turn(turn: Int) = when (this) {
        LEFT -> if (turn == 0) DOWN else UP
        UP -> if (turn == 0) LEFT else RIGHT
        RIGHT -> if (turn == 0) UP else DOWN
        DOWN -> if (turn == 0) RIGHT else LEFT
    }
}

private fun Point.move(direction: Direction) = when (direction) {
    Direction.LEFT -> copy(x = x - 1)
    Direction.UP -> copy(y = y - 1)
    Direction.RIGHT -> copy(x = x + 1)
    Direction.DOWN -> copy(y = y + 1)
}
