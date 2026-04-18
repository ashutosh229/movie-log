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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.movielog.features.library.domain.model.UserContent
import com.example.movielog.features.library.domain.model.WatchStatus
import com.example.movielog.features.library.domain.model.displayName
import com.example.movielog.features.library.presentation.state.LibraryUiState
import com.example.movielog.features.library.presentation.viewmodel.LibraryViewModel

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedContent by remember { mutableStateOf<UserContent?>(null) }
    var selectedTab by remember { mutableStateOf(WatchStatus.ONGOING) }
    var showActionSheet by remember { mutableStateOf(false) }
    var showProgressDialog by remember { mutableStateOf(false) }

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
            when (uiState) {
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
                        description = (uiState as LibraryUiState.Error).message,
                        isError = true,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                is LibraryUiState.Success -> {
                    val allData = (uiState as LibraryUiState.Success).data
                    val filteredData = allData.filter { it.status == selectedTab }

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
                        item {
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
                                }
                            }
                        }

                        item {
                            ScrollableTabRow(
                                selectedTabIndex = tabs.indexOf(selectedTab),
                                edgePadding = 0.dp,
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                                divider = {}
                            ) {
                                tabs.forEach { status ->
                                    Tab(
                                        selected = selectedTab == status,
                                        onClick = { selectedTab = status },
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

                        if (filteredData.isEmpty()) {
                            item {
                                LibraryStateCard(
                                    title = "Nothing in ${selectedTab.displayName()} yet",
                                    description = "Add something from Search and it will show up here with status and progress details."
                                )
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

    if (showActionSheet && selectedContent != null) {
        ContentActionSheet(
            onDismiss = {
                showActionSheet = false
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
