package com.example.movielog.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.movielog.core.auth.AuthManager
import com.example.movielog.core.navigation.navGraph.AuthNavGraph
import com.example.movielog.features.auth.presentation.viewmodel.AuthViewModel
import com.example.movielog.features.library.domain.repository.LibraryRepository
import com.example.movielog.features.library.presentation.viewmodel.LibraryViewModel
import com.example.movielog.features.search.presentation.viewmodel.SearchViewModel

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel,
    searchViewModel: SearchViewModel,
    libraryRepository: LibraryRepository,
    libraryViewModel: LibraryViewModel
) {
    val authState by AuthManager.authState.collectAsState()

    if (authState == null) {
//        TODO: Migrate this navcontroller onto the nav graph
        val navController = rememberNavController()
        AuthNavGraph(
            navController = navController,
            authViewModel = authViewModel,
            onAuthSuccess = {
            }
        )
    } else {
        MainAppContainer(
            searchViewModel = searchViewModel,
            libraryRepository = libraryRepository,
            libraryViewModel = libraryViewModel
        )
    }
}