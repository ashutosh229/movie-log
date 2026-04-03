package com.example.movielog.core.ui.components.common

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.movielog.core.common.BottomNavItem

@Composable
fun BottomNavBar(navController: NavHostController) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Library,
        BottomNavItem.Profile
    )

    NavigationBar {

        val currentRoute =
            navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}