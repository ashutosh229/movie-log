package com.example.movielog.core.ui.components.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

    val isMovie = type == ContentType.MOVIE

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                text = "Update Progress",
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                if (!isMovie) {

                    OutlinedTextField(
                        value = season,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                season = input
                            }
                        },
                        label = { Text("Season") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = episode,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                episode = input
                            }
                        },
                        label = { Text("Episode") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = timestamp,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            timestamp = input
                        }
                    },
                    label = { Text("Timestamp (seconds)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isMovie)
                        "Enter playback position in seconds"
                    else
                        "Enter current episode playback position",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },

        confirmButton = {
            Button(
                onClick = {

                    val progress = if (isMovie) {
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

                },
                enabled = timestamp.isNotBlank() &&
                        (isMovie || (season.isNotBlank() && episode.isNotBlank()))
            ) {
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