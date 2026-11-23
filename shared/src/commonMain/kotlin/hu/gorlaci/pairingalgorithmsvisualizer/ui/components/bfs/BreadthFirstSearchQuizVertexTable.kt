package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz.BreadthFirstSearchQuizScreenViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextCell
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextFieldCell

@Composable
fun BreadthFirstSearchQuizVertexTable(
    viewModel: BreadthFirstSearchQuizScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val currentGraph by viewModel.currentGraph
    val tableEditable by viewModel.tableEditable

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

                    val tableDistanceContent by viewModel.tableDistanceContent

                    for (vertex in currentGraph.vertices) {
                        TextFieldCell(
                            value = tableDistanceContent[vertex.name] ?: "∞",
                            onValueChange = {
                                viewModel.onTableDistanceChanged(vertex, it)
                            },
                            modifier = cellSizeModifier,
                            enabled = tableEditable,
                        )
                    }
                }
            }

            Row {
                TextCell(
                    text = "előző(v)",
                    modifier = firstCellSizeModifier,
                )
                val tableParentContent by viewModel.tableParentContent
                for (vertex in currentGraph.vertices) {
                    TextFieldCell(
                        value = tableParentContent[vertex.name] ?: "*",
                        onValueChange = {
                            viewModel.onTableParentChanged(vertex, it)
                        },
                        modifier = cellSizeModifier,
                        enabled = tableEditable,
                    )
                }
            }
        }
    }
}
