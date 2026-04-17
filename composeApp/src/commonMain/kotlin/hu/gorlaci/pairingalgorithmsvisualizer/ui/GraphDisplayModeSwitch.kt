package hu.gorlaci.pairingalgorithmsvisualizer.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.graph_7_24px

@Composable
fun GraphDisplayModeSwitch(
    matrixMode: GraphDisplayMode,
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
                    3
                ),
                selected = matrixMode == GraphDisplayMode.BOTH,
                onClick = { onModeChange(GraphDisplayMode.BOTH) },
                label = {
                    Row {
                        Icon(
                            painterResource(Res.drawable.graph_7_24px),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp)
                        )
                        Icon(
                            Icons.Default.GridOn,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp)
                        )

                    }
                },
                icon = {}
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    1,
                    3
                ),
                selected = matrixMode == GraphDisplayMode.GRAPHICAL,
                onClick = { onModeChange(GraphDisplayMode.GRAPHICAL) },
                label = {
                    Icon(
                        painterResource(Res.drawable.graph_7_24px),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                },
                icon = {}
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    2,
                    3
                ),
                selected = matrixMode == GraphDisplayMode.MATRIX,
                onClick = { onModeChange(GraphDisplayMode.MATRIX) },
                label = {
                    Icon(
                        Icons.Default.GridOn,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                },
                icon = {}
            )
        }
    }
}

enum class GraphDisplayMode {
    BOTH,
    GRAPHICAL,
    MATRIX,
}

@Preview
@Composable
fun GraphDisplayModeSwitchPreview() {
    GraphDisplayModeSwitch(
        matrixMode = GraphDisplayMode.BOTH,
        onModeChange = {},
    )
}