package jp.aqua.shizen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jp.aqua.shizen.dictionary.presentation.DictionaryViewModel
import jp.aqua.shizen.listen.components.AddFolderDialog
import jp.aqua.shizen.listen.presentation.ListenViewModel
import jp.aqua.shizen.navigation.AppNavHost
import jp.aqua.shizen.navigation.ShizenScreen
import jp.aqua.shizen.read.presentation.ReadViewModel
import jp.aqua.shizen.utils.scaffold.ScreenTopBar
import jp.aqua.shizen.utils.scaffold.ShizenBotBar
import jp.aqua.shizen.utils.scaffold.ShizenScreenFab
import jp.aqua.shizen.utils.scaffold.ShizenSnackbar


@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    readViewModel: ReadViewModel,
    listenViewModel: ListenViewModel,
    dictionaryViewModel: DictionaryViewModel
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ShizenScreen.valueOf(
        backStackEntry?.destination?.route ?: ShizenScreen.Read.name
    )

    val viewModel = when (currentScreen) {
        ShizenScreen.Read -> readViewModel
        ShizenScreen.Listen -> listenViewModel
        ShizenScreen.Dictionary -> dictionaryViewModel
        else -> readViewModel
    }

    val uiState by viewModel.uiState.collectAsState()
    val items by uiState.items.collectAsState(emptyList())

    val snackbarHostState = remember { SnackbarHostState() }

    if (uiState.isInAddingFolder == true) {
        AddFolderDialog(
            uiState = uiState,
            turnOffFolderAdding = viewModel::turnOffFolderAdding,
            saveFolder = viewModel::addFolder,
            updateFolderCover = viewModel::updateFolderCover,
            updateIsCoverError = viewModel::updateIsCoverError,
            updateFolderTitle = viewModel::updateFolderTitle
        )
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    ShizenSnackbar(data = data)
                }
            )
        },
        topBar = {
            ScreenTopBar(
                currentScreen = currentScreen,
                uiState = uiState,
                onClearSelection = viewModel::clearSelection,
                onSelectAll = { viewModel.selectAll(items) },
                onRemoveSelectedItems = viewModel::removeSelected,
                onUpdateFilter = viewModel::updateFilter,
                onUpdateSort = viewModel::updateSort,
                onSearchQueryChange = viewModel::updateSearchQuery,
                onUpdateIsInSearch = viewModel::updateIsInSearch,
                onClearSearchQuery = viewModel::clearSearchQuery
            )
        },
        bottomBar = {
            ShizenBotBar(currentScreen, navController)
        },
        floatingActionButton = {
            ShizenScreenFab(
                currentScreen = currentScreen,
                addBook = viewModel::addBook,
                snackbarHostState = snackbarHostState,
                turnOnAddingFolder = viewModel::turnOnFolderAdding
            ) {
                Icon(Icons.Filled.Add, null)
            }
        },
    ) { innerPadding ->
        AppNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController,
            viewModel = viewModel,
            uiState = uiState,
            items = items
        )
    }
}





