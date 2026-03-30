package com.example.movielog.features.search.presentation.state

import com.example.movielog.features.search.domain.model.Content

sealed class SearchUiState {

    object Idle : SearchUiState()

    object Loading : SearchUiState()

    data class Success(
        val data: List<Content>
    ) : SearchUiState()

    data class Error(
        val message: String
    ) : SearchUiState()
}