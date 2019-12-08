import util.Day

// Answer #1: 2016
// Answer #2:
// #  # ####  ##  #### #  #
// #  #    # #  #    # #  #
// ####   #  #      #  #  #
// #  #  #   #     #   #  #
// #  # #    #  # #    #  #
// #  # ####  ##  ####  ##

private const val WIDTH = 25
private const val HEIGHT = 6
fun main() {
    Day(n = 8) {
        answer {
            lines.first().chunked(WIDTH * HEIGHT)
                .map { layer -> layer.count { it == '0' } to layer.count { it == '1' } * layer.count { it == '2' } }
                .minBy { it.first }
                ?.second
        }
        answer {
            lines.first().chunked(WIDTH * HEIGHT)
                .reversed()
                .fold(MutableList(WIDTH * HEIGHT) { '2' }) { image, layer -> applyLayer(image, layer) }
                .map { if (it == '1') '#' else ' ' }
                .chunked(WIDTH) { row -> row.joinToString("") }
                .joinToString("\n", prefix = "\n")
        }
    }
}

private fun applyLayer(image: MutableList<Char>, layer: String) =
    layer.foldIndexed(image) { i, img, c -> img.also { img[i] = if (c != '2') c else img[i] } }
