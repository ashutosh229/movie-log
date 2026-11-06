package com.example.movielog.core.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movielog.core.auth.AuthManager
import com.example.movielog.core.navigation.Routes
import com.example.movielog.core.ui.theme.ThemeViewModel
import com.example.movielog.features.analytics.presentation.screen.AnalyticsScreen
import com.example.movielog.features.analytics.presentation.viewmodel.AnalyticsViewModel
import com.example.movielog.features.detail.presentation.screen.DetailScreen
import com.example.movielog.features.detail.presentation.viewmodel.DetailViewModel
import com.example.movielog.features.export.presentation.viewmodel.ExportViewModel
import com.example.movielog.features.library.domain.repository.LibraryRepository
import com.example.movielog.features.library.presentation.screen.HomeScreen
import com.example.movielog.features.library.presentation.screen.LibraryScreen
import com.example.movielog.features.library.presentation.viewmodel.LibraryViewModel
import com.example.movielog.features.profile.presentation.screen.ProfileScreen
import com.example.movielog.features.profile.presentation.viewmodel.ProfileViewModel
import com.example.movielog.features.search.domain.model.ContentType
import com.example.movielog.features.search.presentation.screen.SearchScreen
import com.example.movielog.features.search.presentation.viewmodel.SearchViewModel

@Composable
fun MainAppNavGraph(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    libraryRepository: LibraryRepository,
    libraryViewModel: LibraryViewModel,
    profileViewModel: ProfileViewModel,
    analyticsViewModel: AnalyticsViewModel,
    detailViewModel: DetailViewModel,
    exportViewModel: ExportViewModel,
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
                libraryRepository = libraryRepository,
                onContentClick = { type, id ->
                    navController.navigate(Routes.detail(type.name, id))
                }
            )
        }

        composable(Routes.LIBRARY) {
            LibraryScreen(
                viewModel = libraryViewModel,
                onViewDetails = { content ->
                    navController.navigate(Routes.detail(content.type.name, content.id))
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onLogout = {
                    AuthManager.logout()
                },
                profileViewModel = profileViewModel,
                themeViewModel = themeViewModel,
                onAnalyticsClick = {
                    navController.navigate(Routes.ANALYTICS)
                },
                exportViewModel = exportViewModel,
            )
        }

        composable(Routes.ANALYTICS) {
            AnalyticsScreen(
                viewModel = analyticsViewModel
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument(Routes.ARG_CONTENT_TYPE) { type = NavType.StringType },
                navArgument(Routes.ARG_CONTENT_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val contentId = backStackEntry.arguments
                ?.getString(Routes.ARG_CONTENT_ID)
            val contentType = backStackEntry.arguments
                ?.getString(Routes.ARG_CONTENT_TYPE)
                ?.let { runCatching { ContentType.valueOf(it) }.getOrNull() }

            if (contentId != null && contentType != null) {
                DetailScreen(
                    viewModel = detailViewModel,
                    libraryRepository = libraryRepository,
                    contentId = contentId,
                    contentType = contentType,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}