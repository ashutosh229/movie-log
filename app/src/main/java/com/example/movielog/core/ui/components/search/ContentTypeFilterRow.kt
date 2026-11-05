package com.example.movielog.core.ui.components.search

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movielog.features.search.domain.model.ContentType

@Composable
fun ContentTypeFilterRow(
    selectedType: ContentType?,
    onTypeSelected: (ContentType?) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        null to "All",
        ContentType.MOVIE to "Movies",
        ContentType.ANIME to "Anime",
        ContentType.SERIES to "Series"
    )

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { (type, label) ->
            FilterChip(
                selected = selectedType == type,
                onClick = {
                    onTypeSelected(type)
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}
