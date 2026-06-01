package hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.quiz

import hu.gorlaci.pairingalgorithmsvisualizer.model.SkipPoint as SkipPointInterface
import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsBlossomVertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsEdge
import hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.EdmondsVertex

sealed class EdmondsStepType(
    description: String,
) : StepType(description) {
    class Nothing(
        description: String = "",
    ) : EdmondsStepType(description)

    class SelectedEdge(
        description: String,
        val edge: EdmondsEdge,
        val edgeType: EdmondsEdgeType,
    ) : EdmondsStepType(description)

    class MarkAugmentingPath(
        description: String,
        val currentEdge: EdmondsEdge,
        val pathEdges: Set<EdmondsEdge>,
    ) : EdmondsStepType(description), SkipPointInterface

    class MarkBlossom(
        description: String,
        val currentEdge: EdmondsEdge,
        val blossomEdges: Set<EdmondsEdge>,
    ) : EdmondsStepType(description), SkipPointInterface

    class DeconstructBlossom(
        description: String,
        val blossomVertex: EdmondsBlossomVertex,
    ) : EdmondsStepType(description)

    class BlossomInAnimation(
        description: String,
        val blossomVertices: Set<EdmondsVertex>,
    ) : EdmondsStepType(description)

    class BlossomOutAnimation(
        description: String,
        val blossomVertices: Set<EdmondsVertex>,
    ) : EdmondsStepType(description)

    class MaxPairingFound(
        description: String = "",
    ) : EdmondsStepType(description), SkipPointInterface
}
