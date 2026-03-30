package com.example.movielog.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost;
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movielog.features.auth.presentation.screen.LoginScreen
import com.example.movielog.features.auth.presentation.screen.SignupScreen
import com.example.movielog.features.auth.presentation.viewmodel.AuthViewModel;
import com.example.movielog.features.library.presentation.screen.HomeScreen

@Composable
fun AppNavGraph(viewModel: AuthViewModel) {
    val navController = rememberNavController()
    val startDestination = if (viewModel.getCurrentUser() != null) {
        Routes.HOME
    } else {
        Routes.LOGIN
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = viewModel,
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
                viewModel = viewModel,
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
                viewModel.logout();navController.navigate(
                Routes.LOGIN
            ) { popUpTo(Routes.HOME) { inclusive = true } }
            })
        }
    }
}