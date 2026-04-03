package com.example.movielog.core.ui.components.library

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {

            // Header (Optional but improves UX)
            Text(
                text = "Actions",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Divider()

            AppActionItem(
                icon = Icons.Default.Edit,
                text = "Update Progress",
                onClick = onUpdateProgress
            )

            AppActionItem(
                icon = Icons.Default.Done,
                text = "Mark as Completed",
                onClick = { onChangeStatus(WatchStatus.COMPLETED) }
            )

            AppActionItem(
                icon = Icons.Default.Bookmark,
                text = "Move to To Watch",
                onClick = { onChangeStatus(WatchStatus.TO_WATCH) }
            )

            AppActionItem(
                icon = Icons.Default.List,
                text = "Move to Repository",
                onClick = { onChangeStatus(WatchStatus.REPOSITORY) }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            AppActionItem(
                icon = Icons.Default.Delete,
                text = "Delete",
                isDestructive = true,
                onClick = onDelete
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}