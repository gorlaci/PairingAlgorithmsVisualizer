package hu.gorlaci.pairingalgorithmsvisualizer.model.quiz

sealed class Answer {
    data object Correct : Answer()
    data class Incorrect(val correctAnswer: String) : Answer()
}
