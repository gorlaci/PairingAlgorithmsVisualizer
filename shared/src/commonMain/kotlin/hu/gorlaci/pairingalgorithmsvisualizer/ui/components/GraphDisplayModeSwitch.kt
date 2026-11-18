package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.graph_7
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.grid_on

@Composable
fun GraphDisplayModeSwitch(
    selectedMode: GraphDisplayMode,
    onModeChange: (GraphDisplayMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SingleChoiceSegmentedButtonRow {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    0,
                    3,
                ),
                selected = selectedMode == GraphDisplayMode.BOTH,
                onClick = { onModeChange(GraphDisplayMode.BOTH) },
                label = {
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
                },
                icon = {},
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    1,
                    3,
                ),
                selected = selectedMode == GraphDisplayMode.GRAPHICAL,
                onClick = { onModeChange(GraphDisplayMode.GRAPHICAL) },
                label = {
                    Icon(
                        graph_7,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                    )
                },
                icon = {},
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    2,
                    3,
                ),
                selected = selectedMode == GraphDisplayMode.MATRIX,
                onClick = { onModeChange(GraphDisplayMode.MATRIX) },
                label = {
                    Icon(
                        grid_on,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                    )
                },
                icon = {},
            )
        }
    }
}

enum class GraphDisplayMode {
    BOTH,
    GRAPHICAL,
    MATRIX,
}
