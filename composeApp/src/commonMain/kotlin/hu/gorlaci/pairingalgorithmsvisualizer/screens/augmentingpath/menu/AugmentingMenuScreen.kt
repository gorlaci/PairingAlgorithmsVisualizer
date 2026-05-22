package hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.menu

import androidx.compose.runtime.Composable
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuItem
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuScreen
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.draw_matrix_screen
import pairingalgorithmsvisualizer.composeapp.generated.resources.draw_visual_screen
import pairingalgorithmsvisualizer.composeapp.generated.resources.run_algorithm_button

@Composable
fun AugmentingMenuScreen(
    onDrawVisualClick: () -> Unit,
    onDrawMatrixClick: () -> Unit,
    onRunAlgorithm: () -> Unit,
    onBack: () -> Unit,
) {
    MenuScreen(
        title = "Javítóutas algoritmus",
        items = listOf(
            MenuItem(stringResource(Res.string.draw_visual_screen), onDrawVisualClick),
            MenuItem(stringResource(Res.string.draw_matrix_screen), onDrawMatrixClick),
            MenuItem(stringResource(Res.string.run_algorithm_button), onRunAlgorithm),
        ),
        onBack = onBack,
    )
}
