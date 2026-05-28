package hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.runalgorithm

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.AugmentingPathGraph
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.GraphDisplayMode
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningViewModel

class AugmentingAlgorithmRunningViewModel(
    graphStorage: GraphStorage,
) : AlgorithmRunningViewModel(graphStorage) {

    override val graphList = graphStorage.getAllAugmentingPathGraphs()

    override val selectedGraph = derivedStateOf {
        graphList[selectedGraphIndex.value]
    }

    private val _steps = mutableStateOf(
        listOf(
            selectedGraph.value.toGraphicalGraph(StepType.Nothing(initString)) to
                AugmentingPathGraph(),
        ),
    )

    override val steps = derivedStateOf {
        _steps.value.map { it.first }
    }

    val tree = derivedStateOf {
        _steps.value[step.value].second.toGraphicalGraph()
    }
    val class1Ids = mutableStateOf(emptyList<String>())
    val class2Ids = mutableStateOf(emptyList<String>())

    override fun onGraphSelected(index: Int) {
        selectedGraphIndex.value = index

        selectedGraph.value.resetAlgorithm()

        step.value = 0

        _steps.value =
            listOf(
                selectedGraph.value.toGraphicalGraph(StepType.Nothing(initString)) to
                    AugmentingPathGraph(),
            )

        graphicalGraph.value = steps.value[0]

        selectedVertex = null

        inSetup.value = true
    }

    private fun getClasses() {
        val graph = selectedGraph.value
        class1Ids.value = graph.class1.map { it.name }
        class2Ids.value = graph.class2.map { it.name }
    }

    override fun onRun() {
        val graph = selectedGraph.value

        graph.runAlgorithm()

        _steps.value = graph.steps

        step.value = 0
        getClasses()
        inSetup.value = false

        selectedVertex = null

        graphicalGraph.value = steps.value[0]
    }

    val graphDisplayMode = mutableStateOf(GraphDisplayMode.BOTH)

    fun changeDisplayMode(newValue: GraphDisplayMode) {
        graphDisplayMode.value = newValue
    }
}
