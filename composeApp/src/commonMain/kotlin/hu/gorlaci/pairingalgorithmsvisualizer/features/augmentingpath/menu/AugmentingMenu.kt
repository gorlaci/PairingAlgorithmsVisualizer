package hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import hu.gorlaci.pairingalgorithmsvisualizer.ui.SimpleTopAppbar

@Composable
fun AugmentingMenu(
    onRunAlgorithm: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopAppbar(
                title = "Javítóutas algoritmus",
                onBack = onBack,
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            Button(onClick = onRunAlgorithm) {
                Text("Algoritmus futtatása")
            }
        }
    }
}