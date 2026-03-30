package com.example.movielog.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.movielog.features.search.domain.model.Content

@Composable
fun SearchItem(
    content: Content,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {

        Image(
            painter = rememberAsyncImagePainter(content.imageUrl),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {

            Text(text = content.title)

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = content.type.name)

            content.releaseYear?.let {
                Text(text = it)
            }
        }
    }
}