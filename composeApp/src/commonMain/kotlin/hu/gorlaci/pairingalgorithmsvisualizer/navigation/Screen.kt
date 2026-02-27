package hu.gorlaci.pairingalgorithmsvisualizer.navigation

import kotlinx.serialization.Serializable


object Screen{

    object Edmonds {

        @Serializable
        object Canvas

        @Serializable
        object GraphDrawing

        @Serializable
        object Menu

        @Serializable
        object Quiz
    }

    @Serializable
    object MainMenu
}
