package jp.aqua.shizen.utils.scaffold

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.BookOpen
import compose.icons.evaicons.fill.Headphones
import compose.icons.evaicons.fill.Settings
import compose.icons.evaicons.fill.Star
import compose.icons.evaicons.outline.Book
import compose.icons.evaicons.outline.Headphones
import compose.icons.evaicons.outline.Settings
import compose.icons.evaicons.outline.Star
import jp.aqua.shizen.navigation.BotNavItem
import jp.aqua.shizen.navigation.ShizenScreen

@Composable
fun ShizenBotBar(currentScreen: ShizenScreen?, navController: NavHostController) {
    val items = listOf(
        BotNavItem(
            title = "Read",
            screen = ShizenScreen.Read,
            selectedIcon = EvaIcons.Fill.BookOpen,
            unselectedIcon = EvaIcons.Outline.Book
        ), BotNavItem(
            title = "Listen",
            screen = ShizenScreen.Listen,
            selectedIcon = EvaIcons.Fill.Headphones,
            unselectedIcon = EvaIcons.Outline.Headphones
        ), BotNavItem(
            title = "Dictionary",
            screen = ShizenScreen.Dictionary,
            selectedIcon = EvaIcons.Fill.Star,
            unselectedIcon = EvaIcons.Outline.Star
        ), BotNavItem(
            title = "Settings",
            screen = ShizenScreen.Settings,
            selectedIcon = EvaIcons.Fill.Settings,
            unselectedIcon = EvaIcons.Outline.Settings
        )
    )

    if (currentScreen != null) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    label = {
                        // Надпись иконки
                        Text(item.title)
                    },
                    selected = currentScreen.name == item.screen.name,
                    onClick = {
                        // Навигация на другой экран
                        navController.navigate(item.screen.name)
                    },
                    icon = {
                        // Иконка
                        Icon(
                            imageVector = if (currentScreen.name == item.screen.name) item.selectedIcon
                            else item.unselectedIcon, contentDescription = null
                        )
                    }
                )
            }
        }
    }
}



