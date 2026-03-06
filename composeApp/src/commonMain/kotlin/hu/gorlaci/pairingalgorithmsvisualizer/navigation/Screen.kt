package hu.gorlaci.pairingalgorithmsvisualizer.navigation

import kotlinx.serialization.Serializable


object Screen {

    object Edmonds {

        @Serializable
        object RunAlgorithm

        @Serializable
        object GraphDrawing

        @Serializable
        object Menu

        @Serializable
        object Quiz
    }

    object AugmentingPath {

        @Serializable
        object RunAlgorithm

        @Serializable
        object Menu

    }

    @Serializable
    object MainMenu
}
