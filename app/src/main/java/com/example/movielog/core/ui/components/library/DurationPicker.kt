package com.example.movielog.core.ui.components.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun DurationPicker(
    initialHours: Int = 0,
    initialMinutes: Int = 0,
    initialSeconds: Int = 0,
    onTimeSelected: (hours: Int, minutes: Int, seconds: Int) -> Unit
) {
    var hours by remember { mutableStateOf(initialHours) }
    var minutes by remember { mutableStateOf(initialMinutes) }
    var seconds by remember { mutableStateOf(initialSeconds) }

    LaunchedEffect(hours, minutes, seconds) {
        onTimeSelected(hours, minutes, seconds)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NumberPicker(
            label = "HH",
            range = 0..23,
            selected = hours,
            onValueChange = { hours = it }
        )

        NumberPicker(
            label = "MM",
            range = 0..59,
            selected = minutes,
            onValueChange = { minutes = it }
        )

        NumberPicker(
            label = "SS",
            range = 0..59,
            selected = seconds,
            onValueChange = { seconds = it }
        )
    }
}

