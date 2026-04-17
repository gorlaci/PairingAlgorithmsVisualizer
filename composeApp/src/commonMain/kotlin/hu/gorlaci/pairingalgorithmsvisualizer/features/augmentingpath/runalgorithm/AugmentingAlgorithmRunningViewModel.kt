package hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.runalgorithm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.AugmentingPathGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.GraphDisplayMode
import hu.gorlaci.pairingalgorithmsvisualizer.ui.model.GraphicalGraph

class AugmentingAlgorithmRunningViewModel(
    val graphStorage: GraphStorage
) : ViewModel() {
    val graphList = graphStorage.getAllAugmentingPathGraphs()
    private var selectedGraphIndex = 0

    val selectedGraph: MutableState<AugmentingPathGraph> = mutableStateOf(graphList[selectedGraphIndex])

    private val steps = mutableStateOf(
        listOf(
            selectedGraph.value.toGraphicalGraph() to AugmentingPathGraph()
        )
    )
    val step = mutableStateOf(0)

    val maxSteps = derivedStateOf {
        steps.value.size
    }

    val graphicalGraph = mutableStateOf(steps.value[0].first)
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
        if (step.value < steps.value.size - 1) {
            step.value++

            setCurrentGraph()
        }
        setButtons()
    }

    fun onBack() {
        if (step.value > 0) {
            step.value--
            setCurrentGraph()
        }
        setButtons()
    }

    fun onStepChange(newValue: String) {
        val int = try {
            newValue.toInt() - 1
        } catch (_: NumberFormatException) {
            return
        }
        if (int < 0) {
            return
        }
        if (int >= steps.value.size) {
            return
        }
        step.value = int
        setCurrentGraph()
        setButtons()
    }

    private fun setCurrentGraph() {
        graphicalGraph.value = steps.value[step.value].first
        tree.value = steps.value[step.value].second.toGraphicalGraph()
    }

    private fun setButtons() {
        nextEnabled.value = step.value < steps.value.size - 1
        backEnabled.value = step.value > 0
    }

    fun onGraphSelected(index: Int) {
        selectedGraphIndex = index
        selectedGraph.value = graphList[selectedGraphIndex]

        steps.value = listOf(selectedGraph.value.toGraphicalGraph() to AugmentingPathGraph())
        step.value = 0
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

        graph.runAlgorithm()

        steps.value = graph.steps

        step.value = 0
        getClasses()
        setCurrentGraph()
        setButtons()
        runEnabled.value = false
    }

    val graphDisplayMode = mutableStateOf(GraphDisplayMode.BOTH)

    fun changeDisplayMode(newValue: GraphDisplayMode) {
        graphDisplayMode.value = newValue
    }

}