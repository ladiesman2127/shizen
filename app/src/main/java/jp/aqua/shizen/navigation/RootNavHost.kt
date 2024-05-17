package jp.aqua.shizen.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.aqua.shizen.home.HomeScreen
import jp.aqua.shizen.AppViewModelProvider
import jp.aqua.shizen.dictionary.presentation.DictionaryViewModel
import jp.aqua.shizen.listen.presentation.ListenViewModel
import jp.aqua.shizen.read.presentation.ReadViewModel

object Graph {
    const val ROOT = "root_graph"
    const val HOME = "home_graph"
}

@Composable
fun RootNavHost(
    navController: NavHostController = rememberNavController(),
    readViewModel: ReadViewModel = viewModel(factory = AppViewModelProvider.Factory),
    listenViewModel: ListenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    dictionaryViewModel: DictionaryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.HOME,
    ) {
        composable(route = Graph.HOME) {
            HomeScreen(
                readViewModel = readViewModel,
                listenViewModel = listenViewModel,
                dictionaryViewModel = dictionaryViewModel
            )
        }
    }
}
