package com.example.movielog.features.library.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.core.auth.AuthManager
import com.example.movielog.features.library.domain.repository.LibraryRepository
import com.example.movielog.features.library.presentation.state.LibraryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
    val uiState: StateFlow<LibraryUiState> = _uiState

    init {
        observeAuthAndFetch()
    }

    private fun observeAuthAndFetch() {
        viewModelScope.launch {
            AuthManager.authState.collectLatest { user ->

                if (user == null) {
                    // 🔥 Clear data when logged out
                    _uiState.value = LibraryUiState.Success(emptyList())
                } else {
                    // 🔥 Fetch fresh data for new user
                    fetchLibrary()
                }
            }
        }
    }

    fun fetchLibrary() {
        viewModelScope.launch {
            _uiState.value = LibraryUiState.Loading

            val result = repository.getAllContent()

            _uiState.value = result.fold(
                onSuccess = { LibraryUiState.Success(it) },
                onFailure = { LibraryUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}