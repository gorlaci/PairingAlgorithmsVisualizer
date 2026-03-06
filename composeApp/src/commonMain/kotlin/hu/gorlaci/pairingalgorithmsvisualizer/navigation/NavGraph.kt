package hu.gorlaci.pairingalgorithmsvisualizer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.menu.AugmentingMenu
import hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.drawgraph.GraphDrawingScreen
import hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.edmodsmenu.EdmondsMainMenuScreen
import hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.quiz.EdmondsQuizScreen
import hu.gorlaci.pairingalgorithmsvisualizer.features.mainmenu.MainMenuScreen
import hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.runalgorithm.AlgorithmRunningScreen as AugmentingAlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.run_algorithm.AlgorithmRunningScreen as EdmondsAlgorithmRunningScreen

@Composable
fun NavGraph(
    graphStorage: GraphStorage,
    navHostController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.MainMenu,
    ) {
        composable<Screen.Edmonds.GraphDrawing> {
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
            EdmondsMainMenuScreen(
                onDrawGraphClick = {
                    navHostController.navigate(Screen.Edmonds.GraphDrawing)
                },
                onRunAlgorithmClick = {
                    navHostController.navigate(Screen.Edmonds.RunAlgorithm)
                },
                onPlayQuizClick = {
                    navHostController.navigate(Screen.Edmonds.Quiz)
                },
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
                onEdmondsMenuClick = { navHostController.navigate(Screen.Edmonds.Menu) },
                onAugmentingPathMenuClick = { navHostController.navigate(Screen.AugmentingPath.Menu) },
            )
        }

        composable<Screen.AugmentingPath.Menu> {
            AugmentingMenu(
                onRunAlgorithm = { navHostController.navigate(Screen.AugmentingPath.RunAlgorithm) },
            )
        }

        composable<Screen.AugmentingPath.RunAlgorithm> {
            AugmentingAlgorithmRunningScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
            )
        }
    }
}
