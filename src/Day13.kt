import util.Day
import util.IntCode
import extension.asLongs
import extension.csv
import util.Point

// Answer #1: 462
// Answer #2: 23981

fun main() {
    Day(n = 13) {
        answer {
            IntCode(lines.first().csv.asLongs()).apply { run() }
                .outputs
                .chunked(3)
                .filter { it[2] == 2L }
                .count()
        }
        answer {
            val memory = lines.first().csv.asLongs().toMutableList().also { it[0] = 2 }
            val engine = IntCode(memory, debug = false)
            val canvas = createCanvas(engine)
            
            println(canvas)
            while (!engine.hasTerminated) {
                engine.run(calculatePaddleMovement(canvas.findPaddle(), canvas.findBall()))
                canvas.render(engine)
            }
            score
        }
    }
}

private var score = -1

private fun calculatePaddleMovement(paddle: Point, ball: Point) = when {
    paddle.x < ball.x -> 1L
    paddle.x > ball.x -> -1L
    else -> 0L
}

private fun createCanvas(engine: IntCode): Canvas {
    engine.run() // Get the initial state

    val tiles = engine.outputs.chunked(3)
    val width = (tiles.map { it[0].toInt() }.max() ?: 0) + 1
    val height = tiles.size / width
    val canvas = Canvas(width, height)

    canvas.render(engine) // Populate the canvas with the initial state
    return canvas
}

private data class Canvas(val width: Int, val height: Int) {
    private val spriteMap = mapOf(0 to ' ', 1 to '@', 2 to '#', 3 to '-', 4 to 'o')
    private val tiles: CharArray = CharArray(width * height) { ' ' }

    fun set(coord: Pair<Int, Int>, tile: Int) {
        tiles[coord.second * width + coord.first] = spriteMap[tile] ?: error("No sprite for $tile")
    }

    fun render(engine: IntCode) =
        engine.outputs
            .map { it.toInt() }
            .chunked(3)
            .fold(score) { score, instr ->
                if (instr[0] == -1 && instr[1] == 0) {
                    instr[2]
                } else {
                    set(instr[0] to instr[1], instr[2])
                    score
                }
            }.let { score = it }

    fun findBall() = find('o')
    fun findPaddle() = find('-')

    private fun find(tile: Char) = tiles.indexOf(tile).let { Point(it % width, it / width) }

    override fun toString() =
        tiles.toList().chunked(width).joinToString("\n") { it.joinToString("") }}
