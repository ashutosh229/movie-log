package com.example.movielog.features.detail.presentation.state

import com.example.movielog.features.detail.domain.model.ContentDetail

sealed class DetailUiState {

    object Loading : DetailUiState()

    data class Success(
        val detail: ContentDetail
    ) : DetailUiState()

    data class Error(
        val message: String
    ) : DetailUiState()
}