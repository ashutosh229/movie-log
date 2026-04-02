package com.example.movielog.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.movielog.features.library.domain.model.WatchStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentActionSheet(
    onDismiss: () -> Unit,
    onUpdateProgress: () -> Unit,
    onChangeStatus: (WatchStatus) -> Unit,
    onDelete: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        ListItem(
            headlineContent = { Text("Update Progress") },
            modifier = androidx.compose.ui.Modifier.clickable {
                onUpdateProgress()
            }
        )

        ListItem(
            headlineContent = { Text("Mark as Completed") },
            modifier = androidx.compose.ui.Modifier.clickable {
                onChangeStatus(WatchStatus.COMPLETED)
            }
        )

        ListItem(
            headlineContent = { Text("Move to To Watch") },
            modifier = androidx.compose.ui.Modifier.clickable {
                onChangeStatus(WatchStatus.TO_WATCH)
            }
        )

        ListItem(
            headlineContent = { Text("Move to Repository") },
            modifier = androidx.compose.ui.Modifier.clickable {
                onChangeStatus(WatchStatus.REPOSITORY)
            }
        )

        ListItem(
            headlineContent = { Text("Delete") },
            modifier = androidx.compose.ui.Modifier.clickable {
                onDelete()
            }
        )
    }
}