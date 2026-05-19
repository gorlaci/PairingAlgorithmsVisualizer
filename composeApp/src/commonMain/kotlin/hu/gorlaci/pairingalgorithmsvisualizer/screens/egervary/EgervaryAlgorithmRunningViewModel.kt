package hu.gorlaci.pairingalgorithmsvisualizer.screens.egervary

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningViewModel

class EgervaryAlgorithmRunningViewModel(
    graphStorage: GraphStorage,
) : AlgorithmRunningViewModel(graphStorage) {

    override val initString = ""

    override val graphList = graphStorage.getAllEgervaryGraphs()

    override val selectedGraph = derivedStateOf {
        graphList[selectedGraphIndex.value]
    }

    init {
        selectedGraph.value.createClasses()
    }

    private val _steps = mutableStateOf(listOf(selectedGraph.value.toGraphicalGraph()))

    override val steps = derivedStateOf {
        _steps.value
    }

    override fun onGraphSelected(index: Int) {
        selectedGraphIndex.value = index
        _steps.value = listOf(selectedGraph.value.toGraphicalGraph())
        step.value = 0
        runEnabled.value = true
        selectedGraph.value.createClasses()
    }

    override fun onRun() {
        val graph = selectedGraph.value

        try {
            graph.runAlgorithm()
        } catch (e: Exception) {
            println("Hiba az algoritmus futtatása közben: ${e.message}")
        }

        _steps.value = graph.steps
        step.value = 0

        runEnabled.value = false
    }

    val class1names = derivedStateOf {
        selectedGraph.value.class1.map { it.name }
    }

    val class2names = derivedStateOf {
        selectedGraph.value.class2.map { it.name }
    }

    override fun onTap(
        x: Double,
        y: Double,
    ) {
    }
}
