package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.bfs

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningViewModel.DataStructure
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.DisplayModeSwitch
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.grid_on
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.list

@Composable
fun BreadthFirstSearchDataStructureSwitch(
    viewModel: BreadthFirstSearchAlgorithmRunningViewModel,
    modifier: Modifier = Modifier,
) {
    val selectedDataStructure by viewModel.dataStructure

    DisplayModeSwitch(
        selected = selectedDataStructure,
        onDisplayModeSelected = viewModel::onDataStructureChanged,
        modifier = modifier,
        label = { dataStructure ->
            {
                Icon(
                    imageVector = when (dataStructure) {
                        DataStructure.List -> list
                        DataStructure.Matrix -> grid_on
                    },
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                )
            }
        },
    )
}
