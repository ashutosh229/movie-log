package com.example.movielog.features.library.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielog.core.ui.components.LibraryItem
import com.example.movielog.features.library.presentation.state.LibraryUiState
import com.example.movielog.features.library.presentation.viewmodel.LibraryViewModel

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

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
                val data = (uiState as LibraryUiState.Success).data

                if (data.isEmpty()) {
                    Text("No content added yet.")
                } else {
                    LazyColumn {
                        items(data) { item ->
                            LibraryItem(item)
                        }
                    }
                }
            }
        }
    }
}