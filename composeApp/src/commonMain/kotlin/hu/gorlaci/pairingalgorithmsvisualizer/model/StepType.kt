package hu.gorlaci.pairingalgorithmsvisualizer.model

import hu.gorlaci.pairingalgorithmsvisualizer.model.SkipPoint as SkipPointInterface

abstract class StepType(
    val description: String = "",
) {
    class Nothing(
        description: String = "",
    ) : StepType(description)

    class SkipPoint(
        description: String = "",
    ) : StepType(description), SkipPointInterface

    class AlgorithmEnd(
        description: String = "",
    ) : StepType(description), SkipPointInterface
}

interface SkipPoint
