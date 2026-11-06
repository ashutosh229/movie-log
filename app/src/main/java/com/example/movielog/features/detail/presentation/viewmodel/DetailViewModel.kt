package com.example.movielog.features.detail.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.features.detail.domain.repository.DetailRepository
import com.example.movielog.features.detail.presentation.state.DetailUiState
import com.example.movielog.features.search.domain.model.ContentType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: DetailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun load(contentId: String, contentType: ContentType) {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            _uiState.value = repository.getContentDetail(contentId, contentType).fold(
                onSuccess = { DetailUiState.Success(it) },
                onFailure = { DetailUiState.Error(it.message ?: "Unable to load details") }
            )
        }
    }
}