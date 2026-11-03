package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.graphcanvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalVertex
import hu.gorlaci.pairingalgorithmsvisualizer.util.distanceFromSegment

@Composable
fun GraphCanvasBase(
    graphicalGraph: GraphicalGraph,
    drawVertex: DrawScope.(GraphicalVertex, Vector2D, TextMeasurer) -> Unit,
    drawEdge: DrawScope.(GraphicalEdge, Vector2D) -> Unit,
    modifier: Modifier = Modifier,
    onTap: (Vector2D) -> Unit = { _ -> },
    onDragStart: (Vector2D) -> Unit = { _ -> },
    onDrag: (Vector2D) -> Unit = { _ -> },
    onDragEnd: () -> Unit = {},
) {
    val vertices = graphicalGraph.graphicalVertices
    val edges = graphicalGraph.graphicalEdges

    val textMeasurer = rememberTextMeasurer()

    var hoverPosition by remember { mutableStateOf<Vector2D?>(null) }

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    when (event.type) {
                        PointerEventType.Move -> {
                            hoverPosition = Vector2D(event.changes[0].position)
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

                onTap(Vector2D(modelX, modelY))
            }
        }.pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    val modelX = offset.x.toDouble() - size.width / 2.0
                    val modelY = size.height / 2.0 - offset.y.toDouble()

                    onDragStart(Vector2D(modelX, modelY))
                },
                onDrag = { _, offset ->
                    val modelX = offset.x.toDouble()
                    val modelY = -offset.y.toDouble()

                    onDrag(Vector2D(modelX, modelY))
                },
                onDragEnd = {
                    onDragEnd()
                },
            )
        },
    ) {
        val center = Vector2D(size.width / 2.0, size.height / 2.0)

        for (edge in edges) {
            drawEdge(
                edge,
                center,
            )
        }

        for (vertex in vertices) {
            drawVertex(
                vertex,
                center,
                textMeasurer,
            )
        }

        hoverPosition?.let { hoverPosition ->
            val hoveredEdge = edges
                .associateWith { edge ->
                    distanceFromSegment(
                        hoverPosition,
                        edge.startGraphicalVertex.position.toCanvasCoordinates(center),
                        edge.endGraphicalVertex.position.toCanvasCoordinates(center),
                    )
                }
                .filter { it.value < 5f }
                .minByOrNull { it.value }?.key
            if (hoveredEdge?.label?.isNotEmpty() ?: false) {
                val measuredLabel = textMeasurer.measure(
                    text = hoveredEdge.label,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                )

                drawRect(
                    color = Color.Black,
                    topLeft = (
                        hoverPosition - Vector2D(
                            measuredLabel.size.width / 2.0 + 4.0,
                            measuredLabel.size.height + 10.0,
                        )
                        ).toOffset(),
                    size = Size(measuredLabel.size.width + 8f, measuredLabel.size.height + 8f),
                )

                drawRect(
                    color = Color.White,
                    topLeft = (
                        hoverPosition - Vector2D(
                            measuredLabel.size.width / 2.0 + 3.0,
                            measuredLabel.size.height + 9.0,
                        )
                        ).toOffset(),
                    size = Size(measuredLabel.size.width + 6f, measuredLabel.size.height + 6f),
                )

                drawText(
                    measuredLabel,
                    topLeft = (
                        hoverPosition - Vector2D(
                            measuredLabel.size.width / 2.0,
                            measuredLabel.size.height + 6.0,
                        )
                        ).toOffset(),
                )
            }
        }
    }
}

@Composable
fun UndirectedGraphCanvas(
    graphicalGraph: GraphicalGraph,
    modifier: Modifier = Modifier,
    onTap: (Vector2D) -> Unit = { _ -> },
    onDragStart: (Vector2D) -> Unit = { _ -> },
    onDrag: (Vector2D) -> Unit = { _ -> },
    onDragEnd: () -> Unit = {},
) {
    GraphCanvasBase(
        graphicalGraph = graphicalGraph,
        drawVertex = { vertex, canvasCenter, textMeasurer ->
            drawVertex(vertex, canvasCenter, textMeasurer)
        },
        drawEdge = { edge, canvasCenter ->
            drawUndirectedEdge(edge, canvasCenter)
        },
        modifier = modifier,
        onTap = onTap,
        onDragStart = onDragStart,
        onDrag = onDrag,
        onDragEnd = onDragEnd,
    )
}

@Composable
fun DirectedGraphCanvas(
    graphicalGraph: GraphicalGraph,
    modifier: Modifier = Modifier,
    onTap: (Vector2D) -> Unit = { _ -> },
    onDragStart: (Vector2D) -> Unit = { _ -> },
    onDrag: (Vector2D) -> Unit = { _ -> },
    onDragEnd: () -> Unit = {},
) {
    GraphCanvasBase(
        graphicalGraph = graphicalGraph,
        drawVertex = { vertex, centerOffset, textMeasurer ->
            drawVertex(vertex, centerOffset, textMeasurer)
        },
        drawEdge = { edge, canvasCenter ->
            drawDirectedEdge(edge, canvasCenter)
        },
        modifier = modifier,
        onTap = onTap,
        onDragStart = onDragStart,
        onDrag = onDrag,
        onDragEnd = onDragEnd,
    )
}

@Composable
fun GraphCanvas(
    graphicalGraph: GraphicalGraph,
    directed: Boolean,
    modifier: Modifier = Modifier,
    onTap: (Vector2D) -> Unit = { _ -> },
    onDragStart: (Vector2D) -> Unit = { _ -> },
    onDrag: (Vector2D) -> Unit = { _ -> },
    onDragEnd: () -> Unit = {},
) {
    if (directed) {
        DirectedGraphCanvas(
            graphicalGraph = graphicalGraph,
            modifier = modifier,
            onTap = onTap,
            onDragStart = onDragStart,
            onDrag = onDrag,
            onDragEnd = onDragEnd,
        )
    } else {
        UndirectedGraphCanvas(
            graphicalGraph = graphicalGraph,
            modifier = modifier,
            onTap = onTap,
            onDragStart = onDragStart,
            onDrag = onDrag,
            onDragEnd = onDragEnd,
        )
    }
}
