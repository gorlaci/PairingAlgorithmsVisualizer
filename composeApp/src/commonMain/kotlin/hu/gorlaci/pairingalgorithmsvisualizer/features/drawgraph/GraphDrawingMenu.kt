package hu.gorlaci.pairingalgorithmsvisualizer.features.drawgraph

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.SimpleTopAppbar
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.draw_custom_screen

@Composable
fun GraphDrawingMenu(
    onBack: () -> Unit,
    onVisual: () -> Unit,
    onMatrixBipartite: () -> Unit,
    onMatrixBipartiteWeighted: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopAppbar(
                title = stringResource(Res.string.draw_custom_screen),
                onBack = onBack,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            Button(onClick = onVisual) {
                Text("Gráf rajzolása")
            }

            Spacer(modifier = Modifier.padding(50.dp))

            Button(onClick = onMatrixBipartite) {
                Text("Páros gráf táblázattal")
            }

            Spacer(modifier = Modifier.padding(50.dp))

            Button(onClick = onMatrixBipartiteWeighted) {
                Text("Súlyozott páros gráf táblázattal")
            }
        }
    }
}
