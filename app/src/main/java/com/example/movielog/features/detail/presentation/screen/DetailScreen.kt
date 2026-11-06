package com.example.movielog.features.detail.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movielog.core.ui.components.library.ProgressDialog
import com.example.movielog.core.ui.components.search.StatusSelectionDialog
import com.example.movielog.features.detail.domain.model.ContentDetail
import com.example.movielog.features.detail.presentation.state.DetailUiState
import com.example.movielog.features.detail.presentation.viewmodel.DetailViewModel
import com.example.movielog.features.library.domain.mapper.toUserContent
import com.example.movielog.features.library.domain.model.WatchStatus
import com.example.movielog.features.library.domain.model.displayName
import com.example.movielog.features.library.domain.repository.LibraryRepository
import com.example.movielog.features.search.domain.model.ContentSnapshot
import com.example.movielog.features.search.domain.model.ContentType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    libraryRepository: LibraryRepository,
    contentId: String,
    contentType: ContentType,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val libraryItems by remember(libraryRepository) {
        libraryRepository.observeAllContent().catch { emit(emptyList()) }
    }.collectAsState(initial = emptyList())

    val existing = libraryItems.firstOrNull {
        it.id == contentId && it.type == contentType
    }

    var showStatusDialog by remember { mutableStateOf(false) }
    var showProgressDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(contentId, contentType) {
        viewModel.load(contentId, contentType)
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f),
            MaterialTheme.colorScheme.background
        )
    )

    val detail = (uiState as? DetailUiState.Success)
        ?.detail
        ?.takeIf { it.id == contentId }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        when {
            uiState is DetailUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp, start = 12.dp, end = 20.dp)
                ) {
                    BackButton(onBack)

                    DetailStateCard(
                        title = "Details unavailable",
                        description = (uiState as DetailUiState.Error).message,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            detail == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                DetailContent(
                    detail = detail,
                    libraryLabel = existing?.let {
                        "In library: ${it.status.displayName()} · Change"
                    } ?: "Add to library",
                    onBack = onBack,
                    onLibraryClick = { showStatusDialog = true }
                )
            }
        }
    }

    if (showStatusDialog && detail != null) {
        StatusSelectionDialog(
            onDismiss = {
                showStatusDialog = false
            },
            onStatusSelected = { status ->
                showStatusDialog = false

                if (status == WatchStatus.ONGOING) {
                    showProgressDialog = true
                } else {
                    val content = existing?.copy(
                        status = status,
                        updatedAt = System.currentTimeMillis()
                    ) ?: detail.toSnapshot().toUserContent(status)

                    scope.launch {
                        libraryRepository.addOrUpdateContent(content)
                    }
                }
            }
        )
    }

    if (showProgressDialog && detail != null) {
        ProgressDialog(
            type = detail.type,
            onDismiss = {
                showProgressDialog = false
            },
            onSave = { progress ->
                val base = existing
                    ?: detail.toSnapshot().toUserContent(WatchStatus.ONGOING)

                val content = base.copy(
                    status = WatchStatus.ONGOING,
                    progress = progress,
                    updatedAt = System.currentTimeMillis()
                )

                scope.launch {
                    libraryRepository.addOrUpdateContent(content)
                }

                showProgressDialog = false
            }
        )
    }
}

private fun ContentDetail.toSnapshot(): ContentSnapshot {
    return ContentSnapshot(
        id = id,
        title = title,
        imageUrl = imageUrl,
        type = type
    )
}

@Composable
private fun DetailContent(
    detail: ContentDetail,
    libraryLabel: String,
    onBack: () -> Unit,
    onLibraryClick: () -> Unit
) {
    val stats = buildList {
        detail.rating?.let { add("Rating" to "★ ${"%.1f".format(it)}") }
        detail.runtime?.let { add("Runtime" to it) }
        detail.seasons?.let { add("Seasons" to it.toString()) }
        detail.episodes?.let { add("Episodes" to it.toString()) }
        detail.status?.let { add("Status" to it) }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 24.dp,
            end = 20.dp,
            bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BackButton(onBack)
        }

        item {
            DetailHeader(detail = detail)
        }

        if (detail.genres.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    detail.genres.forEach { genre ->
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        ) {
                            Text(
                                text = genre,
                                modifier = Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 8.dp
                                ),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }
        }

        if (stats.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    stats.chunked(2).forEach { rowStats ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowStats.forEach { (label, value) ->
                                DetailStatCard(
                                    label = label,
                                    value = value,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (rowStats.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Overview",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = detail.overview,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Button(
                onClick = onLibraryClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(libraryLabel)
            }
        }
    }
}

@Composable
private fun BackButton(onBack: () -> Unit) {
    IconButton(onClick = onBack) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
        )
    }
}

@Composable
private fun DetailHeader(detail: ContentDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(18.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = detail.imageUrl,
                contentDescription = detail.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 120.dp, height = 172.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.headlineMedium
                )

                detail.tagline?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = when (detail.type) {
                                ContentType.MOVIE -> "MOVIE"
                                ContentType.SERIES -> "SERIES"
                                ContentType.ANIME,
                                ContentType.ANIME_MOVIE -> "ANIME"
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = detail.releaseYear,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DetailStateCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.14f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}