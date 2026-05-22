package hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds.menu

import androidx.compose.runtime.Composable
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuItem
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuScreen
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.*

@Composable
fun EdmondsMenuScreen(
    onDrawGraphClick: () -> Unit,
    onRunAlgorithmClick: () -> Unit,
    onPlayQuizClick: () -> Unit,
    onBack: () -> Unit,
) {
    MenuScreen(
        title = stringResource(Res.string.edmonds_algorithm),
        items = listOf(
            MenuItem(stringResource(Res.string.draw_visual_screen), onDrawGraphClick),
            MenuItem(stringResource(Res.string.run_algorithm_button), onRunAlgorithmClick),
            MenuItem(stringResource(Res.string.quiz_screen), onPlayQuizClick),
        ),
        onBack = onBack,
    )
}
