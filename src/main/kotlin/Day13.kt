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
            val game = IntCode(memory, debug = false).apply { run() }
            generateGameSequence(game, Canvas(game)).last()
        }
    }
}

private fun generateGameSequence(game: IntCode, canvas: Canvas) = generateSequence(0) { score ->
    if (game.hasTerminated) return@generateSequence null
    game.run(calculatePaddleMovement(canvas.paddlePosition, canvas.ballPosition))
    canvas.render(score)
}

private fun calculatePaddleMovement(paddle: Point, ball: Point) = when {
    paddle.x < ball.x -> 1L
    paddle.x > ball.x -> -1L
    else -> 0L
}

private data class Canvas(private val game: IntCode) {
    private val spriteMap = mapOf(0 to ' ', 1 to '@', 2 to '#', 3 to '-', 4 to 'o')
    private val tiles: CharArray
    private val width: Int

    val ballPosition: Point get() = find('o')
    val paddlePosition: Point get() = find('-')

    init {
        val initialState = game.updates
        width = (initialState.map { it[0] }.max() ?: 0) + 1
        val height = initialState.size / width
        tiles = CharArray(width * height) { ' ' }
        render(0)
    }

    fun render(currentScore: Int) = game.updates
        .fold(currentScore) { score, instr ->
            if (instr[0] == -1 && instr[1] == 0) {
                instr[2]
            } else {
                tiles[instr[0] + instr[1] * width] = spriteMap[instr[2]] ?: error("No sprite for ${instr[2]}")
                score
            }
        }

    private val IntCode.updates get() = outputs.map { it.toInt() }.chunked(3)
    private fun find(tile: Char) = tiles.indexOf(tile).let { Point(it % width, it / width) }

    override fun toString() =
        tiles.toList().chunked(width).joinToString("\n") { it.joinToString("") }}
