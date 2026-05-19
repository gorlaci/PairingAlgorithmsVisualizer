package hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds.runalgorithm

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.EdmondsLegend
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.OpenableLegend
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.run_algorithm_screen

@Composable
fun EdmondsAlgorithmRunningScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val viewModel = viewModel {
        EdmondsAlgorithmRunningScreenViewModel(
            graphStorage,
            coroutineScope.coroutineContext,
        )
    }

    AlgorithmRunningScreen(
        viewModel = viewModel,
        title = stringResource(Res.string.run_algorithm_screen),
        onNavigateBack = onBack,
        modifier = Modifier.fillMaxSize(),
        legend = {
            OpenableLegend(
                modifier = Modifier.fillMaxSize().weight(1f),
            ) {
                EdmondsLegend(
                    modifier = Modifier.padding(10.dp).size(200.dp, 500.dp).weight(1f),
                )
            }
        },
    )
}
