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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielog.core.ui.components.SearchItem
import com.example.movielog.core.ui.components.StatusSelectionDialog
import com.example.movielog.features.library.domain.mapper.toUserContent
import com.example.movielog.features.library.domain.repository.LibraryRepository
import com.example.movielog.features.search.domain.mapper.toSnapshot
import com.example.movielog.features.search.domain.model.ContentSnapshot
import com.example.movielog.features.search.presentation.state.SearchUiState
import com.example.movielog.features.search.presentation.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    libraryRepository: LibraryRepository
) {

    val query by viewModel.query.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var selectedContent by remember { mutableStateOf<ContentSnapshot?>(null) }
    val scope = rememberCoroutineScope()

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
                            // 🔥 Instead of navigating → open dialog
                            selectedContent = content.toSnapshot()
                        }
                    }
                }
            }
        }
    }

    // 🔥 STATUS SELECTION DIALOG
    selectedContent?.let { snapshot ->

        StatusSelectionDialog(
            onDismiss = { selectedContent = null },
            onStatusSelected = { status ->

                val userContent = snapshot.toUserContent(status)

                scope.launch {
                    libraryRepository.addOrUpdateContent(userContent)
                    selectedContent = null
                }
            }
        )
    }
}