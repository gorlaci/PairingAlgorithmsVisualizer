package hu.gorlaci.pairingalgorithmsvisualizer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.menu.AugmentingMenu
import hu.gorlaci.pairingalgorithmsvisualizer.features.augmentingpath.runalgorithm.AugmentingAlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.features.drawgraph.GraphDrawingScreen
import hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.edmodsmenu.EdmondsMenuScreen
import hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.quiz.EdmondsQuizScreen
import hu.gorlaci.pairingalgorithmsvisualizer.features.edmonds.run_algorithm.EdmondsAlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.features.mainmenu.MainMenuScreen

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
            EdmondsMenuScreen(
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
                onDrawClick = { navHostController.navigate(Screen.Edmonds.GraphDrawing) },
                onEdmondsMenuClick = { navHostController.navigate(Screen.Edmonds.Menu) },
                onAugmentingPathMenuClick = { navHostController.navigate(Screen.AugmentingPath.Menu) },
            )
        }

        composable<Screen.AugmentingPath.Menu> {
            AugmentingMenu(
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
    }
}
