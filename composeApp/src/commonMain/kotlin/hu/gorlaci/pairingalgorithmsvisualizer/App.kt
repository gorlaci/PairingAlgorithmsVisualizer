package hu.gorlaci.pairingalgorithmsvisualizer

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.navigation.NavGraph

@Composable
@Preview
fun App(graphStorage: GraphStorage) {
    MaterialTheme {
        NavGraph(
            graphStorage = graphStorage,
        )
    }
}
