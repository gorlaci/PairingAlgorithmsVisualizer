package hu.gorlaci.pairingalgorithmsvisualizer.ui

import androidx.compose.ui.graphics.drawscope.DrawScope
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalEdge

fun DrawScope.drawUndirectedEdge(
    edge: GraphicalEdge,
    canvasCenter: Vector2D,
) {
    drawLine(
        color = edge.highlight,
        start = edge.startGraphicalVertex.position.toCanvasCoordinates(canvasCenter).toOffset(),
        end = edge.endGraphicalVertex.position.toCanvasCoordinates(canvasCenter).toOffset(),
        strokeWidth = 15f,
    )

    drawLine(
        color = edge.color,
        start = edge.startGraphicalVertex.position.toCanvasCoordinates(canvasCenter).toOffset(),
        end = edge.endGraphicalVertex.position.toCanvasCoordinates(canvasCenter).toOffset(),
        strokeWidth = if (edge.selected) 8f else 3f,
    )
}
