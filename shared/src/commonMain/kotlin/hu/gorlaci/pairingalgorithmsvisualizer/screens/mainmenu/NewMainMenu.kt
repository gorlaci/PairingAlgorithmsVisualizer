package hu.gorlaci.pairingalgorithmsvisualizer.screens.mainmenu

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.AlgorithmCard
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MobileAlgorithmCard
import hu.gorlaci.pairingalgorithmsvisualizer.util.isTouchDevice

@Composable
fun NewMainMenuScreen(menuItems: List<MainMenuItem>) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 250.dp),
        ) {
            items(menuItems) { menuItem ->
                if (!isTouchDevice()) {
                    AlgorithmCard(
                        algorithmName = menuItem.algorithmName,
                        algorithmImage = menuItem.algorithmImage,
                        onRunAlgorithm = menuItem.onRunAlgorithm,
                        onQuiz = menuItem.onQuiz,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(250.dp),
                    )
                } else {
                    MobileAlgorithmCard(
                        algorithmName = menuItem.algorithmName,
                        algorithmImage = menuItem.algorithmImage,
                        onRunAlgorithm = menuItem.onRunAlgorithm,
                        onQuiz = menuItem.onQuiz,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(200.dp),
                    )
                }
            }
        }
    }
}

data class MainMenuItem(
    val algorithmName: String,
    val algorithmImage: ImageBitmap,
    val onRunAlgorithm: () -> Unit,
    val onQuiz: (() -> Unit)? = null,
)
