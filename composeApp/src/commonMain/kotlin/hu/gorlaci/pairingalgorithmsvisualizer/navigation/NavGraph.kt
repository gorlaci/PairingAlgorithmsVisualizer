package hu.gorlaci.pairingalgorithmsvisualizer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.menu.AugmentingMenuScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.runalgorithm.AugmentingAlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.matrixbipartite.MatrixBipartiteGraphMakerScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.matrixbipartiteweighted.MatrixBipartiteWeightedGraphMakerScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.menu.GraphDrawingMenu
import hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.visual.GraphDrawingScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds.menu.EdmondsMenuScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds.quiz.EdmondsQuizScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds.runalgorithm.EdmondsAlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.egervary.menu.EgervaryMenuScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.egervary.runalgorithm.EgervaryAlgorithmRunningViewScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.mainmenu.MainMenuScreen

@Composable
fun NavGraph(
    graphStorage: GraphStorage,
    navHostController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.MainMenu,
    ) {
        composable<Screen.DrawGraph.Visual> {
            GraphDrawingScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.Edmonds.RunAlgorithm> {
            EdmondsAlgorithmRunningScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.Edmonds.Menu> {
            EdmondsMenuScreen(
                onDrawGraphClick = {
                    navHostController.navigate(Screen.DrawGraph.Visual)
                },
                onRunAlgorithmClick = {
                    navHostController.navigate(Screen.Edmonds.RunAlgorithm)
                },
                onPlayQuizClick = {
                    navHostController.navigate(Screen.Edmonds.Quiz)
                },
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.Edmonds.Quiz> {
            EdmondsQuizScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.MainMenu> {
            MainMenuScreen(
                onDrawGraphClick = { navHostController.navigate(Screen.DrawGraph.Menu) },
                onEdmondsClick = { navHostController.navigate(Screen.Edmonds.Menu) },
                onAugmentingPathClick = {
                    navHostController.navigate(Screen.AugmentingPath.Menu)
                },
                onEgervaryClick = {
                    navHostController.navigate(Screen.Egervary.Menu)
                },
            )
        }

        composable<Screen.AugmentingPath.Menu> {
            AugmentingMenuScreen(
                onDrawVisualClick = { navHostController.navigate(Screen.DrawGraph.Visual) },
                onDrawMatrixClick = {
                    navHostController.navigate(Screen.DrawGraph.MatrixBipartite)
                },
                onRunAlgorithm = { navHostController.navigate(Screen.AugmentingPath.RunAlgorithm) },
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.AugmentingPath.RunAlgorithm> {
            AugmentingAlgorithmRunningScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.DrawGraph.Menu> {
            GraphDrawingMenu(
                onBack = { navHostController.popBackStack() },
                onVisual = { navHostController.navigate(Screen.DrawGraph.Visual) },
                onMatrixBipartite = {
                    navHostController.navigate(Screen.DrawGraph.MatrixBipartite)
                },
                onMatrixBipartiteWeighted = {
                    navHostController.navigate(Screen.DrawGraph.MatrixBipartiteWeighted)
                },
            )
        }

        composable<Screen.DrawGraph.MatrixBipartite> {
            MatrixBipartiteGraphMakerScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.Egervary.RunAlgorithm> {
            EgervaryAlgorithmRunningViewScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.DrawGraph.MatrixBipartiteWeighted> {
            MatrixBipartiteWeightedGraphMakerScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.Egervary.Menu> {
            EgervaryMenuScreen(
                onDrawGraphClick = {
                    navHostController.navigate(Screen.DrawGraph.MatrixBipartiteWeighted)
                },
                onRunAlgorithmClick = {
                    navHostController.navigate(Screen.Egervary.RunAlgorithm)
                },
                onBack = { navHostController.popBackStack() },
            )
        }
    }
}
