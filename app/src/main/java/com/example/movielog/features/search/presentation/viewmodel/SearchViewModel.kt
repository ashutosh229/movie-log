package com.example.movielog.features.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.features.search.domain.repository.SearchRepository
import com.example.movielog.features.search.presentation.state.SearchUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState

    init {
        observeSearch()
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    private fun observeSearch() {
        viewModelScope.launch {
            _query
//                TODO: Migrate this hyperparameter into config
                .debounce(400) // 🔥 KEY PART
                .map { it.trim() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        flowOf<SearchUiState>(SearchUiState.Idle)
                    } else {
                        flow<SearchUiState> {

                            emit(SearchUiState.Loading)

                            val result = repository.searchContent(query)

                            emit(
                                result.fold(
                                    onSuccess = { SearchUiState.Success(it) },
                                    onFailure = {
                                        SearchUiState.Error(
                                            it.message ?: "Unknown error"
                                        )
                                    }
                                )
                            )
                        }
                    }
                }
                .collect {
                    _uiState.value = it
                }
        }
    }
}