package com.example.movielog.features.search.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielog.core.ui.components.SearchItem
import com.example.movielog.features.search.domain.mapper.toSnapshot
import com.example.movielog.features.search.presentation.state.SearchUiState
import com.example.movielog.features.search.presentation.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onContentClick: (com.example.movielog.features.search.domain.model.ContentSnapshot) -> Unit
) {

    val query by viewModel.query.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔍 SEARCH BAR
        TextField(
            value = query,
            onValueChange = { viewModel.onQueryChange(it) },
            label = { Text("Search movies, anime, series") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 📊 UI STATE HANDLING
        when (uiState) {

            is SearchUiState.Idle -> {
                Text("Start typing to search...")
            }

            is SearchUiState.Loading -> {
                CircularProgressIndicator()
            }

            is SearchUiState.Error -> {
                Text((uiState as SearchUiState.Error).message)
            }

            is SearchUiState.Success -> {
                val results = (uiState as SearchUiState.Success).data

                LazyColumn {
                    items(results) { content ->
                        SearchItem(content = content) {
                            onContentClick(content.toSnapshot())
                        }
                    }
                }
            }
        }
    }
}