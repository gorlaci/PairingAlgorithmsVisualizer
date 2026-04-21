package hu.gorlaci.pairingalgorithmsvisualizer.features.drawgraph

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
fun GraphDrawingMenu(
    onBack: () -> Unit,
    onVisual: () -> Unit,
    onMatrixBipartite: () -> Unit,
) {
    Scaffold(
    topBar = {
        SimpleTopAppbar(
            title = "Saját gráf megadása",
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
        Button(onClick = onVisual) {
            Text("Gráf rajzolása")
        }

        Button(onClick = onMatrixBipartite) {
            Text("Páros gráf táblázattal")
        }
    }
}
}