package hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.quiz

import hu.gorlaci.pairingalgorithmsvisualizer.model.bfs.BreadthFirstSearchVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.StepType

sealed class BreadthFirstSearchStepType(
    description: String,
) : StepType(description) {
    class Nothing(
        description: String = "",
    ) : BreadthFirstSearchStepType(description)

    class SelectedVertex(
        description: String,
        val vertex: BreadthFirstSearchVertex,
    ) : BreadthFirstSearchStepType(description)

    class SelectedNeighbour(
        description: String,
        val vertex: BreadthFirstSearchVertex,
        val neighbour: BreadthFirstSearchVertex,
        val neighbourStatus: NeighbourStatus,
    ) : BreadthFirstSearchStepType(description)
}

enum class NeighbourStatus {
    ADDED,
    SKIPPED,
}
