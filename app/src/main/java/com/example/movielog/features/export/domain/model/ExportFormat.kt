package com.example.movielog.features.export.domain.model

enum class ExportFormat(
    val title: String,
    val description: String,
    val mimeType: String,
    val extension: String
) {
    CSV(
        title = "CSV spreadsheet",
        description = "Open in Excel or Google Sheets.",
        mimeType = "text/csv",
        extension = "csv"
    ),
    JSON(
        title = "JSON backup",
        description = "Complete data including progress details.",
        mimeType = "application/json",
        extension = "json"
    ),
    TEXT(
        title = "Text list",
        description = "A readable list to share in any chat app.",
        mimeType = "text/plain",
        extension = "txt"
    )
}