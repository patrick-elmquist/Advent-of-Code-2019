import extension.asLongs
import extension.csv
import javafx.geometry.Pos
import util.Day
import util.IntCode

// Answer #1:
// Answer #2:

private const val BLACK = 0
private const val WHITE = 1
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

fun main() {
    Day(n = 11) {
        answer {
            var position = Position(0, 0)
            var dir = Direction.UP
            val canvas = Canvas()
            val program = IntCode(lines.first().csv.asLongs())
            val paintedPositions = mutableSetOf<Position>()
            while (!program.hasTerminated) {
                val color = canvas[position]
                program.run(color.toLong())
                val newColor = program.outputs[0].toInt()
                val turn = program.outputs[1].toInt()

                paintedPositions.add(position)
                canvas[position] = newColor
                dir = dir.nextDir(turn)
                position = dir.nextPosition(position)
            }
            paintedPositions.size
        }
        answer {
            var position = Position(0, 0)
            var dir = Direction.UP
            val canvas = Canvas().apply { set(position, WHITE) }
            val program = IntCode(lines.first().csv.asLongs(), debug = false)
            val paintedPositions = mutableSetOf<Position>()
            while (!program.hasTerminated) {
                val color = canvas[position]
                program.run(color.toLong())
                val newColor = program.outputs[0].toInt()
                val turn = program.outputs[1].toInt()

                paintedPositions.add(position)
                canvas[position] = newColor
                dir = dir.nextDir(turn)
                position = dir.nextPosition(position)
            }

            val xValues = canvas.pixels.keys.map { it.x }
            val yValues = canvas.pixels.keys.map { it.y }
            val minX = xValues.min() ?: 0
            val maxX = xValues.max() ?: 0
            val width = maxX - minX
            val minY = yValues.min() ?: 0
            val maxY = yValues.max() ?: 0
            val height = maxY - minY

            val xOffset = if (minX < 0) minX * -1 else 0
            val yOffset = if (minY < 0) minY * -1 else 0

            val pixels = canvas.pixels.map { (position, color) ->
                position.copy(x = position.x + xOffset, y = position.y + yOffset) to color
            }.associate { it.first to it.second }
            println(minY)
            println(maxY)
            for (y in 0..height) {
                for (x in 0..width) {
                    val color = pixels[Position(x, y)]
                    print(if (color == WHITE) '#' else ' ')
                }
                println()
            }

            ""
        }
    }
}


private class Canvas() {
    private val _pixels = mutableMapOf<Position, Int>()
    val pixels: Map<Position, Int> = _pixels

    operator fun get(index: Position): Int = _pixels.getOrPut(index, { BLACK })
    operator fun set(index: Position, value: Int) = _pixels.set(index, value)
}

private data class Position(val x: Int, val y: Int)
