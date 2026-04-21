package hu.gorlaci.pairingalgorithmsvisualizer.features.drawgraph.matrixbipartite

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage

class MatrixBipartiteGraphMakerViewmodel(
    private val graphStorage: GraphStorage,
) : ViewModel() {
    val rows = mutableStateOf(3)
    val columns = mutableStateOf(3)

    val adjacencyMatrix = mutableStateOf(List(3) { List(3) { false } })

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
}
