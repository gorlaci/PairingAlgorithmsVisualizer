package hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.runalgorithm

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphCanvas
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphDisplayMode
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphDisplayModeSwitch
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphMatrix
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.AugmentingLegend
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.legend.OpenableLegend
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.augmenting_path_algorithm

@Composable
fun AugmentingAlgorithmRunningScreen(
    graphStorage: GraphStorage,
    onBack: () -> Unit,
) {
    val viewModel = viewModel { AugmentingAlgorithmRunningViewModel(graphStorage) }

    val graphicalGraph by viewModel.graphicalGraph
    val tree by viewModel.tree

    val class1Ids by viewModel.class1Ids
    val class2Ids by viewModel.class2Ids

    val displayMode by viewModel.graphDisplayMode

    AlgorithmRunningScreen(
        viewModel = viewModel,
        title = stringResource(Res.string.augmenting_path_algorithm),
        onNavigateBack = onBack,
        modifier = Modifier.fillMaxSize(),
        legend = {
            OpenableLegend(
                modifier = Modifier.fillMaxSize().weight(1f),
            ) {
                AugmentingLegend(
                    modifier = Modifier.padding(10.dp).size(200.dp, 500.dp).weight(1f),
                )
            }
        },
        controls = {
            GraphDisplayModeSwitch(
                matrixMode = displayMode,
                onModeChange = viewModel::changeDisplayMode,
                modifier = Modifier.padding(10.dp),
            )
        },
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(.5f).fillMaxHeight(),
            ) {
                when (displayMode) {
                    GraphDisplayMode.BOTH -> {
                        GraphCanvas(
                            graphicalGraph = graphicalGraph,
                            onTap = viewModel::onTap,
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(.5f),
                        )
                        GraphMatrix(
                            graphicalGraph = graphicalGraph,
                            class1Ids = class1Ids,
                            class2Ids = class2Ids,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    GraphDisplayMode.GRAPHICAL -> {
                        GraphCanvas(
                            graphicalGraph = graphicalGraph,
                            onTap = viewModel::onTap,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    GraphDisplayMode.MATRIX -> {
                        GraphMatrix(
                            graphicalGraph = graphicalGraph,
                            class1Ids = class1Ids,
                            class2Ids = class2Ids,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }

            GraphCanvas(
                graphicalGraph = tree,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
