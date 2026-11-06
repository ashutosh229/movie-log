package com.example.movielog.features.export.domain.model

data class ExportResult(
    val format: ExportFormat,
    val fileName: String,
    val content: String
)