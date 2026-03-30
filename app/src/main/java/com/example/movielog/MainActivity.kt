package com.example.movielog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movielog.core.navigation.AppNavGraph
import com.example.movielog.features.auth.data.remote.FirebaseAuthDataSource
import com.example.movielog.features.auth.data.repository.AuthRepositoryImpl
import com.example.movielog.features.auth.presentation.viewmodel.AuthViewModel
import com.example.movielog.features.search.data.remote.SearchRemoteDataSource
import com.example.movielog.features.search.data.repository.SearchRepositoryImpl
import com.example.movielog.features.search.presentation.viewmodel.SearchViewModel
import com.example.movielog.ui.theme.MovieLogTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel = AuthViewModel(
            AuthRepositoryImpl(
                FirebaseAuthDataSource()
            )
        )
        val searchViewModel = SearchViewModel(
            SearchRepositoryImpl(
                SearchRemoteDataSource()
            )
        )
        setContent {
            MovieLogTheme {
                AppNavGraph(authViewModel, searchViewModel)
            }
        }
    }
}
