package hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier

@Composable
fun AugmentingMenu(
    onRunAlgorithm: () -> Unit,
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = onRunAlgorithm) {
            Text("Run algorithm")
        }
    }
}