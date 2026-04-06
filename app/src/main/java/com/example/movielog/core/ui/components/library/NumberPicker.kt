package com.example.movielog.core.ui.components.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NumberPicker(
    label: String,
    range: IntRange,
    selected: Int,
    onValueChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.height(120.dp)
        ) {
            items(range.toList()) { value ->

                Text(
                    text = value.toString().padStart(2, '0'),
                    modifier = Modifier
                        .padding(6.dp)
                        .clickable { onValueChange(value) },
                    style = if (value == selected)
                        MaterialTheme.typography.titleMedium
                    else
                        MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}