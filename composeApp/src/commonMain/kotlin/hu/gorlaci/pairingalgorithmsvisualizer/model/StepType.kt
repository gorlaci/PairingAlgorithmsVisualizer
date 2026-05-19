package hu.gorlaci.pairingalgorithmsvisualizer.model

abstract class StepType(
    val description: String = "",
) {
    class Nothing(
        description: String = "",
    ) : StepType(description)

    class AugmentingPathFound(
        description: String = "",
    ) : StepType(description)
}
