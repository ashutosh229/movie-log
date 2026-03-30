package com.example.movielog.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movielog.features.auth.presentation.screen.LoginScreen
import com.example.movielog.features.auth.presentation.screen.SignupScreen
import com.example.movielog.features.auth.presentation.viewmodel.AuthViewModel
import com.example.movielog.features.library.domain.repository.LibraryRepository
import com.example.movielog.features.library.presentation.screen.HomeScreen
import com.example.movielog.features.library.presentation.screen.LibraryScreen
import com.example.movielog.features.library.presentation.viewmodel.LibraryViewModel
import com.example.movielog.features.search.presentation.screen.SearchScreen
import com.example.movielog.features.search.presentation.viewmodel.SearchViewModel

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel,
    searchViewModel: SearchViewModel,
    libraryRepository: LibraryRepository,
    libraryViewModel: LibraryViewModel
) {
    val navController = rememberNavController()
    val startDestination = if (authViewModel.getCurrentUser() != null) {
        Routes.HOME
    } else {
        Routes.LOGIN
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onSwitchToSignup = { navController.navigate(Routes.SIGNUP) },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                })
        }
        composable(Routes.SIGNUP) {
            SignupScreen(
                viewModel = authViewModel,
                onSwitchToLogin = { navController.navigate(Routes.SIGNUP) },
                onSignupSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SIGNUP) {
                            inclusive = true
                        }
                    }
                })
        }
        composable(Routes.HOME) {
            HomeScreen(onLogout = {
                authViewModel.logout();
                navController.navigate(
                    Routes.LOGIN
                ) { popUpTo(Routes.HOME) { inclusive = true } }
            }, onNavigateToSearch = {
                navController.navigate(Routes.SEARCH)
            }, onNavigateToShow = {
                navController.navigate(Routes.LIBRARY)
            })
        }
        composable(Routes.SEARCH) {
            SearchScreen(
                viewModel = searchViewModel, libraryRepository = libraryRepository
            )
        }
        composable(Routes.LIBRARY) {
            LibraryScreen(viewModel = libraryViewModel)
        }
    }
}