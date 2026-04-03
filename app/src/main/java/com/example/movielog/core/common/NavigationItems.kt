package com.example.movielog.core.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import com.example.movielog.core.navigation.Routes

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Home : BottomNavItem(Routes.HOME, "Home", Icons.Default.Home)
    object Search : BottomNavItem(Routes.SEARCH, "Search", Icons.Default.Search)
    object Library : BottomNavItem(Routes.LIBRARY, "Library", Icons.Default.VideoLibrary)
    object Profile : BottomNavItem(Routes.PROFILE, "Profile", Icons.Default.Person)
}

