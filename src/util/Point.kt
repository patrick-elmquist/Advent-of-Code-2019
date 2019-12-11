package util

import extension.f
import extension.pow
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

data class Point(val x: Int = 0, val y: Int = 0) {
    fun toPointF() = PointF(x.f, y.f)
}

data class PointF(val x: Float = 0f, val y: Float = 0f) {
    fun toPoint(round: Boolean = false) =
        if (round) Point(x.roundToInt(), y.roundToInt()) else Point(x.toInt(), y.toInt())
}

fun Point.angleTo(target: Point) = toPointF().angleTo(target.toPointF())
fun PointF.angleTo(target: PointF) = atan2(y - target.y, x - target.x) * 180f / PI.f

fun Point.distanceTo(target: Point) = toPointF().distanceTo(target.toPointF())
fun PointF.distanceTo(target: PointF) = sqrt((x - target.x).pow(2) + (y - target.y).pow(2))