package hu.gorlaci.pairingalgorithmsvisualizer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.graph_7_24px

@Composable
fun GraphDisplayModeSwitch(
    matrixMode: Boolean,
    onSwitchChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painterResource(Res.drawable.graph_7_24px),
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
        Switch(
            checked = matrixMode,
            onCheckedChange = onSwitchChanged,
            modifier = Modifier.height(48.dp)
        )
        Icon(
            Icons.Default.GridOn,
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Preview
@Composable
fun GraphDisplayModeSwitchPreview() {
    GraphDisplayModeSwitch(
        matrixMode = false,
        onSwitchChanged = {},
    )
}