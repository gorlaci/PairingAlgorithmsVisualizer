package hu.gorlaci.pairingalgorithmsvisualizer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import hu.gorlaci.pairingalgorithmsvisualizer.data.InMemoryGraphStorage

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "pairingalgorithmsvisualizer",
        ) {
            App(
                graphStorage = InMemoryGraphStorage(),
            )
        }
    }
