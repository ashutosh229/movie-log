package com.example.movielog.core.ui.components.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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

//TODO: Add progressType in model
enum class ProgressInputType {
    FULL_SEASON,
    EPISODE_COMPLETED,
    EPISODE_IN_PROGRESS
}

@Composable
fun ProgressDialog(
    type: ContentType,
    onDismiss: () -> Unit,
    onSave: (Progress) -> Unit
) {

    var season by remember { mutableStateOf("") }
    var episode by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf("") }

    var inputType by remember { mutableStateOf(ProgressInputType.FULL_SEASON) }

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

                // 🔥 Progress Type Selector (Only for Series/Anime)
                if (!isMovie) {

                    Text(
                        text = "Progress Type",
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProgressInputType.values().forEach { option ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RadioButton(
                                selected = inputType == option,
                                onClick = { inputType = option }
                            )

                            Text(
                                text = when (option) {
                                    ProgressInputType.FULL_SEASON -> "Seasonal Progress"
                                    ProgressInputType.EPISODE_COMPLETED -> "Episodic Progress"
                                    ProgressInputType.EPISODE_IN_PROGRESS -> "Timed Progress"
                                },
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // 🔹 SEASON
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
                }

                // 🔹 EPISODE (only if not FULL_SEASON)
                if (!isMovie && inputType != ProgressInputType.FULL_SEASON) {
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

                // 🔹 TIMESTAMP (only for movies OR in-progress)
                if (isMovie || inputType == ProgressInputType.EPISODE_IN_PROGRESS) {
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
                }

                Text(
                    text = when {
                        isMovie -> "Enter playback position in seconds"
                        inputType == ProgressInputType.FULL_SEASON -> "Marks entire season as completed"
                        inputType == ProgressInputType.EPISODE_COMPLETED -> "Marks completion till selected episode"
                        else -> "Track current playback within episode"
                    },
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
                        when (inputType) {

                            ProgressInputType.FULL_SEASON -> {
                                Progress.EpisodeProgress(
                                    season = season.toIntOrNull() ?: 1,
                                    episode = Int.MAX_VALUE, // represents full season
                                    timestamp = 0L
                                )
                            }

                            ProgressInputType.EPISODE_COMPLETED -> {
                                Progress.EpisodeProgress(
                                    season = season.toIntOrNull() ?: 1,
                                    episode = episode.toIntOrNull() ?: 1,
                                    timestamp = 0L
                                )
                            }

                            ProgressInputType.EPISODE_IN_PROGRESS -> {
                                Progress.EpisodeProgress(
                                    season = season.toIntOrNull() ?: 1,
                                    episode = episode.toIntOrNull() ?: 1,
                                    timestamp = timestamp.toLongOrNull() ?: 0L
                                )
                            }
                        }
                    }

                    onSave(progress)

                },
                enabled =
                if (isMovie) {
                    timestamp.isNotBlank()
                } else {
                    when (inputType) {

                        ProgressInputType.FULL_SEASON ->
                            season.isNotBlank()

                        ProgressInputType.EPISODE_COMPLETED ->
                            season.isNotBlank() && episode.isNotBlank()

                        ProgressInputType.EPISODE_IN_PROGRESS ->
                            season.isNotBlank() && episode.isNotBlank() && timestamp.isNotBlank()
                    }
                }
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