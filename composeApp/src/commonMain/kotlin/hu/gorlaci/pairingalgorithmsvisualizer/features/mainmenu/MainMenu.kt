package hu.gorlaci.pairingalgorithmsvisualizer.features.mainmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MainMenuScreen(
    onEdmondsMenuClick: () -> Unit,
    onAugmentingPathMenuClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onAugmentingPathMenuClick,
        ) {
            Text("Javítóutas algoritmus")
        }

        Button(
            onClick = onEdmondsMenuClick,
        ) {
            Text("Edmonds algoritmus")
        }
    }
}