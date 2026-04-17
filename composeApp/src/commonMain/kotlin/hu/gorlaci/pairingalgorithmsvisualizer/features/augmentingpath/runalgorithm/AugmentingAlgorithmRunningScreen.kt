package hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.runalgorithm

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.*
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

    val step by viewModel.step
    val maxSteps by viewModel.maxSteps

    val class1Ids by viewModel.class1Ids
    val class2Ids by viewModel.class2Ids

    val nextEnabled by viewModel.nextEnabled
    val backEnabled by viewModel.backEnabled
    val runEnabled by viewModel.runEnabled

    val displayMode by viewModel.graphDisplayMode

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
        },
        controls = {
            GraphDisplayModeSwitch(
                matrixMode = displayMode,
                onModeChange = viewModel::changeDisplayMode,
                modifier = Modifier.padding(10.dp)
            )
        },
        step = step + 1,
        maxStep = maxSteps,
        onStepChange = viewModel::onStepChange,
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