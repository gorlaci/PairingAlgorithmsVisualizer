package hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.matrixbipartiteweighted

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.egervary.EgervaryGraph
import hu.gorlaci.pairingalgorithmsvisualizer.model.egervary.EgervaryVertex

class MatrixBipartiteWeightedGraphMakerViewmodel(
    private val graphStorage: GraphStorage,
) : ViewModel() {
    val rows = mutableStateOf(3)
    val columns = mutableStateOf(3)

    val adjacencyMatrix = mutableStateOf(List(3) { List(3) { 0 } })

    val name = mutableStateOf(DEFAULT_GRAPH_NAME)

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
            adjacencyMatrix.value += List(newValue - rows.value) { List(columns.value) { 0 } }
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
                adjacencyMatrix.value.map { it + List(newValue - columns.value) { 0 } }
        }
        columns.value = newValue
    }

    fun setAdjacencyMatrix(
        rowIndex: Int,
        columnIndex: Int,
        newValue: String,
    ) {
        val newWeight = if (newValue.isBlank()) {
            0
        } else {
            try {
                newValue.toInt()
            } catch (_: NumberFormatException) {
                return
            }
        }
        if (newWeight < 0) {
            return
        }
        val newAdjacencyMatrix = adjacencyMatrix.value.map { it.toMutableList() }
        newAdjacencyMatrix[rowIndex][columnIndex] = newWeight
        adjacencyMatrix.value = newAdjacencyMatrix
    }

    fun onNameChange(newValue: String) {
        name.value = newValue
    }

    private fun getGraph(): EgervaryGraph {
        val width = 700.0
        val height = 300.0

        val coordinateMap = mutableMapOf<String, Pair<Double, Double>>()

        val columnVertices = mutableListOf<EgervaryVertex>()
        for (i in 0..<columns.value) {
            val label = (i + 1).toString()
            columnVertices.add(EgervaryVertex(label))
            coordinateMap[label] = Pair(-width / 2 + width / (columns.value - 1) * i, height / 2)
        }
        val rowVertices = mutableListOf<EgervaryVertex>()
        for (i in 0..<rows.value) {
            val label = ('A' + i).toString()
            rowVertices.add(EgervaryVertex(label))
            coordinateMap[label] = Pair(-width / 2 + width / (rows.value - 1) * i, -height / 2)
        }

        val vertices = mutableSetOf<EgervaryVertex>()
        vertices.addAll(rowVertices)
        vertices.addAll(columnVertices)

        val graph = EgervaryGraph(
            name = name.value,
            vertices = vertices,
            idCoordinateMap = coordinateMap,
        )

        for (i in 0..<rows.value) {
            for (j in 0..<columns.value) {
                if (adjacencyMatrix.value[i][j] > 0) {
                    graph.addEdge(rowVertices[i], columnVertices[j], adjacencyMatrix.value[i][j])
                }
            }
        }

        return graph
    }

    fun saveGraph() {
        graphStorage.addGraph(getGraph())

        rows.value = 3
        columns.value = 3
        adjacencyMatrix.value = List(3) { List(3) { 0 } }
        name.value = DEFAULT_GRAPH_NAME
    }

    companion object {
        private const val DEFAULT_GRAPH_NAME = "Saját Súlyozott Páros Gráf"
    }
}
