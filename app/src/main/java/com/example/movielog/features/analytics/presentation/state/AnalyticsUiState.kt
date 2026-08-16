package com.example.movielog.features.analytics.presentation.state

import com.example.movielog.features.analytics.domain.model.LibraryAnalytics

sealed class AnalyticsUiState {

    data object Loading : AnalyticsUiState()

    data class Success(
        val analytics: LibraryAnalytics
    ) : AnalyticsUiState()

    data class Error(
        val message: String
    ) : AnalyticsUiState()
}