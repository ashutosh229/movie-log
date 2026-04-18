package com.example.movielog.features.search.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f),
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Card(
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Find something worth watching",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        text = "Search across movies, series, and anime, then drop favorites straight into your library.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = query,
                        onValueChange = { viewModel.onQueryChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(22.dp),
                        placeholder = {
                            Text("Search movies, anime, series")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onQueryChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear search"
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (uiState) {
                is SearchUiState.Idle -> {
                    SearchStateCard(
                        title = "Start with a title, franchise, or genre vibe",
                        description = "Try a movie name, a show you paused halfway, or the anime you meant to watch last month."
                    )
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
                    SearchStateCard(
                        title = "Search hit a snag",
                        description = (uiState as SearchUiState.Error).message,
                        isError = true
                    )
                }

                is SearchUiState.Success -> {
                    val results = (uiState as SearchUiState.Success).data

                    if (results.isEmpty()) {
                        SearchStateCard(
                            title = "No matches yet",
                            description = "Try a broader title or another keyword. The best results usually come from full names."
                        )
                    } else {
                        Text(
                            text = "${results.size} result${if (results.size == 1) "" else "s"}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
                        )

                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 120.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
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

@Composable
private fun SearchStateCard(
    title: String,
    description: String,
    isError: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isError) {
                MaterialTheme.colorScheme.error.copy(alpha = 0.14f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                textAlign = TextAlign.Center
            )
        }
    }
}
