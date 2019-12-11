import extension.asLongs
import extension.csv
import util.Day
import util.IntCode

// Answer #1: 1909
// Answer #2:
//   ## #  # #### #### #  # #  # ###  #  #
//    # #  # #    #    # #  #  # #  # #  #
//    # #  # ###  ###  ##   #### #  # ####
//    # #  # #    #    # #  #  # ###  #  #
// #  # #  # #    #    # #  #  # #    #  #
//  ##   ##  #    #### #  # #  # #    #  #


fun main() {
    Day(n = 11) {
        answer {
            val program = IntCode(lines.first().csv.asLongs())
            val canvas = Canvas()
            val start = Position(0, 0)
            runPaintProgram(program, canvas, start)
            canvas.paintedPixels
        }
        answer {
            val program = IntCode(lines.first().csv.asLongs())
            val start = Position(0, 0)
            val canvas = Canvas().apply { set(start, 1) }
            runPaintProgram(program, canvas, start)

            val (minX, maxX) = canvas.pixels.keys.map { it.x }.let { (it.min() ?: 0) to (it.max() ?: 0) }
            val (minY, maxY) = canvas.pixels.keys.map { it.y }.let { (it.min() ?: 0) to (it.max() ?: 0) }
            val sb = StringBuilder("\n")
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    sb.append(if (canvas.pixels[Position(x, y)] == 1) '#' else ' ')
                }
                sb.append("\n")
            }
            sb.toString()
        }
    }
}

private fun runPaintProgram(program: IntCode, canvas: Canvas, start: Position) {
    var position = start
    var dir = Direction.UP
    while (!program.hasTerminated) {
        program.run(canvas[position].toLong())
        canvas[position] = program.outputs[0].toInt()
        dir = dir.nextDir(program.outputs[1].toInt())
        position = dir.nextPosition(position)
    }
}

private class Canvas() {
    private val _pixels = mutableMapOf<Position, Int>()
    val pixels: Map<Position, Int> = _pixels
    val paintedPixels: Int
        get() = _pixels.keys.count()
    operator fun get(index: Position): Int = _pixels.getOrPut(index, { 0 })
    operator fun set(index: Position, value: Int) = _pixels.set(index, value)
}

private data class Position(val x: Int, val y: Int)

private enum class Direction {
    LEFT, UP, RIGHT, DOWN;

    fun nextDir(turn: Int) = when (this) {
        LEFT -> if (turn == 0) DOWN else UP
        UP -> if (turn == 0) LEFT else RIGHT
        RIGHT -> if (turn == 0) UP else DOWN
        DOWN -> if (turn == 0) RIGHT else LEFT
    }

    fun nextPosition(current: Position) = when (this) {
        LEFT -> current.copy(x = current.x - 1)
        UP -> current.copy(y = current.y - 1)
        RIGHT -> current.copy(x = current.x + 1)
        DOWN -> current.copy(y = current.y + 1)
    }
}
