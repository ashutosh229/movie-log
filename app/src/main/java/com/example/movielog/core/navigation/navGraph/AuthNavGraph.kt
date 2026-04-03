package com.example.movielog.core.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movielog.core.navigation.Routes
import com.example.movielog.features.auth.presentation.screen.LoginScreen
import com.example.movielog.features.auth.presentation.screen.SignupScreen
import com.example.movielog.features.auth.presentation.viewmodel.AuthViewModel

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onAuthSuccess: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onSwitchToSignup = {
                    navController.navigate(Routes.SIGNUP)
                },
                onLoginSuccess = {
                    onAuthSuccess()
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignupScreen(
                viewModel = authViewModel,
                onSwitchToLogin = {
                    navController.navigate(Routes.LOGIN)
                },
                onSignupSuccess = {
                    onAuthSuccess()
                }
            )
        }
    }
}