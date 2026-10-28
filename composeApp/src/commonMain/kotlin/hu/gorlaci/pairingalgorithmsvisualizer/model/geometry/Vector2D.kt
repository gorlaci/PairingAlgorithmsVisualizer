package hu.gorlaci.pairingalgorithmsvisualizer.model.geometry

import androidx.compose.ui.geometry.Offset

data class Vector2D(
    val x: Double,
    val y: Double,
) {
    operator fun plus(other: Vector2D): Vector2D = Vector2D(x + other.x, y + other.y)

    operator fun minus(other: Vector2D): Vector2D = Vector2D(x - other.x, y - other.y)

    operator fun times(scalar: Double): Vector2D = Vector2D(x * scalar, y * scalar)

    operator fun div(scalar: Double): Vector2D = Vector2D(x / scalar, y / scalar)

    fun rotate(degrees: Double): Vector2D {
        val radians = degrees * (kotlin.math.PI / 180.0)
        val cosAngle = kotlin.math.cos(radians)
        val sinAngle = kotlin.math.sin(radians)
        return Vector2D(
            x * cosAngle - y * sinAngle,
            x * sinAngle + y * cosAngle,
        )
    }

    fun toCanvasCoordinates(
        canvasCenterX: Double,
        canvasCenterY: Double,
    ): Vector2D = Vector2D(canvasCenterX + x, canvasCenterY - y)

    fun length(): Double = kotlin.math.sqrt(x * x + y * y)

    fun toOffset() = Offset(x.toFloat(), y.toFloat())

    fun distanceTo(other: Vector2D) = (this - other).length()
}
