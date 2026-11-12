package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.graphcanvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.HighlightType
import kotlin.math.abs

fun DrawScope.drawVertex(
    vertex: GraphicalVertex,
    canvasCenter: Vector2D,
    textMeasurer: TextMeasurer,
) {
    val vertexCenter =
        vertex.position.toCanvasCoordinates(canvasCenter).toOffset()

    val radius = vertex.radius

    val highlightReversed = vertex.highlight.reversed()

    when (vertex.highlightType) {
        HighlightType.CIRCLE -> {
            highlightReversed.forEachIndexed { index, highlightColor ->
                drawCircle(
                    color = highlightColor,
                    radius = radius + (highlightReversed.size - index) * 5f,
                    center = vertexCenter,
                )
            }
        }

        HighlightType.SQUARE -> {
            highlightReversed.forEachIndexed { index, highlightColor ->

                val halfSideLength = radius + (highlightReversed.size - index) * 5f

                drawRect(
                    color = highlightColor,
                    size = Size(halfSideLength * 2f, halfSideLength * 2f),
                    topLeft = vertexCenter - Offset(halfSideLength, halfSideLength),
                )
            }
            drawRect(
                color = Color.White,
                size = Size(radius * 2f, radius * 2f),
                topLeft = vertexCenter - Offset(radius, radius),
            )
        }
    }

    drawCircle(
        color = Color.Black,
        radius = radius,
        center = vertexCenter,
    )
    drawCircle(
        color = vertex.innerColor,
        radius = radius - 3f,
        center = vertexCenter,
    )

    val measuredText =
        textMeasurer.measure(
            text = vertex.name,
            style =
                TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                ),
            constraints = Constraints(maxWidth = vertex.maxTextSize),
        )
    drawText(
        measuredText,
        topLeft =
            vertexCenter -
                Offset(
                    measuredText.size.width / 2f,
                    measuredText.size.height / 2f,
                ),
    )

    if (vertex.label.isNotEmpty()) {
        val direction = (vertex.position.y / (abs(vertex.position.y))).toFloat() * -1f
        val labelCenter = vertexCenter + Offset(0f, (radius + 20f) * direction)

        val measuredLabel = textMeasurer.measure(
            text = vertex.label,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            ),
        )
        drawText(
            measuredLabel,
            topLeft = labelCenter - Offset(
                measuredLabel.size.width / 2f,
                measuredLabel.size.height / 2f,
            ),
        )
    }
}
