package hu.gorlaci.pairingalgorithmsvisualizer.screens.egervary.runalgorithm

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphMatrix
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen

@Composable
fun EgervaryAlgorithmRunningViewScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val viewModel = viewModel { EgervaryAlgorithmRunningViewModel(graphStorage) }

    val graphicalGraph by viewModel.graphicalGraph

    val class1names by viewModel.class1names
    val classes2names by viewModel.class2names

    AlgorithmRunningScreen(
        viewModel = viewModel,
        title = "Egerváry Algoritmus",
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
