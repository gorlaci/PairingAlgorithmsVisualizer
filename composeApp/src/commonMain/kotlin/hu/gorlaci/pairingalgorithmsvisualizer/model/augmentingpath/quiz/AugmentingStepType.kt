package hu.gorlaci.pairingalgorithmsvisualizer.model.augmentingpath.quiz

import hu.gorlaci.pairingalgorithmsvisualizer.model.StepType

sealed class AugmentingStepType(
    description: String,
): StepType(description) {
    class Nothing(
        description: String = "",
    ) : AugmentingStepType(description)
}