package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.drawDirectedEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.drawUndirectedEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.drawVertex
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.util.distanceFromSegment

@Composable
fun GraphCanvas(
    graphicalGraph: GraphicalGraph,
    drawVertex: DrawScope.(GraphicalVertex, Offset, TextMeasurer) -> Unit,
    drawEdge: DrawScope.(GraphicalEdge, Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    onTap: (Double, Double) -> Unit = { _, _ -> },
    onDragStart: (Double, Double) -> Unit = { _, _ -> },
    onDrag: (Double, Double) -> Unit = { _, _ -> },
    onDragEnd: () -> Unit = {},
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
        }.pointerInput(Unit) {
            detectTapGestures { offset ->
                val modelX = offset.x.toDouble() - size.width / 2.0
                val modelY = size.height / 2.0 - offset.y.toDouble()

                onTap(modelX, modelY)
            }
        }.pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    val modelX = offset.x.toDouble() - size.width / 2.0
                    val modelY = size.height / 2.0 - offset.y.toDouble()

                    onDragStart(modelX, modelY)
                },
                onDrag = { _, offset ->
                    val modelX = offset.x.toDouble()
                    val modelY = -offset.y.toDouble()

                    onDrag(modelX, modelY)
                },
                onDragEnd = {
                    onDragEnd()
                },
            )
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

@Composable
fun UndirectedGraphCanvas(
    graphicalGraph: GraphicalGraph,
    modifier: Modifier = Modifier,
    onTap: (Double, Double) -> Unit = { _, _ -> },
    onDragStart: (Double, Double) -> Unit = { _, _ -> },
    onDrag: (Double, Double) -> Unit = { _, _ -> },
    onDragEnd: () -> Unit = {},
) {
    GraphCanvas(
        graphicalGraph = graphicalGraph,
        drawVertex = { vertex, centerOffset, textMeasurer ->
            drawVertex(vertex, centerOffset, textMeasurer)
        },
        drawEdge = { edge, centerX, centerY ->
            drawUndirectedEdge(edge, centerX, centerY)
        },
        modifier = modifier,
        onTap = onTap,
        onDragStart = onDragStart,
        onDrag = onDrag,
        onDragEnd = onDragEnd,
    )
}

@Composable fun DirectedGraphCanvas(
    graphicalGraph: GraphicalGraph,
    modifier: Modifier = Modifier,
    onTap: (Double, Double) -> Unit = { _, _ -> },
    onDragStart: (Double, Double) -> Unit = { _, _ -> },
    onDrag: (Double, Double) -> Unit = { _, _ -> },
    onDragEnd: () -> Unit = {},
) {
    GraphCanvas(
        graphicalGraph = graphicalGraph,
        drawVertex = { vertex, centerOffset, textMeasurer ->
            drawVertex(vertex, centerOffset, textMeasurer)
        },
        drawEdge = { edge, centerX, centerY ->
            drawDirectedEdge(edge, centerX, centerY)
        },
        modifier = modifier,
        onTap = onTap,
        onDragStart = onDragStart,
        onDrag = onDrag,
        onDragEnd = onDragEnd,
    )
}
