package com.example.movielog.features.library.presentation.screen

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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movielog.core.ui.components.library.ContentActionSheet
import com.example.movielog.core.ui.components.library.LibraryItem
import com.example.movielog.core.ui.components.library.ProgressDialog
import com.example.movielog.core.ui.components.search.ContentTypeFilterRow
import com.example.movielog.features.library.domain.model.UserContent
import com.example.movielog.features.library.domain.model.WatchStatus
import com.example.movielog.features.library.domain.model.displayName
import com.example.movielog.features.library.presentation.state.LibraryUiState
import com.example.movielog.features.library.presentation.viewmodel.LibraryViewModel
import com.example.movielog.features.search.domain.model.ContentType

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onViewDetails: (UserContent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedContent by remember {
        mutableStateOf<UserContent?>(null)
    }

    var selectedTab by remember {
        mutableStateOf(WatchStatus.ONGOING)
    }

    var showActionSheet by remember {
        mutableStateOf(false)
    }

    var showProgressDialog by remember {
        mutableStateOf(false)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedContentType by remember {
        mutableStateOf<ContentType?>(null)
    }

    val tabs = listOf(
        WatchStatus.ONGOING,
        WatchStatus.TO_WATCH,
        WatchStatus.COMPLETED,
        WatchStatus.REPOSITORY
    )

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
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
                .padding(top = 24.dp)
        ) {
            when (val state = uiState) {

                is LibraryUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LibraryUiState.Error -> {
                    LibraryStateCard(
                        title = "Library unavailable",
                        description = state.message,
                        isError = true,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                is LibraryUiState.Success -> {
                    val allData = state.data

                    // Apply content type, title search, and status filters.
                    val filteredData = allData.filter { content ->

                        val matchesType =
                            selectedContentType == null ||
                                    content.type == selectedContentType

                        val matchesSearch =
                            searchQuery.isBlank() ||
                                    content.title.contains(
                                        searchQuery.trim(),
                                        ignoreCase = true
                                    )

                        // Preserve the existing behavior:
                        // Searching searches across every watch status.
                        // Without a search query, the selected tab is respected.
                        val matchesStatus =
                            searchQuery.isNotBlank() ||
                                    content.status == selectedTab

                        matchesType && matchesSearch && matchesStatus
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 0.dp,
                            end = 20.dp,
                            bottom = 120.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Header and search field.
                        item {
                            Card(
                                shape = RoundedCornerShape(30.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 8.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    MaterialTheme.colorScheme.secondaryContainer,
                                                    MaterialTheme.colorScheme.surface
                                                )
                                            )
                                        )
                                        .padding(22.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Your watch universe",
                                        style = MaterialTheme.typography.displayMedium
                                    )

                                    Text(
                                        text = "Keep everything sorted by status and update progress whenever you stop mid-episode or mid-scene.",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Text(
                                        text = "${allData.size} saved title${if (allData.size == 1) "" else "s"}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    OutlinedTextField(
                                        value = searchQuery,
                                        onValueChange = {
                                            searchQuery = it
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        shape = RoundedCornerShape(20.dp),
                                        placeholder = {
                                            Text("Search your library")
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Search library"
                                            )
                                        },
                                        trailingIcon = {
                                            if (searchQuery.isNotEmpty()) {
                                                IconButton(
                                                    onClick = {
                                                        searchQuery = ""
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Clear search"
                                                    )
                                                }
                                            }
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor =
                                            MaterialTheme.colorScheme.surface,
                                            unfocusedContainerColor =
                                            MaterialTheme.colorScheme.surface,
                                            focusedBorderColor =
                                            MaterialTheme.colorScheme.primary,
                                            unfocusedBorderColor =
                                            MaterialTheme.colorScheme.outlineVariant
                                        )
                                    )
                                }
                            }
                        }

                        // Content type filter.
                        item {
                            ContentTypeFilterRow(
                                selectedType = selectedContentType,
                                onTypeSelected = {
                                    selectedContentType = it
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Search hint.
                        if (searchQuery.isNotBlank()) {
                            item {
                                Text(
                                    text = "Showing results from your entire library",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }

                        // Watch status tabs.
                        item {
                            ScrollableTabRow(
                                selectedTabIndex = tabs.indexOf(selectedTab),
                                edgePadding = 0.dp,
                                containerColor = MaterialTheme.colorScheme.surface.copy(
                                    alpha = 0.92f
                                ),
                                divider = {}
                            ) {
                                tabs.forEach { status ->
                                    Tab(
                                        selected = selectedTab == status,
                                        onClick = {
                                            selectedTab = status
                                        },
                                        text = {
                                            Text(
                                                text = status.displayName(),
                                                fontWeight = if (selectedTab == status) {
                                                    FontWeight.SemiBold
                                                } else {
                                                    FontWeight.Medium
                                                }
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        // Empty state or filtered library items.
                        if (filteredData.isEmpty()) {
                            item {
                                if (searchQuery.isBlank()) {
                                    LibraryStateCard(
                                        title = if (selectedContentType == null) {
                                            "Nothing in ${selectedTab.displayName()} yet"
                                        } else {
                                            "No matching titles"
                                        },
                                        description = if (selectedContentType == null) {
                                            "Add something from Search and it will show up here with status and progress details."
                                        } else {
                                            "There are no ${selectedContentType?.name?.lowercase() ?: "all"} titles in ${selectedTab.displayName()}."
                                        }
                                    )
                                } else {
                                    LibraryStateCard(
                                        title = "No matching titles",
                                        description = "No title in your library matches \"$searchQuery\" with the selected filters."
                                    )
                                }
                            }
                        } else {
                            items(filteredData) { item ->
                                LibraryItem(
                                    content = item,
                                    onClick = {
                                        selectedContent = item
                                        showActionSheet = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Update progress dialog.
    if (showProgressDialog && selectedContent != null) {
        val content = selectedContent!!

        ProgressDialog(
            type = content.type,
            onDismiss = {
                showProgressDialog = false
            },
            onSave = { progress ->
                val updatedContent = content.copy(
                    progress = progress,
                    status = WatchStatus.ONGOING,
                    updatedAt = System.currentTimeMillis()
                )

                viewModel.updateContent(updatedContent)

                showProgressDialog = false
                selectedContent = null
            }
        )
    }

    // Content actions.
    if (showActionSheet && selectedContent != null) {
        ContentActionSheet(
            onDismiss = {
                showActionSheet = false
            },
            onViewDetails = {
                val content = selectedContent!!
                showActionSheet = false
                selectedContent = null
                onViewDetails(content)
            },
            onUpdateProgress = {
                showActionSheet = false
                showProgressDialog = true
            },
            onChangeStatus = { newStatus ->
                val updated = selectedContent!!.copy(
                    status = newStatus,
                    updatedAt = System.currentTimeMillis()
                )

                viewModel.updateContent(updated)

                showActionSheet = false
                selectedContent = null
            },
            onDelete = {
                viewModel.deleteContent(selectedContent!!.id)

                showActionSheet = false
                selectedContent = null
            }
        )
    }
}

@Composable
private fun LibraryStateCard(
    title: String,
    description: String,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                textAlign = TextAlign.Center,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}