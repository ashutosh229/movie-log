package com.example.movielog.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.movielog.core.navigation.navGraph.MainAppNavGraph
import com.example.movielog.core.ui.components.common.BottomNavBar
import com.example.movielog.core.ui.theme.ThemeViewModel
import com.example.movielog.features.library.domain.repository.LibraryRepository
import com.example.movielog.features.library.presentation.viewmodel.LibraryViewModel
import com.example.movielog.features.search.presentation.viewmodel.SearchViewModel

@Composable
fun MainAppContainer(
    searchViewModel: SearchViewModel,
    libraryRepository: LibraryRepository,
    libraryViewModel: LibraryViewModel,
    themeViewModel: ThemeViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { innerPadding ->

        MainAppNavGraph(
            navController = navController,
            searchViewModel = searchViewModel,
            libraryRepository = libraryRepository,
            libraryViewModel = libraryViewModel,
            themeViewModel = themeViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}