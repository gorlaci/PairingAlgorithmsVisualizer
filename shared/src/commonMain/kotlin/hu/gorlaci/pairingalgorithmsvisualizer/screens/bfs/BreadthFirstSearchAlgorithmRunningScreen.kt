package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel.DisplayMode
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.CrossableText
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.TextCell
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.graphcanvas.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.graph_7
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.grid_on
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.list
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

    val displayMode by viewModel.displayMode

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
        controls = {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.padding(10.dp),
            ) {
                DisplayMode.entries.forEachIndexed { index, mode ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index,
                            DisplayMode.entries.size,
                        ),
                        selected = displayMode == mode,
                        onClick = { viewModel.onDisplayModeChanged(mode) },
                        label = {
                            Icon(
                                imageVector = when (mode) {
                                    DisplayMode.Visual -> graph_7
                                    DisplayMode.NeighbourList -> list
                                    DisplayMode.Matrix -> grid_on
                                },
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                            )
                        },
                        icon = {},
                    )
                }
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
    val displayMode by viewModel.displayMode

    val currentGraph by viewModel.currentGraph

    val vertices = currentGraph.vertices

    val activeVertex = currentGraph.activeVertex
    val queue = currentGraph.queue
    val processedVertices = currentGraph.processedVertices
    val visitedNeighbours = currentGraph.visitedNeighbours

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().weight(1f),
        ) {
            when (displayMode) {
                DisplayMode.Visual -> {
                    GraphCanvas(
                        graphicalGraph = graphicalGraph,
                        directed = isDirected,
                        modifier = Modifier.fillMaxSize().weight(1f),
                        onTap = viewModel::onTap,
                    )
                }

                DisplayMode.NeighbourList -> {
                    Column(
                        modifier = Modifier.fillMaxSize().weight(1f).padding(start = 50.dp, top = 50.dp),
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

                DisplayMode.Matrix -> {
                    TODO()
                }
            }

            Row(
                modifier = Modifier.padding(bottom = 30.dp, start = 30.dp),
            ) {
                Text(
                    text = "Vizsgálandó csúcsok: ",
                    fontSize = 20.sp,
                )
                processedVertices.forEachIndexed { index, vertex ->
                    CrossableText(
                        text = vertex.name,
                        forwardSlash = true,
                        backwardSlash = true,
                        fontSize = 20.sp,
                    )
                    if (index != processedVertices.lastIndex) {
                        Text(
                            text = ", ",
                            fontSize = 20.sp,
                        )
                    }
                }

                if (activeVertex != null) {
                    if (currentGraph.processedVertices.isNotEmpty()) {
                        Text(
                            text = ", ",
                            fontSize = 20.sp,
                        )
                    }
                    CrossableText(
                        text = activeVertex.name,
                        forwardSlash = true,
                        backwardSlash = false,
                        fontSize = 20.sp,
                    )
                }

                if (processedVertices.isNotEmpty() || activeVertex != null) {
                    Text(
                        text = ", ",
                        fontSize = 20.sp,
                    )
                }

                queue.forEachIndexed { index, vertex ->
                    Text(
                        text = vertex.name,
                        fontSize = 20.sp,
                    )
                    if (index != queue.lastIndex) {
                        Text(
                            text = ", ",
                            fontSize = 20.sp,
                        )
                    }
                }
            }
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
