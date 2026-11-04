package hu.gorlaci.pairingalgorithmsvisualizer.data

import androidx.compose.runtime.mutableStateOf
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.egervary.EgervaryGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.egervary.EgervaryVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.geometry.Vector2D

class InMemoryGraphStorage : GraphStorage {
    private val graphs = mutableStateOf(mutableListOf<Graph<out Vertex, out Edge<out Vertex>>>())

    init {
        addExampleGraphs()
    }

    override fun storeGraph(graph: Graph<out Vertex, out Edge<out Vertex>>) {
        graphs.value = graphs.value.toMutableList().apply { add(graph) }
    }

    override fun getAllGraphs() = graphs

    private fun addExampleGraphs() {
        addExampleGraph6()
        addExampleGraph1()
        addExampleGraph2()
        addExampleGraph3()
        addExampleGraph4()
        addExampleGraph5()
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
                        "A" to Vector2D(-100.0, 200.0),
                        "B" to Vector2D(0.0, 200.0),
                        "C" to Vector2D(100.0, 200.0),
                        "D" to Vector2D(-200.0, 0.0),
                        "E" to Vector2D(0.0, 0.0),
                        "F" to Vector2D(200.0, 0.0),
                        "G" to Vector2D(-100.0, -200.0),
                        "H" to Vector2D(0.0, -200.0),
                        "I" to Vector2D(100.0, -200.0),
                    ),
                name = "Példa Gráf 1",
                newEdge = { from, to -> Edge(from, to) },
                directed = false,
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
                        "A" to Vector2D(-50.0, 150.0),
                        "B" to Vector2D(-100.0, 50.0),
                        "C" to Vector2D(0.0, 50.0),
                        "D" to Vector2D(-50.0, -50.0),
                        "E" to Vector2D(50.0, -50.0),
                        "F" to Vector2D(0.0, -150.0),
                        "G" to Vector2D(100.0, -150.0),
                    ),
                name = "Példa Gráf 2",
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
                    "A" to Vector2D(-100.0, -100.0),
                    "B" to Vector2D(0.0, -100.0),
                    "C" to Vector2D(100.0, -100.0),
                    "D" to Vector2D(-150.0, 100.0),
                    "E" to Vector2D(-50.0, 100.0),
                    "F" to Vector2D(50.0, 100.0),
                    "G" to Vector2D(150.0, 100.0),
                ),
            name = "Példa Páros Gráf 1",
            newEdge = { from, to -> Edge(from, to) },
        )
        addGraph(graph)
    }

    private fun addExampleGraph4() {
        val vertices = mutableSetOf<Vertex>()
        val coordinates = mutableMapOf<String, Vector2D>()
        ('A'..'I').map { it.toString() }.forEachIndexed { i, it ->
            vertices.add(Vertex(id = it))
            coordinates[it] = Vector2D(-250.0 + i * (500.0 / 8), -100.0)
        }
        (0..9).forEachIndexed { i, it ->
            vertices.add(Vertex(id = it.toString()))
            val shiftedIndex = (i + 9) % 10
            coordinates[it.toString()] = Vector2D(-300.0 + shiftedIndex * (600.0 / 9), 100.0)
        }
        val graph = Graph(
            name = "Példa Páros Gráf 2",
            vertices = vertices,
            edges = mutableSetOf(),
            idCoordinatesMap = coordinates,
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

    private fun addExampleGraph5() {
        val vertices = mutableSetOf(
            EgervaryVertex("A"),
            EgervaryVertex("B"),
            EgervaryVertex("C"),
            EgervaryVertex("D"),
            EgervaryVertex("P"),
            EgervaryVertex("Q"),
            EgervaryVertex("R"),
            EgervaryVertex("S"),
        )
        val idCoordinatesMap = mutableMapOf(
            "A" to Vector2D(-300.0, -200.0),
            "B" to Vector2D(-100.0, -200.0),
            "C" to Vector2D(100.0, -200.0),
            "D" to Vector2D(300.0, -200.0),
            "P" to Vector2D(-300.0, 200.0),
            "Q" to Vector2D(-100.0, 200.0),
            "R" to Vector2D(100.0, 200.0),
            "S" to Vector2D(300.0, 200.0),
        )

        val graph = EgervaryGraph(
            name = "Példa Súlyozott Gráf 1",
            vertices = vertices,
            idCoordinateMap = idCoordinatesMap,
        )
        graph.addEdge("A", "P", weight = 8)
        graph.addEdge("A", "Q", weight = 3)
        graph.addEdge("A", "R", weight = 5)
        graph.addEdge("A", "S", weight = 4)
        graph.addEdge("B", "P", weight = 7)
        graph.addEdge("B", "Q", weight = 1)
        graph.addEdge("B", "R", weight = 6)
        graph.addEdge("B", "S", weight = 2)
        graph.addEdge("C", "P", weight = 9)
        graph.addEdge("C", "Q", weight = 3)
        graph.addEdge("C", "R", weight = 4)
        graph.addEdge("C", "S", weight = 1)
        graph.addEdge("D", "P", weight = 4)
        graph.addEdge("D", "Q", weight = 2)
        graph.addEdge("D", "R", weight = 7)
        graph.addEdge("D", "S", weight = 5)

        addGraph(graph)
    }

    private fun addExampleGraph6() {
        val vertices = mutableSetOf(
            Vertex("A"),
            Vertex("B"),
            Vertex("C"),
            Vertex("D"),
            Vertex("E"),
            Vertex("F"),
        )
        val coordinates = mutableMapOf(
            "A" to Vector2D(-100.0, 0.0),
            "B" to Vector2D(-50.0, -100.0),
            "C" to Vector2D(100.0, -100.0),
            "D" to Vector2D(-50.0, -200.0),
            "E" to Vector2D(200.0, 0.0),
            "F" to Vector2D(100.0, -200.0),
        )
        val graph = Graph(
            name = "Irányított Gráf 1",
            vertices = vertices,
            idCoordinatesMap = coordinates,
            newEdge = { from, to -> Edge(from, to) },
            directed = true,
        )
        graph.addEdge("A", "B")
        graph.addEdge("A", "C")
        graph.addEdge("A", "F")
        graph.addEdge("B", "D")
        graph.addEdge("C", "D")
        graph.addEdge("C", "F")
        graph.addEdge("D", "A")
        graph.addEdge("E", "F")

        addGraph(graph)
    }
}
