package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.quizscreen

import androidx.compose.runtime.mutableStateOf
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.model.Edge
import hu.gorlaci.pairingalgorithmsvisualizer.model.Graph
import hu.gorlaci.pairingalgorithmsvisualizer.model.Vertex
import hu.gorlaci.pairingalgorithmsvisualizer.model.quiz.Answer
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.algorithmrunningscreen.AlgorithmRunningViewModel

abstract class QuizScreenViewModel<
    GraphType : Graph<VertexType, EdgeType>,
    VertexType : Vertex,
    EdgeType : Edge<VertexType>,
    >(
    graphStorage: GraphStorage,
) : AlgorithmRunningViewModel<GraphType, VertexType, EdgeType>(graphStorage) {

    val quizStarted = mutableStateOf(false)

    val lastAnswer = mutableStateOf<Answer>(Answer.Correct)

    val questionFrequency = mutableStateOf(1f)
}
