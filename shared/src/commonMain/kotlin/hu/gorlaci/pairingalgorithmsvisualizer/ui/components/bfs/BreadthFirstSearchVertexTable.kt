package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextCell

@Composable
fun BreadthFirstSearchVertexTable(
    viewModel: BreadthFirstSearchAlgorithmRunningViewModel,
    modifier: Modifier = Modifier,
) {
    val currentGraph by viewModel.currentGraph

    val cellSizeModifier = Modifier.size(40.dp, 25.dp)
    val firstCellSizeModifier = Modifier.size(80.dp, 25.dp)

    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
        ) {
            Row {
                Spacer(modifier = firstCellSizeModifier)
                for (vertex in currentGraph.vertices) {
                    TextCell(
                        text = vertex.name,
                        modifier = cellSizeModifier,
                    )
                }
            }

            val showDistances by viewModel.showDistances

            if (showDistances) {
                Row {
                    TextCell(
                        text = "táv(v)",
                        modifier = firstCellSizeModifier,
                    )
                    for (vertex in currentGraph.vertices) {
                        TextCell(
                            text = vertex.distance?.toString() ?: "∞",
                            modifier = cellSizeModifier,
                        )
                    }
                }
            }

            Row {
                TextCell(
                    text = "előző(v)",
                    modifier = firstCellSizeModifier,
                )
                for (vertex in currentGraph.vertices) {
                    TextCell(
                        text = vertex.parent?.name ?: "*",
                        modifier = cellSizeModifier,
                    )
                }
            }
        }
    }
}
