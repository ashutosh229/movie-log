package com.example.movielog.features.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielog.core.config.AppConfig
import com.example.movielog.features.search.domain.model.ContentType
import com.example.movielog.features.search.domain.repository.SearchRepository
import com.example.movielog.features.search.presentation.state.SearchUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class
)
class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _selectedContentType =
        MutableStateFlow<ContentType?>(null)

    val selectedContentType: StateFlow<ContentType?> =
        _selectedContentType

    private val _uiState =
        MutableStateFlow<SearchUiState>(SearchUiState.Idle)

    val uiState: StateFlow<SearchUiState> = _uiState

    init {
        observeSearch()
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun onContentTypeChange(contentType: ContentType?) {
        _selectedContentType.value = contentType
    }

    private fun observeSearch() {
        viewModelScope.launch {
            combine(
                _query,
                _selectedContentType
            ) { query, contentType ->
                query.trim() to contentType
            }
                .debounce(AppConfig.SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .flatMapLatest { (query, contentType) ->

                    if (query.isBlank()) {
                        flowOf(SearchUiState.Idle)
                    } else {
                        flow<SearchUiState> {
                            emit(SearchUiState.Loading)

                            try {
                                val result = repository.searchContent(
                                    query = query,
                                    contentType = contentType
                                )

                                emit(
                                    result.fold(
                                        onSuccess = {
                                            SearchUiState.Success(it)
                                        },
                                        onFailure = {
                                            SearchUiState.Error(
                                                it.message ?: "Unknown error"
                                            )
                                        }
                                    )
                                )
                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: Exception) {
                                emit(
                                    SearchUiState.Error(
                                        e.message ?: "Network error"
                                    )
                                )
                            }
                        }
                    }
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }
}