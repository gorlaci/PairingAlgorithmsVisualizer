package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel.DataStructure
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel.DisplayMode
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.CrossableText
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.graphcanvas.GraphCanvas

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
    val dataStructure by viewModel.dataStructure

    val currentGraph by viewModel.currentGraph

    val activeVertex = currentGraph.activeVertex
    val queue = currentGraph.queue
    val processedVertices = currentGraph.processedVertices

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().weight(1f),
        ) {
            val dataStructureDisplay: @Composable () -> Unit = {
                when (dataStructure) {
                    DataStructure.List -> BreadthFirstSearchNeighbourList(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize().weight(1f).padding(start = 50.dp, top = 50.dp),
                    )

                    DataStructure.Matrix -> BreadthFirstSearchMatrix(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize().weight(1f).padding(start = 50.dp, top = 50.dp),
                    )
                }
            }

            when (displayMode) {
                DisplayMode.Visual -> {
                    GraphCanvas(
                        graphicalGraph = graphicalGraph,
                        directed = isDirected,
                        modifier = Modifier.fillMaxSize().weight(1f),
                        onTap = viewModel::onTap,
                    )
                }

                DisplayMode.DataStructure -> {
                    dataStructureDisplay()
                }

                DisplayMode.Both -> {
                    GraphCanvas(
                        graphicalGraph = graphicalGraph,
                        directed = isDirected,
                        modifier = Modifier.fillMaxHeight(.5f).fillMaxWidth().weight(1f),
                        onTap = viewModel::onTap,
                    )

                    dataStructureDisplay()
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
                        backwardSlash = true,
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

            Row(modifier = Modifier.padding(bottom = 30.dp, start = 30.dp)) {
                Text(
                    text = "Aktív csúcs: ",
                    fontSize = 20.sp,
                )
                activeVertex?.let {
                    Text(
                        text = it.name,
                        fontSize = 20.sp,
                    )
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
