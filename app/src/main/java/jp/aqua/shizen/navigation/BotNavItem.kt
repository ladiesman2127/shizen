package jp.aqua.shizen.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BotNavItem(
    val title: String,
    val screen: ShizenScreen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
