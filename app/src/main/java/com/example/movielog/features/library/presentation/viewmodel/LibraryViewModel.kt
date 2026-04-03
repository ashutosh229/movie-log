package com.example.movielog.features.library.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.core.auth.AuthManager
import com.example.movielog.features.library.domain.repository.LibraryRepository
import com.example.movielog.features.library.presentation.state.LibraryUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    val uiState: StateFlow<LibraryUiState> =
        AuthManager.authState
            .flatMapLatest { user ->

                if (user == null) {
                    flowOf(LibraryUiState.Success(emptyList()))
                } else {
                    repository.observeAllContent()
                        .map { LibraryUiState.Success(it) as LibraryUiState }
                }
            }
            .catch { emit(LibraryUiState.Error(it.message ?: "Unknown error")) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                LibraryUiState.Loading
            )

    // Only needed for write operations
    fun updateContent(content: com.example.movielog.features.library.domain.model.UserContent) {
        viewModelScope.launch {
            repository.addOrUpdateContent(content)
        }
    }

    fun deleteContent(contentId: String) {
        viewModelScope.launch {
            repository.deleteContent(contentId)
        }
    }
}