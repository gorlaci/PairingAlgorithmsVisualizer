package hu.gorlaci.pairingalgorithmsvisualizer.screens.egervary.menu

import androidx.compose.runtime.Composable
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuItem
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuScreen
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.shared.generated.resources.Res
import pairingalgorithmsvisualizer.shared.generated.resources.draw_matrix_weighted_screen
import pairingalgorithmsvisualizer.shared.generated.resources.egervary_algorithm
import pairingalgorithmsvisualizer.shared.generated.resources.run_algorithm_button

@Composable
fun EgervaryMenuScreen(
    onDrawGraphClick: () -> Unit,
    onRunAlgorithmClick: () -> Unit,
    onBack: () -> Unit,
) {
    MenuScreen(
        title = stringResource(Res.string.egervary_algorithm),
        items = listOf(
            MenuItem(stringResource(Res.string.draw_matrix_weighted_screen), onDrawGraphClick),
            MenuItem(stringResource(Res.string.run_algorithm_button), onRunAlgorithmClick),
        ),
        onBack = onBack,
    )
}
