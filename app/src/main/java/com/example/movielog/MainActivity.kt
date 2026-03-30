package com.example.movielog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movielog.core.navigation.AppNavGraph
import com.example.movielog.features.auth.data.remote.FirebaseAuthDataSource
import com.example.movielog.features.auth.data.repository.AuthRepositoryImpl
import com.example.movielog.features.auth.presentation.viewmodel.AuthViewModel
import com.example.movielog.features.library.data.remote.LibraryRemoteDataSource
import com.example.movielog.features.library.data.repository.LibraryRepositoryImpl
import com.example.movielog.features.library.presentation.viewmodel.LibraryViewModel
import com.example.movielog.features.search.data.remote.SearchRemoteDataSource
import com.example.movielog.features.search.data.repository.SearchRepositoryImpl
import com.example.movielog.features.search.presentation.viewmodel.SearchViewModel
import com.example.movielog.ui.theme.MovieLogTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        TODO: Migration of entities from entry point to navigation graph
        val authRepository = AuthRepositoryImpl(
            FirebaseAuthDataSource()
        )
        val authViewModel = AuthViewModel(
            authRepository
        )
        val searchRepository = SearchRepositoryImpl(
            SearchRemoteDataSource()
        )
        val searchViewModel = SearchViewModel(
            searchRepository
        )
        val libraryRepository = LibraryRepositoryImpl(
            LibraryRemoteDataSource()
        )
        val libraryViewModel = LibraryViewModel(
            libraryRepository
        )
        setContent {
            MovieLogTheme {
                AppNavGraph(authViewModel, searchViewModel, libraryRepository, libraryViewModel)
            }
        }
    }
}
