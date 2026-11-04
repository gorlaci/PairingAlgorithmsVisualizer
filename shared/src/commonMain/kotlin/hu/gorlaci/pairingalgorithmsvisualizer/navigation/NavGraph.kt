package hu.gorlaci.pairingalgorithmsvisualizer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.gorlaci.pairingalgorithmsvisualizer.data.GraphStorage
import hu.gorlaci.pairingalgorithmsvisualizer.screens.TestScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.menu.AugmentingMenuScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.augmentingpath.runalgorithm.AugmentingAlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.BreadthFirstSearchAlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.bfs.quiz.BreadthFirstSearchQuizScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs.DepthFirstSearchAlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.dfs.quiz.DepthFirstSearchQuizScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.matrixbipartite.MatrixBipartiteGraphMakerScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.matrixbipartiteweighted.MatrixBipartiteWeightedGraphMakerScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.menu.GraphDrawingMenu
import hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.menu.VisualGraphDrawingMenu
import hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.visual.GraphDrawingScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.drawgraph.visualdirected.VisualDirectedGraphDrawingScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds.menu.EdmondsMenuScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds.quiz.EdmondsQuizScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.edmonds.runalgorithm.EdmondsAlgorithmRunningScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.egervary.menu.EgervaryMenuScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.egervary.runalgorithm.EgervaryAlgorithmRunningViewScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.mainmenu.MainMenuItem
import hu.gorlaci.pairingalgorithmsvisualizer.screens.mainmenu.MainMenuScreen
import hu.gorlaci.pairingalgorithmsvisualizer.screens.mainmenu.NewMainMenuScreen
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.shared.generated.resources.Res
import pairingalgorithmsvisualizer.shared.generated.resources.bfs
import pairingalgorithmsvisualizer.shared.generated.resources.bfs_algorithm

@Composable
fun NavGraph(
    graphStorage: GraphStorage,
    navHostController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.NewMainMenu,
    ) {
        composable<Screen.DrawGraph.Visual.Undirected> {
            GraphDrawingScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.Edmonds.RunAlgorithm> {
            EdmondsAlgorithmRunningScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
                onNewGraph = {
                    navHostController.navigate(Screen.DrawGraph.Visual.Undirected)
                },
            )
        }

        composable<Screen.Edmonds.Menu> {
            EdmondsMenuScreen(
                onDrawGraphClick = {
                    navHostController.navigate(Screen.DrawGraph.Visual.Undirected)
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
                onNewGraph = {
                    navHostController.navigate(Screen.DrawGraph.Visual.Undirected)
                },
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
                onDrawVisualClick = { navHostController.navigate(Screen.DrawGraph.Visual.Undirected) },
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
                onNewGraph = {
                    navHostController.navigate(Screen.AugmentingPath.Menu)
                },
            )
        }

        composable<Screen.DrawGraph.Menu> {
            GraphDrawingMenu(
                onBack = { navHostController.popBackStack() },
                onVisual = { navHostController.navigate(Screen.DrawGraph.Visual.Undirected) },
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
                onNewGraph = {
                    navHostController.navigate(Screen.DrawGraph.MatrixBipartiteWeighted)
                },
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

        composable<Screen.NewMainMenu> {
            val menuItems = listOf(
                MainMenuItem(
                    algorithmName = stringResource(Res.string.bfs_algorithm),
                    algorithmImage = imageResource(Res.drawable.bfs),
                    onRunAlgorithm = {
                        navHostController.navigate(Screen.BreadthFirstSearch.RunAlgorithm)
                    },
                    onQuiz = {
                        navHostController.navigate(Screen.BreadthFirstSearch.Quiz)
                    },
                ),
            )

            NewMainMenuScreen(menuItems = menuItems)
        }

        composable<Screen.BreadthFirstSearch.RunAlgorithm> {
            BreadthFirstSearchAlgorithmRunningScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
                onNewGraph = {
                    navHostController.navigate(Screen.DrawGraph.Visual.Menu)
                },
            )
        }

        composable<Screen.DepthFirstSearch.RunAlgorithm> {
            DepthFirstSearchAlgorithmRunningScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
                onNewGraph = {
                    navHostController.navigate(Screen.DrawGraph.Visual.Menu)
                },
            )
        }

        composable<Screen.BreadthFirstSearch.Quiz> {
            BreadthFirstSearchQuizScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
                onNewGraph = {
                    navHostController.navigate(Screen.DrawGraph.Visual.Menu)
                },
            )
        }

        composable<Screen.DepthFirstSearch.Quiz> {
            DepthFirstSearchQuizScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
                onNewGraph = {
                    navHostController.navigate(Screen.DrawGraph.Visual.Menu)
                },
            )
        }

        composable<Screen.Test> {
            TestScreen()
        }

        composable<Screen.DrawGraph.Visual.Directed> {
            VisualDirectedGraphDrawingScreen(
                graphStorage = graphStorage,
                onBack = { navHostController.popBackStack() },
            )
        }

        composable<Screen.DrawGraph.Visual.Menu> {
            VisualGraphDrawingMenu(
                onBack = { navHostController.popBackStack() },
                onDirected = { navHostController.navigate(Screen.DrawGraph.Visual.Directed) },
                onUndirected = { navHostController.navigate(Screen.DrawGraph.Visual.Undirected) },
            )
        }
    }
}
