package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel
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
            Row {
                TextCell(
                    text = rowVertex.name,
                    modifier = widthModifier,
                )
                for (columnVertex in vertices) {
                    TextCell(
                        text = if (rowVertex.neighbours.contains(columnVertex)) "1" else "0",
                        modifier = widthModifier,
                    )
                }
            }
        }
    }
}
