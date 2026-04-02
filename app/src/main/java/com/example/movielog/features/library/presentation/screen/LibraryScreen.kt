package com.example.movielog.features.library.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielog.core.ui.components.ContentActionSheet
import com.example.movielog.core.ui.components.LibraryItem
import com.example.movielog.core.ui.components.ProgressDialog
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

    TabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
        tabs.forEach { status ->
            Tab(
                selected = selectedTab == status,
                onClick = { selectedTab = status },
                text = {
                    Text(
                        text = status.displayName()
                    )
                }
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "My Library",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {

            is LibraryUiState.Loading -> {
                CircularProgressIndicator()
            }

            is LibraryUiState.Error -> {
                Text((uiState as LibraryUiState.Error).message)
            }

            is LibraryUiState.Success -> {
                val allData = (uiState as LibraryUiState.Success).data
                val filteredData = allData.filter {
                    it.status == selectedTab
                }
                if (filteredData.isEmpty()) {
                    Column {
                        Text(
                            text = "No content in ${selectedTab.displayName()}",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Start adding something",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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