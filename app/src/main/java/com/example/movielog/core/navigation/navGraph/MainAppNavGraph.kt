package com.example.movielog.core.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movielog.core.auth.AuthManager
import com.example.movielog.core.navigation.Routes
import com.example.movielog.core.ui.theme.ThemeViewModel
import com.example.movielog.features.library.domain.repository.LibraryRepository
import com.example.movielog.features.library.presentation.screen.HomeScreen
import com.example.movielog.features.library.presentation.screen.LibraryScreen
import com.example.movielog.features.library.presentation.viewmodel.LibraryViewModel
import com.example.movielog.features.profile.presentation.screen.ProfileScreen
import com.example.movielog.features.search.presentation.screen.SearchScreen
import com.example.movielog.features.search.presentation.viewmodel.SearchViewModel

@Composable
fun MainAppNavGraph(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    libraryRepository: LibraryRepository,
    libraryViewModel: LibraryViewModel,
    themeViewModel: ThemeViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {

        composable(Routes.HOME) {
            HomeScreen(
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                viewModel = searchViewModel,
                libraryRepository = libraryRepository
            )
        }

        composable(Routes.LIBRARY) {
            LibraryScreen(viewModel = libraryViewModel)
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onLogout = {
                    AuthManager.logout()
                },
                themeViewModel = themeViewModel
            )
        }
    }
}