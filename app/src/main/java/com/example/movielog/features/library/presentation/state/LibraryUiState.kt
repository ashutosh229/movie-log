package com.example.movielog.features.library.presentation.state

import com.example.movielog.features.library.domain.model.UserContent

sealed class LibraryUiState {

    object Loading : LibraryUiState()

    data class Success(
        val data: List<UserContent>
    ) : LibraryUiState()

    data class Error(
        val message: String
    ) : LibraryUiState()
}