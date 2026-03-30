package com.example.movielog.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.movielog.features.library.domain.model.WatchStatus

@Composable
fun StatusSelectionDialog(
    onDismiss: () -> Unit,
    onStatusSelected: (WatchStatus) -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Status") },
        text = {
            Column {
                WatchStatus.values().forEach { status ->
                    TextButton(
                        onClick = { onStatusSelected(status) }
                    ) {
                        Text(status.name)
                    }
                }
            }
        },
        confirmButton = {}
    )
}