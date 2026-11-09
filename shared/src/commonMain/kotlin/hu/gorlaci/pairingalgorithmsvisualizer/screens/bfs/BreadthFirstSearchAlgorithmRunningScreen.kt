package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextCell
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.graphcanvas.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.OpenableLegend
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.SearchLegend
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.shared.generated.resources.Res
import pairingalgorithmsvisualizer.shared.generated.resources.bfs_algorithm

@Composable
fun BreadthFirstSearchAlgorithmRunningScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
    onNewGraph: () -> Unit,
) {
    val viewModel = viewModel { BreadthFirstSearchAlgorithmRunningViewModel(graphStorage) }

    AlgorithmRunningScreen(
        viewModel = viewModel,
        title = stringResource(Res.string.bfs_algorithm),
        onNavigateBack = onBack,
        onNewGraph = onNewGraph,
        modifier = Modifier.fillMaxSize(),
        skipButtonsShown = false,
        legend = {
            OpenableLegend(
                modifier = Modifier.fillMaxSize().weight(1f),
            ) {
                SearchLegend(
                    modifier = Modifier.padding(10.dp).size(200.dp, 250.dp).weight(1f),
                )
            }
        },
    ) {
        BreadthFirstSearchScreenContent(
            viewModel = viewModel,
            table = {
                BfsVertexTable(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize().weight(1f),
                )
            },
        )
    }
}

@Composable
fun BreadthFirstSearchScreenContent(
    viewModel: BreadthFirstSearchAlgorithmRunningViewModel,
    table: @Composable ColumnScope.() -> Unit,
) {
    val graphicalGraph by viewModel.graphicalGraph
    val tree by viewModel.tree
    val inSetup by viewModel.inSetup
    val isDirected by viewModel.isDirected

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().weight(1f),
        ) {
            GraphCanvas(
                graphicalGraph = graphicalGraph,
                directed = isDirected,
                modifier = Modifier.fillMaxSize().weight(1f),
                onTap = viewModel::onTap,
            )

            val queue by viewModel.queue

            Text(
                text = "Vizsgálandó csúcsok: " + queue.joinToString(
                    separator = ", ",
                    transform = { it.name },
                ),
                modifier = Modifier.padding(bottom = 30.dp, start = 30.dp),
                fontSize = 20.sp,
            )
        }
        Column(
            modifier = Modifier.fillMaxSize().weight(1f),
        ) {
            GraphCanvas(
                graphicalGraph = tree,
                directed = isDirected,
                modifier = Modifier.fillMaxSize().weight(2f),
            )

            if (!inSetup) {
                table()
            }
        }
    }
}

@Composable
fun BfsVertexTable(
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
