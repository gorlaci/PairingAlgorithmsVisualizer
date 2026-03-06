package hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.runalgorithm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage

class AlgorithmRunningViewModel(
    val graphStorage: GraphStorage
) : ViewModel() {
    val graphList = graphStorage.getAllAugmentingPathGraphs()
    private var selectedGraphIndex = 0

    val selectedGraph = mutableStateOf(graphList[selectedGraphIndex])

    private val steps = mutableListOf(selectedGraph.value.toGraphicalGraph())
    private var step = 0

    val graphicalGraph = mutableStateOf(steps[0])

    val nextEnabled = mutableStateOf(false)
    val backEnabled = mutableStateOf(false)
    val runEnabled = mutableStateOf(true)

    fun onNext() {
        if (step < steps.size - 1) {
            step++

            setCurrentGraph()
        }
        setButtons()
    }

    fun onBack() {
        if (step > 0) {
            step--
            setCurrentGraph()
        }
        setButtons()
    }

    private fun setCurrentGraph() {
        graphicalGraph.value = steps[step]
    }

    private fun setButtons() {
        nextEnabled.value = step < steps.size - 1
        backEnabled.value = step > 0
    }

    fun onGraphSelected(index: Int) {
        selectedGraphIndex = index
        selectedGraph.value = graphList[selectedGraphIndex]

        steps.clear()
        steps.add(selectedGraph.value.toGraphicalGraph())
        step = 0
        setCurrentGraph()
        setButtons()
    }

    fun onRun() {
        val graph = graphList[selectedGraphIndex]

        steps.clear()
        graph.runAlgorithm()
        steps.addAll(graph.steps)

        step = 0
        setCurrentGraph()
        setButtons()
        runEnabled.value = false
    }
}