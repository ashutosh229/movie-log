package com.example.movielog.features.analytics.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.core.auth.AuthManager
import com.example.movielog.features.analytics.domain.calculator.AnalyticsCalculator
import com.example.movielog.features.analytics.presentation.state.AnalyticsUiState
import com.example.movielog.features.library.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AnalyticsViewModel(
    private val repository: LibraryRepository
) : ViewModel() {
    val uiState: StateFlow<AnalyticsUiState> =
        AuthManager.authState
            .flatMapLatest { user ->
                if (user == null) {
                    flowOf<AnalyticsUiState>(
                        AnalyticsUiState.Success(
                            AnalyticsCalculator.calculate(emptyList())
                        )
                    )
                } else {
                    repository.observeAllContent()
                        .map<_, AnalyticsUiState> { content ->
                            AnalyticsUiState.Success(
                                AnalyticsCalculator.calculate(content)
                            )
                        }
                        .catch { error ->
                            emit(
                                AnalyticsUiState.Error(
                                    error.message ?: "Unable to load analytics"
                                )
                            )
                        }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AnalyticsUiState.Loading
            )
}