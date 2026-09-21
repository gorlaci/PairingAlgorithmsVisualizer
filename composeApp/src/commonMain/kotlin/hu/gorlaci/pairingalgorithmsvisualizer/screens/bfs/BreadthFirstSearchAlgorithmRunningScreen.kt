package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextCell
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.bfs_algorithm

@Composable
fun BreadthFirstSearchAlgorithmRunningScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val viewModel = viewModel { BreadthFirstSearchAlgorithmRunningViewModel(graphStorage) }

    val graphicalGraph by viewModel.graphicalGraph
    val tree by viewModel.tree
    val graph by viewModel.currentGraph

    val inSetup by viewModel.inSetup

    AlgorithmRunningScreen(
        viewModel = viewModel,
        title = stringResource(Res.string.bfs_algorithm),
        onNavigateBack = onBack,
        modifier = Modifier.fillMaxSize(),
        skipButtonsShown = false,
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            GraphCanvas(
                graphicalGraph = graphicalGraph,
                modifier = Modifier.fillMaxSize().weight(1f),
                onTap = viewModel::onTap,
            )
            Column(
                modifier = Modifier.fillMaxSize().weight(1f),
            ) {
                GraphCanvas(
                    graphicalGraph = tree,
                    modifier = Modifier.fillMaxSize().weight(2f),
                )

                if (!inSetup) {
                    val cellSizeModifier = Modifier.width(40.dp)

                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f),
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                        ) {
                            Row {
                                Spacer(modifier = cellSizeModifier)
                                for (vertex in graph.vertices) {
                                    TextCell(
                                        text = vertex.name,
                                        modifier = cellSizeModifier,
                                    )
                                }
                            }

                            Row {
                                TextCell(
                                    text = "d(v)",
                                    modifier = cellSizeModifier,
                                )
                                for (vertex in graph.vertices) {
                                    TextCell(
                                        text = vertex.distance?.toString() ?: "∞",
                                        modifier = cellSizeModifier,
                                    )
                                }
                            }

                            Row {
                                TextCell(
                                    text = "p(v)",
                                    modifier = cellSizeModifier,
                                )
                                for (vertex in graph.vertices) {
                                    TextCell(
                                        text = vertex.parent?.name ?: "*",
                                        modifier = cellSizeModifier,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
