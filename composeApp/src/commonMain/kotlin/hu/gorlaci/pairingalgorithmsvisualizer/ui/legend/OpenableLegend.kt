package hu.gorlaci.pairingalgorithmsvisualizer.ui.legend

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun OpenableLegend(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val legendExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Jelmagyarázat")
            IconButton(
                onClick = { legendExpanded.value = !legendExpanded.value }
            ) {
                Icon(Icons.Default.ArrowDropDown, null)
            }
        }

        AnimatedVisibility(legendExpanded.value) {
            Card(
                content = content,
            )
        }
    }
}