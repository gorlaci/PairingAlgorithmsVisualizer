package hu.gorlaci.pairingalgorithmsvisualizer.data

import androidx.compose.runtime.State
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex

interface GraphStorage {
    fun storeGraph(graph: Graph<out Vertex, out Edge<out Vertex>>)

    fun getAllGraphs(): State<List<Graph<out Vertex, out Edge<out Vertex>>>>

    fun addGraph(graph: Graph<out Vertex, out Edge<out Vertex>>) {
        val center =
            graph.vertices.map { graph.getVertexCoordinates(it) }.reduce { acc, vector -> acc + vector } /
                graph.vertices.size.toDouble()

        graph.idCoordinatesMap.entries.forEach {
            graph.idCoordinatesMap[it.key] = it.value - center
        }

        storeGraph(graph)
    }
}
