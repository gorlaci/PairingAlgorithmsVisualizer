@file:Suppress("ktlint:standard:no-wildcard-imports")

package hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.visual

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.SimpleTopAppbar
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.*

@Composable
fun GraphDrawingScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val viewModel = viewModel { GraphDrawingScreenViewmodel(graphStorage) }

    val graph by viewModel.graphicalGraph
    val drawMode by viewModel.drawMode
    val name by viewModel.graphName

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SimpleTopAppbar(
                title = stringResource(Res.string.draw_visual_screen),
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        Row(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f),
            ) {
                TextField(
                    value = name,
                    onValueChange = { viewModel.onNameChange(it) },
                    modifier = Modifier.padding(10.dp),
                )

                GraphCanvas(
                    graphicalGraph = graph,
                    onTap = viewModel::onTap,
                    onDragStart = viewModel::onDragStart,
                    onDrag = viewModel::onDrag,
                    onDragEnd = viewModel::onDragEnd,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Column {
                Button(
                    onClick = { viewModel.changeDrawMode(DrawMode.VERTEX) },
                    enabled = drawMode != DrawMode.VERTEX,
                ) {
                    Text(stringResource(Res.string.add_vertex_button))
                }
                Button(
                    onClick = { viewModel.changeDrawMode(DrawMode.EDGE) },
                    enabled = drawMode != DrawMode.EDGE,
                ) {
                    Text(stringResource(Res.string.add_edge_button))
                }
                Button(
                    onClick = {
                        viewModel.saveGraph()
                    },
                ) {
                    Text(stringResource(Res.string.save_button))
                }
            }
        }
    }
}
