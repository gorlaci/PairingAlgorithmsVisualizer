package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextCell
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextFieldCell
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.OpenableLegend
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.SearchLegend
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.bfs_algorithm

@Composable
fun BreadthFirstSearchAlgorithmRunningScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
    onNewGraph: () -> Unit,
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
        )
    }
}

@Composable
fun BreadthFirstSearchScreenContent(
    viewModel: BreadthFirstSearchAlgorithmRunningViewModel,
    tableEditable: Boolean = false,
) {
    val graphicalGraph by viewModel.graphicalGraph
    val tree by viewModel.tree
    val currentGraph by viewModel.currentGraph
    val inSetup by viewModel.inSetup

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
                val cellSizeModifier = Modifier.size(40.dp, 25.dp)
                val firstCellSizeModifier = Modifier.size(80.dp, 25.dp)

                Box(
                    modifier = Modifier.fillMaxSize().weight(1f),
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

                        Row {
                            TextCell(
                                text = "táv(v)",
                                modifier = firstCellSizeModifier,
                            )
                            for (vertex in currentGraph.vertices) {
                                if (tableEditable) {
                                    val text = mutableStateOf(vertex.distance?.toString() ?: "∞")
                                    TextFieldCell(
                                        value = text.value,
                                        onValueChange = {
                                            text.value = it
                                        },
                                        modifier = cellSizeModifier,
                                    )
                                } else {
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
                                if (tableEditable) {
                                    val text = mutableStateOf(vertex.parent?.name ?: "*")
                                    TextFieldCell(
                                        value = text.value,
                                        onValueChange = {
                                            text.value = it
                                        },
                                        modifier = cellSizeModifier,
                                    )
                                } else {
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
