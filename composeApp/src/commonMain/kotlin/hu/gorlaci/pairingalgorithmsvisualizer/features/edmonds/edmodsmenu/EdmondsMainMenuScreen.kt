package hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.edmodsmenu

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
import pairingalgorithmsvisualizer.composeapp.generated.resources.quiz_screen
import pairingalgorithmsvisualizer.composeapp.generated.resources.run_algorithm_screen

@Composable
fun EdmondsMainMenuScreen(
    onDrawGraphClick: () -> Unit,
    onRunAlgorithmClick: () -> Unit,
    onPlayQuizClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onDrawGraphClick,
        ) {
            Text(stringResource(Res.string.draw_custom_screen))
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = onRunAlgorithmClick,
        ) {
            Text(stringResource(Res.string.run_algorithm_screen))
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = onPlayQuizClick,
        ) {
            Text(stringResource(Res.string.quiz_screen))
        }
    }
}
