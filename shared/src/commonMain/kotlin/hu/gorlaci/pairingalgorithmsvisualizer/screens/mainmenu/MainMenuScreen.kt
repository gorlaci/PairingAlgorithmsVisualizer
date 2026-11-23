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
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MainMenuAlgorithmCard
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.MainMenuItem

@Composable
fun MainMenuScreen(menuItems: List<MainMenuItem>) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 250.dp),
        ) {
            items(menuItems) { menuItem ->
                MainMenuAlgorithmCard(
                    menuItem = menuItem,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(250.dp),
                )
            }
        }
    }
}
