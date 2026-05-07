package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.drawEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.drawVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.util.distanceFromSegment

@Composable
fun GraphCanvas(
    graphicalGraph: GraphicalGraph,
    modifier: Modifier = Modifier,
) {
    val vertices = graphicalGraph.graphicalVertices
    val edges = graphicalGraph.graphicalEdges

    val textMeasurer = rememberTextMeasurer()

    var hoverPosition by remember { mutableStateOf<Offset?>(null) }

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    when (event.type) {
                        PointerEventType.Move -> {
                            hoverPosition = event.changes[0].position
                        }

                        PointerEventType.Exit -> {
                            hoverPosition = null
                        }
                    }
                }
            }
        },
    ) {
        val centerX = size.width / 2.0
        val centerY = size.height / 2.0

        for (edge in edges) {
            drawEdge(
                edge,
                centerX,
                centerY,
            )
        }

        for (vertex in vertices) {
            drawVertex(
                vertex,
                Offset(centerX.toFloat(), centerY.toFloat()),
                textMeasurer,
            )
        }

        hoverPosition?.let { hoverPosition ->
            val hoveredEdge = edges
                .associateWith { edge ->
                    distanceFromSegment(
                        hoverPosition,
                        edge.startGraphicalVertex.transformCoordinates(centerX, centerY),
                        edge.endGraphicalVertex.transformCoordinates(centerX, centerY),
                    )
                }
                .filter { it.value < 5f }
                .minByOrNull { it.value }?.key
            if (hoveredEdge != null && hoveredEdge.label != null) {
                val measuredLabel = textMeasurer.measure(
                    text = hoveredEdge.label,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                )

                drawRect(
                    color = Color.Black,
                    topLeft = hoverPosition - Offset(
                        measuredLabel.size.width / 2f + 4f,
                        measuredLabel.size.height + 10f,
                    ),
                    size = Size(measuredLabel.size.width + 8f, measuredLabel.size.height + 8f),
                )

                drawRect(
                    color = Color.White,
                    topLeft = hoverPosition - Offset(
                        measuredLabel.size.width / 2f + 3f,
                        measuredLabel.size.height + 9f,
                    ),
                    size = Size(measuredLabel.size.width + 6f, measuredLabel.size.height + 6f),
                )

                drawText(
                    measuredLabel,
                    topLeft = hoverPosition - Offset(
                        measuredLabel.size.width / 2f,
                        measuredLabel.size.height + 6f,
                    ),
                )
            }
        }
    }
}
