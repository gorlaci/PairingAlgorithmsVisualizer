package hu.gorlaci.pairingalgorithmsvisualizer.screens.egervary.menu

import androidx.compose.runtime.Composable
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuItem
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MenuScreen

@Composable
fun EgervaryMenuScreen(
    onDrawGraphClick: () -> Unit,
    onRunAlgorithmClick: () -> Unit,
    onBack: () -> Unit,
) {
    MenuScreen(
        title = "Egerváry algoritmus",
        items = listOf(
            MenuItem("Saját gráf megadása", onDrawGraphClick),
            MenuItem("Algoritmus futtatása", onRunAlgorithmClick),
        ),
        onBack = onBack,
    )
}
