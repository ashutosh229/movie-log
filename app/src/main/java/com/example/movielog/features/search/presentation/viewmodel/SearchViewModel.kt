package com.example.movielog.features.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.features.search.domain.repository.SearchRepository
import com.example.movielog.features.search.presentation.state.SearchUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel() {

    // 🔍 Search query input
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    // 📊 UI state
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState

    init {
        observeSearch()
    }

    // 🧠 Called when user types
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    // 🔥 CORE LOGIC (Debouncing)
    private fun observeSearch() {
        viewModelScope.launch {
            _query
                .debounce(500) // ⏳ wait for user to stop typing
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collectLatest { query ->

                    _uiState.value = SearchUiState.Loading

                    val result = repository.searchContent(query)

                    _uiState.value = result.fold(
                        onSuccess = { SearchUiState.Success(it) },
                        onFailure = {
                            SearchUiState.Error(it.message ?: "Something went wrong")
                        }
                    )
                }
        }
    }
}