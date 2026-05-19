package hu.gorlaci.pairingalgorithmsvisualizer.screens.egervary

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage

class EgervaryAlgorithmRunningViewModel(
    val graphStorage: GraphStorage,
) : ViewModel() {
    val graphList = graphStorage.getAllEgervaryGraphs()
    private val selectedGraphIndex = mutableStateOf(0)

    val selectedGraph = derivedStateOf {
        graphList[selectedGraphIndex.value]
    }

    init {
        selectedGraph.value.createClasses()
    }

    private val steps = mutableStateOf(listOf(selectedGraph.value.toGraphicalGraph()))

    val step = mutableStateOf(0)

    val maxSteps = derivedStateOf {
        steps.value.size
    }

    val graphicalGraph = derivedStateOf {
        steps.value[step.value]
    }

    val nextEnabled = derivedStateOf {
        step.value < steps.value.size - 1
    }
    val backEnabled = derivedStateOf {
        step.value > 0
    }
    val runEnabled = mutableStateOf(true)

    fun onNext() {
        step.value++
    }

    fun onBack() {
        step.value--
    }

    fun onStepChange(newValue: String) {
        val newStep = try {
            newValue.toInt() - 1
        } catch (_: NumberFormatException) {
            return
        }
        if (newStep < 0) {
            return
        }
        if (newStep >= maxSteps.value) {
            return
        }
        step.value = newStep
    }

    fun onGraphSelected(index: Int) {
        selectedGraphIndex.value = index
        steps.value = listOf(selectedGraph.value.toGraphicalGraph())
        step.value = 0
        runEnabled.value = true
        selectedGraph.value.createClasses()
    }

    fun onRun() {
        val graph = selectedGraph.value

        try {
            graph.runAlgorithm()
        } catch (e: Exception) {
            println("Hiba az algoritmus futtatása közben: ${e.message}")
        }

        steps.value = graph.steps
        step.value = 0

        runEnabled.value = false
    }

    val class1names = derivedStateOf {
        selectedGraph.value.class1.map { it.name }
    }

    val class2names = derivedStateOf {
        selectedGraph.value.class2.map { it.name }
    }
}
