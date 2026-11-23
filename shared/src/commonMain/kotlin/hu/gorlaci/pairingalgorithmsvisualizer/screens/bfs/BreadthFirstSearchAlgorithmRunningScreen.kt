package hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs.BreadthFirstSearchDisplayModeSwitch
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs.BreadthFirstSearchScreenContent
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs.BreadthFirstSearchVertexTable
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
        controls = {
            BreadthFirstSearchDisplayModeSwitch(
                viewModel = viewModel,
                modifier = Modifier.padding(10.dp),
            )
        },
    ) {
        BreadthFirstSearchScreenContent(
            viewModel = viewModel,
            table = {
                BreadthFirstSearchVertexTable(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize().weight(1f),
                )
            },
        )
    }
}
