package hu.gorlaci.pairingalgorithmsvisualizer.navigation

import kotlinx.serialization.Serializable

object Screen {

    object DrawGraph {

        @Serializable
        object Visual

        @Serializable
        object MatrixBipartite

        @Serializable
        object MatrixBipartiteWeighted

        @Serializable
        object Menu
    }

    object Edmonds {

        @Serializable
        object RunAlgorithm

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
    object Egervary {
        @Serializable
        object RunAlgorithm

        @Serializable
        object Menu
    }

    @Serializable
    object MainMenu
}
