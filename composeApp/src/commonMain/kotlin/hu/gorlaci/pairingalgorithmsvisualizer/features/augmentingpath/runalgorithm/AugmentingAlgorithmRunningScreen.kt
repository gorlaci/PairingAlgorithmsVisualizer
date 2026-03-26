package hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.runalgorithm

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.legend.AugmentingLegend
import hu.gorlaci.pairingalgorithmsvisualizer.ui.legend.OpenableLegend
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.run_algorithm_screen

@Composable
fun AugmentingAlgorithmRunningScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val viewModel = viewModel { AugmentingAlgorithmRunningViewModel(graphStorage) }

    val selectedGraph by viewModel.selectedGraph
    val graphicalGraph by viewModel.graphicalGraph
    val tree by viewModel.tree

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
                AugmentingLegend(
                    modifier = Modifier.padding(10.dp).size(200.dp, 500.dp).weight(1f)
                )
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            GraphCanvas(
                graphicalGraph = graphicalGraph,
                modifier = Modifier.fillMaxWidth(.5f).fillMaxHeight(),
            )

            GraphCanvas(
                graphicalGraph = tree,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

}