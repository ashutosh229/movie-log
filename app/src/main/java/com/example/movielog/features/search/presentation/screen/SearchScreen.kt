package com.example.movielog.features.search.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielog.core.ui.components.library.ProgressDialog
import com.example.movielog.core.ui.components.search.SearchItem
import com.example.movielog.core.ui.components.search.StatusSelectionDialog
import com.example.movielog.features.library.domain.mapper.toUserContent
import com.example.movielog.features.library.domain.model.WatchStatus
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
    var showProgressDialog by remember { mutableStateOf(false) }
    var pendingContent by remember { mutableStateOf<ContentSnapshot?>(null) }
    var pendingStatus by remember { mutableStateOf<WatchStatus?>(null) }

    val scope = rememberCoroutineScope()

    Scaffold { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            // Header
            Text(
                text = "Search",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar (Styled)
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onQueryChange(it) },
                label = { Text("Search movies, anime, series") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.onQueryChange("") // clear text
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Content Area
            when (uiState) {

                is SearchUiState.Idle -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Start typing to search",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is SearchUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SearchUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (uiState as SearchUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is SearchUiState.Success -> {

                    val results = (uiState as SearchUiState.Success).data

                    if (results.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No results found",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(results) { content ->
                                SearchItem(
                                    content = content,
                                    onClick = {
                                        selectedContent = content.toSnapshot()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Status Selection Dialog
    selectedContent?.let { snapshot ->

        StatusSelectionDialog(
            onDismiss = { selectedContent = null },
            onStatusSelected = { status ->

                if (status == WatchStatus.ONGOING) {
                    pendingContent = snapshot
                    pendingStatus = status
                    showProgressDialog = true
                } else {
                    val userContent = snapshot.toUserContent(status)

                    scope.launch {
                        libraryRepository.addOrUpdateContent(userContent)
                        selectedContent = null
                    }
                }
            }
        )
    }

    if (showProgressDialog && pendingContent != null && pendingStatus != null) {

        val snapshot = pendingContent!!

        ProgressDialog(
            type = snapshot.type,
            onDismiss = {
                showProgressDialog = false
                pendingContent = null
                pendingStatus = null
            },
            onSave = { progress ->

                val userContent = snapshot
                    .toUserContent(WatchStatus.ONGOING)
                    .copy(
                        progress = progress,
                        updatedAt = System.currentTimeMillis()
                    )

                scope.launch {
                    libraryRepository.addOrUpdateContent(userContent)
                }

                showProgressDialog = false
                pendingContent = null
                pendingStatus = null
                selectedContent = null
            }
        )
    }
}