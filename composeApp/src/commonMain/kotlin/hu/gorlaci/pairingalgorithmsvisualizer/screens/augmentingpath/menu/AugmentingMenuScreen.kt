package hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.menu

import androidx.compose.runtime.Composable
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuItem
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuScreen

@Composable
fun AugmentingMenuScreen(
    onRunAlgorithm: () -> Unit,
    onBack: () -> Unit,
) {
    MenuScreen(
        title = "Javítóutas algoritmus",
        items = listOf(
            MenuItem("Algoritmus futtatása", onRunAlgorithm),
        ),
        onBack = onBack,
    )
}
