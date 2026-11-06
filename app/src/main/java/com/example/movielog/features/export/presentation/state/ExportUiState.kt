package com.example.movielog.features.export.presentation.state

import com.example.movielog.features.export.domain.model.ExportResult

data class ExportUiState(
    val isExporting: Boolean = false,
    val pendingShare: ExportResult? = null,
    val error: String? = null
)