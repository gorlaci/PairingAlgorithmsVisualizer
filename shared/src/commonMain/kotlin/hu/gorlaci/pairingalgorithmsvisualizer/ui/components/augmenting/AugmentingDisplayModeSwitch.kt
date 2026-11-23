package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.augmenting

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.runalgorithm.AugmentingAlgorithmRunningViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.runalgorithm.AugmentingAlgorithmRunningViewModel.DisplayMode
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.DisplayModeSwitch
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.graph_7
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.grid_on

@Composable
fun AugmentingDisplayModeSwitch(
    viewModel: AugmentingAlgorithmRunningViewModel,
    modifier: Modifier = Modifier,
) {
    val selectedDisplayMode by viewModel.graphDisplayMode

    DisplayModeSwitch(
        selected = selectedDisplayMode,
        onDisplayModeSelected = viewModel::changeDisplayMode,
        modifier = modifier,
        label = { mode ->
            {
                when (mode) {
                    DisplayMode.GRAPHICAL -> {
                        Icon(
                            graph_7,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                    }

                    DisplayMode.MATRIX -> {
                        Icon(
                            grid_on,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                    }

                    DisplayMode.BOTH -> {
                        Row {
                            Icon(
                                graph_7,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                            )
                            Icon(
                                grid_on,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                            )
                        }
                    }
                }
            }
        },
    )
}
