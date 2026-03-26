package hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.run_algorithm

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.legend.EdmondsLegend
import hu.gorlaci.pairingalgorithmsvisualizer.ui.legend.OpenableLegend
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.run_algorithm_screen

@Composable
fun EdmondsAlgorithmRunningScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val viewModel = viewModel { EdmondsAlgorithmRunningScreenViewModel(graphStorage, coroutineScope.coroutineContext) }

    val selectedGraph by viewModel.currentGraph

    val graphicalGraph by viewModel.graphicalGraph

    val nextEnabled by viewModel.nextEnabled
    val backEnabled by viewModel.backEnabled
    val runEnabled by viewModel.runEnabled

    AlgorithmRunningScreen(
        title = stringResource(Res.string.run_algorithm_screen),
        selectedGraph = selectedGraph,
        graphList = viewModel.graphList,
        onGraphIndexSelected = viewModel::onGraphSelected,
        graphicalGraph = graphicalGraph,
        nextEnabled = nextEnabled,
        backEnabled = backEnabled,
        runEnabled = runEnabled,
        onNext = viewModel::onNext,
        onBack = viewModel::onBack,
        onRun = viewModel::onRun,
        onNavigateBack = onBack,
        modifier = Modifier.fillMaxSize(),
        legend = {
            OpenableLegend(
                modifier = Modifier.fillMaxSize().weight(1f)
            ) {
                EdmondsLegend(
                    modifier = Modifier.padding(10.dp).size(200.dp, 500.dp).weight(1f)
                )
            }
        }
    )
}
