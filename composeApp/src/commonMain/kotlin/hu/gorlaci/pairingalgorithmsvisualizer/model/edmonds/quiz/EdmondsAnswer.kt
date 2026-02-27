package hu.gorlaci.pairingalgorithmsvisualizer.model.edmonds.quiz

sealed class EdmondsAnswer {
    data object Correct : EdmondsAnswer()
    data class Incorrect(val correctAnswer: String) : EdmondsAnswer()
}