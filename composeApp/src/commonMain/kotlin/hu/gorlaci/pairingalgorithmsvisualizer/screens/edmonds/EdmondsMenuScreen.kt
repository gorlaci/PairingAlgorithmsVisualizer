package hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds

import androidx.compose.runtime.Composable
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuItem
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuScreen
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.quiz_screen
import pairingalgorithmsvisualizer.composeapp.generated.resources.run_algorithm_screen

@Composable
fun EdmondsMenuScreen(
    onDrawGraphClick: () -> Unit,
    onRunAlgorithmClick: () -> Unit,
    onPlayQuizClick: () -> Unit,
    onBack: () -> Unit,
) {
    MenuScreen(
        title = "Blossom algoritmus",
        items = listOf(
            MenuItem("Saját gráf rajzolása", onDrawGraphClick),
            MenuItem(stringResource(Res.string.run_algorithm_screen), onRunAlgorithmClick),
            MenuItem(stringResource(Res.string.quiz_screen), onPlayQuizClick),
        ),
        onBack = onBack,
    )
}
