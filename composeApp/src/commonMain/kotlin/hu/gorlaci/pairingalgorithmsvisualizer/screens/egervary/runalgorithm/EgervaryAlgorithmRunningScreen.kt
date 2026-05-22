package hu.gorlaci.pairingalgorithmsvisualizer.screens.egervary.runalgorithm

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphMatrix
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.EgervaryLegend
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.OpenableLegend
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.egervary_algorithm

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
        title = stringResource(Res.string.egervary_algorithm),
        onNavigateBack = onBack,
        modifier = Modifier.fillMaxSize(),
        legend = {
            OpenableLegend(
                modifier = Modifier.fillMaxSize().weight(1f),
            ) {
                EgervaryLegend(
                    modifier = Modifier.padding(10.dp).size(200.dp, 500.dp).weight(1f),
                )
            }
        },
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
