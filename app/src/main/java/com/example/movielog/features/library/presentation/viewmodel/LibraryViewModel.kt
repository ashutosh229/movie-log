package com.example.movielog.features.library.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.features.library.domain.repository.LibraryRepository
import com.example.movielog.features.library.presentation.state.LibraryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
    val uiState: StateFlow<LibraryUiState> = _uiState

    init {
        fetchLibrary()
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