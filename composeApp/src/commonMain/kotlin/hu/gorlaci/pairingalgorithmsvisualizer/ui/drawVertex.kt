package hu.gorlaci.pairingalgorithmsvisualizer.ui

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
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.HighlightType
import kotlin.math.abs

fun DrawScope.drawVertex(
    vertex: GraphicalVertex,
    canvasCenter: Offset,
    textMeasurer: TextMeasurer,
) {
    val vertexCenter =
        vertex.transformCoordinates(canvasCenter.x.toDouble(), canvasCenter.y.toDouble())

    val radius = vertex.radiusInFloat

    when (vertex.highlightType) {
        HighlightType.CIRCLE -> {
            drawCircle(
                color = vertex.highlight,
                radius = radius + 5f,
                center = vertexCenter,
            )
        }

        HighlightType.SQUARE -> {
            drawRect(
                color = vertex.highlight,
                size = Size((radius + 5f) * 2f, (radius + 5f) * 2f),
                topLeft = vertexCenter - Offset(radius + 5f, radius + 5f),
            )
            drawRect(
                color = Color.White,
                size = Size(radius * 2f, radius * 2f),
                topLeft = vertexCenter - Offset(radius, radius),
            )
        }

        HighlightType.DOUBLE_CIRCLE -> {
            drawCircle(
                color = vertex.highlight,
                radius = radius + 15f,
                center = vertexCenter,
            )
            drawCircle(
                color = Color.White,
                radius = radius + 10f,
                center = vertexCenter,
            )
            drawCircle(
                color = vertex.highlight,
                radius = radius + 5f,
                center = vertexCenter,
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

    if (vertex.label != null) {
        val direction = (vertex.y / (abs(vertex.y))).toFloat() * -1f
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
