package hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.runalgorithm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.AugmentingPathGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph

class AugmentingAlgorithmRunningViewModel(
    val graphStorage: GraphStorage
) : ViewModel() {
    val graphList = graphStorage.getAllAugmentingPathGraphs()
    private var selectedGraphIndex = 0

    val selectedGraph: MutableState<AugmentingPathGraph> = mutableStateOf(graphList[selectedGraphIndex])

    private val steps: MutableList<Pair<GraphicalGraph, AugmentingPathGraph>> = mutableListOf(
        selectedGraph.value.toGraphicalGraph() to AugmentingPathGraph()
    )
    private var step = 0

    val graphicalGraph = mutableStateOf(steps[0].first)
    val tree = mutableStateOf(
        GraphicalGraph(
            listOf(),
            listOf(),
            StepType()
        )
    )
    val class1Ids = mutableStateOf(emptyList<String>())
    val class2Ids = mutableStateOf(emptyList<String>())

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
        graphicalGraph.value = steps[step].first
        tree.value = steps[step].second.toGraphicalGraph()
    }

    private fun setButtons() {
        nextEnabled.value = step < steps.size - 1
        backEnabled.value = step > 0
    }

    fun onGraphSelected(index: Int) {
        selectedGraphIndex = index
        selectedGraph.value = graphList[selectedGraphIndex]

        steps.clear()
        steps.add(
            selectedGraph.value.toGraphicalGraph() to AugmentingPathGraph()
        )
        step = 0
        getClasses()
        setCurrentGraph()
        setButtons()
        runEnabled.value = true
    }

    private fun getClasses() {
        val graph = selectedGraph.value
        class1Ids.value = graph.class1.map { it.id }
        class2Ids.value = graph.class2.map { it.id }
    }

    fun onRun() {
        val graph = graphList[selectedGraphIndex]

        steps.clear()
        graph.runAlgorithm()
        steps.addAll(graph.steps)

        step = 0
        getClasses()
        setCurrentGraph()
        setButtons()
        runEnabled.value = false
    }

    val matrixMode = mutableStateOf(false)

    fun changeMatrixMode(newValue: Boolean) {
        matrixMode.value = newValue
    }

}