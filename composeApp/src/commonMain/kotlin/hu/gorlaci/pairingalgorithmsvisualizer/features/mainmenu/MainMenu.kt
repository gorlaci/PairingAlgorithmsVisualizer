package hu.gorlaci.pairingalgorithmsvisualizer.features.mainmenu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.draw_custom_screen

@Composable
fun MainMenuScreen(
    onDrawClick: () -> Unit,
    onEdmondsMenuClick: () -> Unit,
    onAugmentingPathMenuClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onDrawClick,
        ) {
            Text(text = stringResource(Res.string.draw_custom_screen))
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = onAugmentingPathMenuClick,
        ) {
            Text("Javítóutas algoritmus")
        }

        Spacer(
            modifier = Modifier.height(50.dp)
        )

        Button(
            onClick = onEdmondsMenuClick,
        ) {
            Text("Edmonds algoritmus")
        }
    }
}