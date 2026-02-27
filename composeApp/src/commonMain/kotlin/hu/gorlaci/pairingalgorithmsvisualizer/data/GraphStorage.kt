package hu.gorlaci.pairingalgorithmsvisualizer.data

import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.AugmentingPathGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsGraph

interface GraphStorage {
    fun addGraph(graph: Graph)

    fun getAllGraphs(): List<Graph>

    fun getAllEdmondsGraphs(): List<EdmondsGraph>

    fun getAllAugmentingPathGraphs(): List<AugmentingPathGraph>
}
