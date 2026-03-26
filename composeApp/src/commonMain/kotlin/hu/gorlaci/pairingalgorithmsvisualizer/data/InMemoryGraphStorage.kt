package hu.gorlaci.pairingalgorithmsvisualizer.data

import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.AugmentingPathGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.toAugmentingPathGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.toEdmondsGraph

class InMemoryGraphStorage : GraphStorage {
    private val graphs: MutableList<Graph<out Vertex, out Edge>> = mutableListOf()

    init {
        addExampleGraphs()
    }

    override fun addGraph(graph: Graph<out Vertex, out Edge>) {
        graphs.add(graph)
    }

    override fun getAllGraphs(): List<Graph<out Vertex, out Edge>> = graphs

    override fun getAllEdmondsGraphs(): List<EdmondsGraph> {
        return graphs.map { it.toEdmondsGraph() }
    }

    override fun getAllAugmentingPathGraphs(): List<AugmentingPathGraph> =
        graphs.filter { it.isBipartite }.map { it.toAugmentingPathGraph() }

    private fun addExampleGraphs() {
        addExampleGraph1()
        addExampleGraph2()
        addExampleGraph3()
        addExampleGraph4()
    }

    private fun addExampleGraph1() {
        val graph =
            Graph(
                vertices =
                    mutableSetOf(
                        Vertex(id = "A"),
                        Vertex(id = "B"),
                        Vertex(id = "C"),
                        Vertex(id = "D"),
                        Vertex(id = "E"),
                        Vertex(id = "F"),
                        Vertex(id = "G"),
                        Vertex(id = "H"),
                        Vertex(id = "I"),
                    ),
                idCoordinatesMap =
                    mutableMapOf(
                        'A' to Pair(-100.0, 200.0),
                        'B' to Pair(0.0, 200.0),
                        'C' to Pair(100.0, 200.0),
                        'D' to Pair(-200.0, 0.0),
                        'E' to Pair(0.0, 0.0),
                        'F' to Pair(200.0, 0.0),
                        'G' to Pair(-100.0, -200.0),
                        'H' to Pair(0.0, -200.0),
                        'I' to Pair(100.0, -200.0),
                    ),
                name = "Example Graph 1",
                newVertex = { Vertex(it) },
                newEdge = { from, to -> Edge(from, to) },
            )

        graph.addEdge("E", "D")
        graph.addEdge("D", "A")
        graph.addEdge("E", "A")
        graph.addEdge("F", "I")
        graph.addEdge("E", "F")
        graph.addEdge("E", "I")
        graph.addEdge("E", "C")
        graph.addEdge("A", "B")
        graph.addEdge("I", "H")
        graph.addEdge("C", "F")
        graph.addEdge("G", "D")
        graph.addEdge("H", "G")
        graph.addEdge("B", "C")
        graph.addEdge("E", "G")

        addGraph(graph)
    }

    private fun addExampleGraph2() {
        val graph =
            Graph(
                vertices =
                    mutableSetOf(
                        Vertex(id = "A"),
                        Vertex(id = "B"),
                        Vertex(id = "C"),
                        Vertex(id = "D"),
                        Vertex(id = "E"),
                        Vertex(id = "F"),
                        Vertex(id = "G"),
                    ),
                idCoordinatesMap =
                    mutableMapOf(
                        'A' to Pair(-50.0, 150.0),
                        'B' to Pair(-100.0, 50.0),
                        'C' to Pair(0.0, 50.0),
                        'D' to Pair(-50.0, -50.0),
                        'E' to Pair(50.0, -50.0),
                        'F' to Pair(0.0, -150.0),
                        'G' to Pair(100.0, -150.0),
                    ),
                name = "Example Graph 2",
                newVertex = { Vertex(it) },
                newEdge = { from, to -> Edge(from, to) },
            )

        graph.addEdge("A", "B")
        graph.addEdge("A", "C")
        graph.addEdge("B", "C")
        graph.addEdge("C", "D")
        graph.addEdge("D", "E")
        graph.addEdge("E", "C")
        graph.addEdge("E", "F")
        graph.addEdge("F", "G")
        graph.addEdge("G", "E")

        addGraph(graph)
    }

    private fun addExampleGraph3() {
        val vertices = ('A'..'G').map {
            Vertex(id = it.toString())
        }
        val edges = mutableSetOf(
            Edge(fromVertex = vertices[0], toVertex = vertices[3]),
            Edge(fromVertex = vertices[0], toVertex = vertices[4]),
            Edge(fromVertex = vertices[0], toVertex = vertices[5]),
            Edge(fromVertex = vertices[1], toVertex = vertices[4]),
            Edge(fromVertex = vertices[1], toVertex = vertices[5]),
            Edge(fromVertex = vertices[1], toVertex = vertices[6]),
            Edge(fromVertex = vertices[2], toVertex = vertices[3]),
        )
        val graph = Graph(
            vertices = vertices.toMutableSet(),
            edges = edges,
            idCoordinatesMap =
                mutableMapOf(
                    'A' to Pair(-100.0, -100.0),
                    'B' to Pair(0.0, -100.0),
                    'C' to Pair(100.0, -100.0),
                    'D' to Pair(-150.0, 100.0),
                    'E' to Pair(-50.0, 100.0),
                    'F' to Pair(50.0, 100.0),
                    'G' to Pair(150.0, 100.0),
                ),
            name = "Example Graph 3",
            newVertex = { Vertex(it) },
            newEdge = { from, to -> Edge(from, to) },
        )
        addGraph(graph)
    }

    private fun addExampleGraph4() {
        val vertices = mutableSetOf<Vertex>()
        val coordinates = mutableMapOf<Char, Pair<Double, Double>>()
        ('A'..'I').forEachIndexed { i, it ->
            vertices.add(Vertex(id = it.toString()))
            coordinates[it] = Pair(-250.0 + i * (500.0 / 8), -100.0)
        }
        (0..9).forEachIndexed { i, it ->
            vertices.add(Vertex(id = it.toString()))
            val shiftedIndex = (i + 9) % 10
            coordinates[it.toString()[0]] = Pair(-300.0 + shiftedIndex * (600.0 / 9), 100.0)
        }
        val graph = Graph(
            name = "Example Graph 4",
            vertices = vertices,
            edges = mutableSetOf(),
            idCoordinatesMap = coordinates,
            newVertex = { Vertex(it) },
            newEdge = { from, to -> Edge(from, to) },
        )
        graph.addEdge("A", "1")
        graph.addEdge("A", "7")
        graph.addEdge("A", "9")
        graph.addEdge("A", "0")
        graph.addEdge("B", "2")
        graph.addEdge("B", "5")
        graph.addEdge("C", "3")
        graph.addEdge("C", "5")
        graph.addEdge("C", "6")
        graph.addEdge("C", "8")
        graph.addEdge("C", "9")
        graph.addEdge("D", "4")
        graph.addEdge("D", "7")
        graph.addEdge("D", "8")
        graph.addEdge("D", "9")
        graph.addEdge("E", "4")
        graph.addEdge("E", "5")
        graph.addEdge("F", "6")
        graph.addEdge("F", "7")
        graph.addEdge("F", "8")
        graph.addEdge("F", "0")
        graph.addEdge("G", "2")
        graph.addEdge("G", "3")
        graph.addEdge("G", "4")
        graph.addEdge("H", "2")
        graph.addEdge("H", "3")
        graph.addEdge("I", "2")
        graph.addEdge("I", "3")
        addGraph(graph)
    }
}
