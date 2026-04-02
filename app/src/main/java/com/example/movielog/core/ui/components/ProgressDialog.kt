package com.example.movielog.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielog.features.library.domain.model.Progress
import com.example.movielog.features.search.domain.model.ContentType

@Composable
fun ProgressDialog(
    type: ContentType,
    onDismiss: () -> Unit,
    onSave: (Progress) -> Unit
) {

    var season by remember { mutableStateOf("") }
    var episode by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Progress") },

        text = {
            Column {

                if (type != ContentType.MOVIE) {
                    TextField(
                        value = season,
                        onValueChange = { season = it },
                        label = { Text("Season") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = episode,
                        onValueChange = { episode = it },
                        label = { Text("Episode") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                TextField(
                    value = timestamp,
                    onValueChange = { timestamp = it },
                    label = { Text("Timestamp (seconds)") }
                )
            }
        },

        confirmButton = {
            Button(onClick = {

                val progress = if (type == ContentType.MOVIE) {
                    Progress.MovieProgress(
                        timestamp = timestamp.toLongOrNull() ?: 0L
                    )
                } else {
                    Progress.EpisodeProgress(
                        season = season.toIntOrNull() ?: 1,
                        episode = episode.toIntOrNull() ?: 1,
                        timestamp = timestamp.toLongOrNull() ?: 0L
                    )
                }

                onSave(progress)

            }) {
                Text("Save")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}