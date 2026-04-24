package hu.gorlaci.pairingalgorithmsvisualizer.features.drawgraph.matrixbipartite

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex

class MatrixBipartiteGraphMakerViewmodel(
    private val graphStorage: GraphStorage,
) : ViewModel() {
    val rows = mutableStateOf(3)
    val columns = mutableStateOf(3)

    val adjacencyMatrix = mutableStateOf(List(3) { List(3) { false } })

    val name = mutableStateOf("Saját páros gráf")

    fun setRows(newString: String) {
        val newValue = try {
            newString.toInt()
        } catch (_: NumberFormatException) {
            return
        }
        if (newValue < 0) {
            return
        }
        if (newValue < rows.value) {
            adjacencyMatrix.value = adjacencyMatrix.value.subList(0, newValue)
        } else if (newValue > rows.value) {
            adjacencyMatrix.value += List(newValue - rows.value) { List(columns.value) { false } }
        }
        rows.value = newValue
    }

    fun setColumns(newString: String) {
        val newValue = try {
            newString.toInt()
        } catch (_: NumberFormatException) {
            return
        }
        if (newValue < 0) {
            return
        }
        if (newValue < columns.value) {
            adjacencyMatrix.value = adjacencyMatrix.value.map { it.subList(0, newValue) }
        } else if (newValue > columns.value) {
            adjacencyMatrix.value =
                adjacencyMatrix.value.map { it + List(newValue - columns.value) { false } }
        }
        columns.value = newValue
    }

    fun setAdjacencyMatrix(
        rowIndex: Int,
        columnIndex: Int,
        newValue: Boolean,
    ) {
        val newAdjacencyMatrix = adjacencyMatrix.value.map { it.toMutableList() }
        newAdjacencyMatrix[rowIndex][columnIndex] = newValue
        adjacencyMatrix.value = newAdjacencyMatrix
    }

    fun onNameChange(newValue: String) {
        name.value = newValue
    }

    private fun getGraph(): Graph<Vertex, Edge> {
        val width = 700.0

        val coordinateMap = mutableMapOf<String, Pair<Double, Double>>()

        val columnVertices = mutableListOf<Vertex>()
        for (i in 0..<columns.value) {
            val label = (i + 1).toString()
            columnVertices.add(Vertex(label))
            coordinateMap[label] = Pair(-width / 2 + width / (columns.value - 1) * i, 200.0)
        }
        val rowVertices = mutableListOf<Vertex>()
        for (i in 0..<rows.value) {
            val label = ('A' + i).toString()
            rowVertices.add(Vertex(label))
            coordinateMap[label] = Pair(-width / 2 + width / (rows.value - 1) * i, -200.0)
        }

        val edges = mutableSetOf<Edge>()
        for (i in 0..<rows.value) {
            for (j in 0..<columns.value) {
                if (adjacencyMatrix.value[i][j]) {
                    edges.add(Edge(rowVertices[i], columnVertices[j]))
                }
            }
        }

        val vertices = mutableSetOf<Vertex>()
        vertices.addAll(rowVertices)
        vertices.addAll(columnVertices)

        val graph = Graph(
            name = name.value,
            vertices = vertices,
            edges = edges,
            idCoordinatesMap = coordinateMap,
            newVertex = { Vertex(it) },
            newEdge = { a, b -> Edge(a, b) },
        )

        return graph
    }

    fun saveGraph() {
        graphStorage.addGraph(getGraph())

        rows.value = 3
        columns.value = 3
        adjacencyMatrix.value = List(3) { List(3) { false } }
        name.value = "Saját páros gráf"
    }
}
