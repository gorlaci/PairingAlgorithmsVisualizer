package hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.menu

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
import pairingalgorithmsvisualizer.shared.generated.resources.Res
import pairingalgorithmsvisualizer.shared.generated.resources.draw_menu_screen

@Composable
fun VisualGraphDrawingMenu(
    onBack: () -> Unit,
    onDirected: () -> Unit,
    onUndirected: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopAppbar(
                title = stringResource(Res.string.draw_menu_screen),
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
            Button(onClick = onDirected) {
                Text("Irányított")
            }

            Spacer(modifier = Modifier.padding(50.dp))

            Button(onClick = onUndirected) {
                Text("Irányítatlan")
            }
        }
    }
}
