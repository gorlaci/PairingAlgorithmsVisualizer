package hu.gorlaci.pairingalgorithmsvisualizer.features.egervary

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphMatrix

@Composable
fun EgervaryAlgorithmRunningViewScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val viewModel = viewModel { EgervaryAlgorithmRunningViewModel(graphStorage) }

    val selectedGraph by viewModel.selectedGraph
    val graphicalGraph by viewModel.graphicalGraph

    val step by viewModel.step
    val maxSteps by viewModel.maxSteps

    val nextEnabled by viewModel.nextEnabled
    val backEnabled by viewModel.backEnabled
    val runEnabled by viewModel.runEnabled

    val class1names by viewModel.class1names
    val classes2names by viewModel.class2names

    AlgorithmRunningScreen(
        title = "Egerváry Algoritmus",
        selectedGraph = selectedGraph,
        graphList = viewModel.graphList,
        onGraphIndexSelected = viewModel::onGraphSelected,
        graphicalGraph = graphicalGraph,
        step = step + 1,
        maxStep = maxSteps,
        nextEnabled = nextEnabled,
        backEnabled = backEnabled,
        runEnabled = runEnabled,
        onNext = viewModel::onNext,
        onBack = viewModel::onBack,
        onRun = viewModel::onRun,
        onStepChange = viewModel::onStepChange,
        onNavigateBack = onBack,
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            GraphMatrix(
                graphicalGraph = graphicalGraph,
                class1Ids = class1names,
                class2Ids = classes2names,
                modifier = Modifier.padding(30.dp),
            )

            GraphCanvas(
                graphicalGraph = graphicalGraph,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
