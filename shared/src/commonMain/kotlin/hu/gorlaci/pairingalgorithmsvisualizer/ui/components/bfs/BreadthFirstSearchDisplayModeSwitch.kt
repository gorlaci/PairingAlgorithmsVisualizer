package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel.DataStructure
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel.DisplayMode
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.DisplayModeSwitch
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.graph_7
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.grid_on
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.list

@Composable
fun BreadthFirstSearchDisplayModeSwitch(
    viewModel: BreadthFirstSearchAlgorithmRunningViewModel,
    modifier: Modifier = Modifier,
) {
    val selectedDisplayMode by viewModel.displayMode
    val dataStructure by viewModel.dataStructure

    val dataStructureIcon = when (dataStructure) {
        DataStructure.List -> list
        DataStructure.Matrix -> grid_on
    }

    DisplayModeSwitch(
        selected = selectedDisplayMode,
        onDisplayModeSelected = viewModel::onDisplayModeChanged,
        modifier = modifier,
        label = { mode ->
            {
                when (mode) {
                    DisplayMode.Visual -> Icon(
                        imageVector = graph_7,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                    )

                    DisplayMode.DataStructure -> Icon(
                        imageVector = dataStructureIcon,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                    )

                    DisplayMode.Both -> Row {
                        Icon(
                            imageVector = graph_7,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                        Icon(
                            imageVector = dataStructureIcon,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                    }
                }
            }
        },
    )
}
