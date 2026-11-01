package hu.gorlaci.pairingalgorithmsvisualizer

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import hu.gorlaci.pairingalgorithmsvisualizer.data.InMemoryGraphStorage

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        App(
            graphStorage = InMemoryGraphStorage()
        )
    }
}
