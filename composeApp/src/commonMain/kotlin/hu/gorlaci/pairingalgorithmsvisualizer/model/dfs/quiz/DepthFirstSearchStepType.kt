package hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.quiz

import hu.gorlaci.pairingalgorithmsvisualizer.model.dfs.DepthFirstSearchVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.StepType

sealed class DepthFirstSearchStepType(
    description: String,
) : StepType(description) {
    class Nothing(
        description: String = "",
    ) : DepthFirstSearchStepType(description)

    class SelectedVertex(
        description: String,
        val vertex: DepthFirstSearchVertex,
    ) : DepthFirstSearchStepType(description)

    class SelectedNeighbour(
        description: String,
        val vertex: DepthFirstSearchVertex,
        val neighbour: DepthFirstSearchVertex,
        val neighbourStatus: NeighbourStatus,
    ) : DepthFirstSearchStepType(description)
}

enum class NeighbourStatus {
    VISIT,
    SKIP,
}
