package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.CrossableText

@Composable
fun BreadthFirstSearchNeighbourList(
    viewModel: BreadthFirstSearchAlgorithmRunningViewModel,
    modifier: Modifier = Modifier,
){
    val currentGraph by viewModel.currentGraph

    val vertices = currentGraph.vertices

    val activeVertex = currentGraph.activeVertex
    val processedVertices = currentGraph.processedVertices
    val visitedNeighbours = currentGraph.visitedNeighbours

    Column(
        modifier = modifier,
    ) {
        vertices.forEach { vertex ->
            Box(modifier = Modifier.wrapContentSize()) {
                Row {
                    Text(
                        text = vertex.name + ": ",
                        fontSize = 20.sp,
                    )
                    vertex.neighbours.sortedBy { it.name }.forEachIndexed { index, neighbour ->
                        CrossableText(
                            text = neighbour.name,
                            forwardSlash =
                                vertex in processedVertices ||
                                    (vertex == activeVertex && neighbour in visitedNeighbours),
                            backwardSlash = false,
                            fontSize = 20.sp,
                        )
                        if (index != vertex.neighbours.size - 1) {
                            Text(
                                text = ", ",
                                fontSize = 20.sp,
                            )
                        }
                    }
                }
                if (vertex in processedVertices) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, size.height / 2f),
                            end = Offset(size.width, size.height / 2f),
                            strokeWidth = 1.dp.toPx(),
                        )
                    }
                }
            }
        }
    }
}
