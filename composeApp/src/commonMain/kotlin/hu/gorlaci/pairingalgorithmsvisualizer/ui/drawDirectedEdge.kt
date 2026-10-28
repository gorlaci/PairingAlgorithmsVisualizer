package hu.gorlaci.pairingalgorithmsvisualizer.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge

fun DrawScope.drawDirectedEdge(
    edge: GraphicalEdge,
    canvasCenterX: Double = 0.0,
    canvasCenterY: Double = 0.0,
) {
    val centerOffsetMultiplier = 2.0

    val startVector = Vector2D(
        edge.startGraphicalVertex.x,
        edge.startGraphicalVertex.y,
    ).toCanvasCoordinates(canvasCenterX, canvasCenterY)

    val endVector = Vector2D(
        edge.endGraphicalVertex.x,
        edge.endGraphicalVertex.y,
    ).toCanvasCoordinates(canvasCenterX, canvasCenterY)

    val center = (startVector + endVector) / 2.0 + ((endVector - startVector).rotate(90.0) * centerOffsetMultiplier)

    val radius = (startVector - center).length().toFloat()

    val startAngleDegrees = (startVector - center).let {
        val angle = kotlin.math.atan2(it.y, it.x) * (180.0 / kotlin.math.PI)
        angle.toFloat()
    }.let { if (it < 0f) it + 360f else it }

    val endAngleDegrees = (endVector - center).let {
        val angle = kotlin.math.atan2(it.y, it.x) * (180.0 / kotlin.math.PI)
        angle.toFloat()
    }.let { if (it < 0f) it + 360f else it }

    val sweepAngleDegrees = (endAngleDegrees - startAngleDegrees).let { if (it < 0f) it + 360f else it }

    val lineWidth = if (edge.selected) 8f else 3f

    drawArc(
        color = edge.highlight,
        startAngle = startAngleDegrees,
        sweepAngle = sweepAngleDegrees,
        useCenter = false,
        topLeft = Offset(
            center.x.toFloat() - (radius + lineWidth / 2f),
            center.y.toFloat() - (radius + lineWidth / 2f),
        ),
        size = Size(radius * 2 + lineWidth, radius * 2 + lineWidth),
        style = Stroke(width = 15f),
    )

    drawArc(
        color = edge.color,
        startAngle = startAngleDegrees,
        sweepAngle = sweepAngleDegrees,
        useCenter = false,
        topLeft = Offset(
            center.x.toFloat() - (radius + lineWidth / 2f),
            center.y.toFloat() - (radius + lineWidth / 2f),
        ),
        size = Size(radius * 2 + lineWidth, radius * 2 + lineWidth),
        style = Stroke(width = lineWidth),
    )

    val arrowWidth = if (edge.selected) 12.0 else 10.0

    val triangleCenterDegrees = startAngleDegrees + (sweepAngleDegrees / 2.0)

    val triangleCenter =
        center +
            Vector2D(
                (radius + lineWidth / 2f).toDouble(),
                0.0,
            ).rotate(triangleCenterDegrees)
    val triangleUp = Vector2D(arrowWidth, 0.0).rotate(triangleCenterDegrees)
    val triangleRight = triangleUp.rotate(90.0)

    val triangle = Path().apply {
        val tip = triangleCenter + triangleRight
        moveTo(tip.x.toFloat(), tip.y.toFloat())

        val up = triangleCenter + triangleUp - triangleRight
        lineTo(up.x.toFloat(), up.y.toFloat())

        val down = triangleCenter - triangleUp - triangleRight
        lineTo(down.x.toFloat(), down.y.toFloat())
        close()
    }

    drawPath(
        path = triangle,
        color = edge.color,
    )
}
