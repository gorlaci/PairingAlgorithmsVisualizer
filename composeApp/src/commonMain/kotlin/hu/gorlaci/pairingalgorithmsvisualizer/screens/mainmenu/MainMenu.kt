package hu.gorlaci.pairingalgorithmsvisualizer.screens.mainmenu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.draw_menu_screen

@Composable
fun MainMenuScreen(
    onDrawGraphClick: () -> Unit,
    onEdmondsClick: () -> Unit,
    onAugmentingPathClick: () -> Unit,
    onEgervaryClick: () -> Unit,
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onDrawGraphClick,
            ) {
                Text(text = stringResource(Res.string.draw_menu_screen))
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = onAugmentingPathClick,
            ) {
                Text("Javítóutas algoritmus")
            }

            Spacer(
                modifier = Modifier.height(50.dp),
            )

            Button(
                onClick = onEdmondsClick,
            ) {
                Text("Edmonds algoritmus")
            }

            Spacer(
                modifier = Modifier.height(50.dp),
            )

            Button(
                onClick = onEgervaryClick,
            ) {
                Text("Egerváry algoritmus")
            }
        }
    }
}
