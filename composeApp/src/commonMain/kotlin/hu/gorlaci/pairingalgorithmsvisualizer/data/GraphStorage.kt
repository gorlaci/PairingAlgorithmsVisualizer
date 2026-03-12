package hu.gorlaci.pairingalgorithmsvisualizer.data

import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.AugmentingPathGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsGraph

interface GraphStorage {
    fun addGraph(graph: Graph<out Vertex, out Edge>)

    fun getAllGraphs(): List<Graph<out Vertex, out Edge>>

    fun getAllEdmondsGraphs(): List<EdmondsGraph>

    fun getAllAugmentingPathGraphs(): List<AugmentingPathGraph>
}
