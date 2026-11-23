package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_ORANGE
import hu.gorlaci.pairingalgorithmsvisualizer.ui.LIGHT_YELLOW
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.CrossableTextCell
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextCell

@Composable
fun BreadthFirstSearchMatrix(
    viewModel: BreadthFirstSearchAlgorithmRunningViewModel,
    modifier: Modifier = Modifier,
) {
    val currentGraph by viewModel.currentGraph

    val vertices = currentGraph.vertices.sortedBy { it.name }

    val widthModifier = Modifier.width(40.dp)

    Column(modifier = modifier) {
        Row {
            Spacer(modifier = widthModifier)
            for (vertex in vertices) {
                TextCell(
                    text = vertex.name,
                    modifier = widthModifier,
                )
            }
        }

        vertices.forEach { rowVertex ->
            Box(
                modifier = Modifier.wrapContentSize(),
            ) {
                Row {
                    TextCell(
                        text = rowVertex.name,
                        modifier = widthModifier
                            .background(
                                if (currentGraph.activeVertex == rowVertex) {
                                    LIGHT_ORANGE
                                } else {
                                    Color.White
                                },
                            ),
                    )
                    for (columnVertex in vertices) {
                        val crossed =
                            (
                                currentGraph.visitedNeighbours.contains(
                                    columnVertex,
                                ) && currentGraph.activeVertex == rowVertex
                                ) ||
                                currentGraph.processedVertices.contains(rowVertex)
                        CrossableTextCell(
                            text = if (rowVertex.neighbours.contains(columnVertex)) "1" else "0",
                            forwardSlash = crossed,
                            backwardSlash = false,
                            modifier = widthModifier.background(
                                if (currentGraph.activeNeighbour == columnVertex &&
                                    currentGraph.activeVertex == rowVertex
                                ) {
                                    LIGHT_YELLOW
                                } else {
                                    Color.White
                                },
                            ),
                        )
                    }
                }
                if (currentGraph.processedVertices.contains(rowVertex)) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, size.height / 2f),
                            end = Offset(size.width, size.height / 2f),
                        )
                    }
                }
            }
        }
    }
}
