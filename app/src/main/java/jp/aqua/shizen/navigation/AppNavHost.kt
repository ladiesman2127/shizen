package jp.aqua.shizen.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jp.aqua.shizen.dictionary.presentation.DictionaryScreen
import jp.aqua.shizen.item.model.Item
import jp.aqua.shizen.listen.presentation.ListenScreen
import jp.aqua.shizen.read.presentation.ReadScreen
import jp.aqua.shizen.settings.SettingsScreen
import jp.aqua.shizen.utils.ScreenUiState
import jp.aqua.shizen.utils.ScreenViewModel


enum class ShizenScreen(
    val title: String,
) {
    Read("Read Library"),
    Listen("Listen Library"),
    Dictionary("Dictionary"),
    Settings("Settings"),
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    // Контроллер навигации
    navController: NavHostController,
    // ViewModel текущего экрана
    viewModel: ScreenViewModel,
    // Состояние UI текущего экрана
    uiState: ScreenUiState,
    // Лист объектов текущего экрана
    items: List<Item>
) {
    // add word bd and color only words which in learning
    NavHost(
        navController = navController,
        // Начальный экран
        startDestination = ShizenScreen.Read.name,
        // Удаление анимаций для более плавной работы
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = ShizenScreen.Read.name) {
            // Экран для чтения книг
            ReadScreen(
                modifier = modifier.fillMaxSize(),
                uiState = uiState,
                viewModel = viewModel,
                items = items
            )
        }
        composable(route = ShizenScreen.Listen.name) {
            // Экран для прослушивания/просмотра
            ListenScreen(
                modifier = modifier.fillMaxSize(),
                uiState = uiState,
                viewModel = viewModel,
                items = items
            )
        }
        composable(route = ShizenScreen.Dictionary.name) {
            // Словарь
            DictionaryScreen(
                modifier = modifier.fillMaxSize(),
                uiState = uiState,
                viewModel = viewModel,
                items = items
            )
        }
        composable(route = ShizenScreen.Settings.name) {
            // Найстроки
            SettingsScreen(
                modifier = modifier.fillMaxSize(),
            )
        }
    }
}

