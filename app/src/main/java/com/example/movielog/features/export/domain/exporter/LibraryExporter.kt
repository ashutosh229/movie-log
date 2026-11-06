package com.example.movielog.features.export.domain.exporter

import com.example.movielog.core.utils.formatDuration
import com.example.movielog.features.export.domain.model.ExportFormat
import com.example.movielog.features.export.domain.model.ExportResult
import com.example.movielog.features.library.domain.model.Progress
import com.example.movielog.features.library.domain.model.UserContent
import com.example.movielog.features.library.domain.model.WatchStatus
import com.example.movielog.features.library.domain.model.displayName
import com.example.movielog.features.search.domain.model.ContentType
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LibraryExporter {

    private val STATUS_ORDER = listOf(
        WatchStatus.ONGOING,
        WatchStatus.TO_WATCH,
        WatchStatus.COMPLETED,
        WatchStatus.REPOSITORY
    )

    private val CSV_HEADER = listOf(
        "Title", "Type", "Status", "Progress", "Added", "Updated"
    )

    fun export(
        content: List<UserContent>,
        format: ExportFormat
    ): ExportResult {
        val sorted = content.sortedBy { it.title.lowercase() }

        val body = when (format) {
            ExportFormat.CSV -> toCsv(sorted)
            ExportFormat.JSON -> toJson(sorted)
            ExportFormat.TEXT -> toText(sorted)
        }

        val stamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.US).format(Date())

        return ExportResult(
            format = format,
            fileName = "movielog_library_$stamp.${format.extension}",
            content = body
        )
    }

    // 🔹 CSV
    fun toCsv(content: List<UserContent>): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

        val rows = buildList {
            add(CSV_HEADER)
            content.forEach {
                add(
                    listOf(
                        it.title,
                        it.type.label(),
                        it.status.displayName(),
                        progressText(it.progress),
                        dateFormat.format(Date(it.createdAt)),
                        dateFormat.format(Date(it.updatedAt))
                    )
                )
            }
        }

        // BOM keeps Excel from garbling non-Latin titles (e.g. Japanese anime names)
        return "\uFEFF" + rows.joinToString("\r\n") { row ->
            row.joinToString(",") { escapeCsv(it) }
        }
    }

    private fun escapeCsv(value: String): String {
        // Neutralise spreadsheet formulas in user-visible titles
        val safe = if (value.firstOrNull() in listOf('=', '+', '@', '\t', '\r')) {
            "'$value"
        } else {
            value
        }

        return if (safe.any { it == ',' || it == '"' || it == '\n' || it == '\r' }) {
            "\"" + safe.replace("\"", "\"\"") + "\""
        } else {
            safe
        }
    }

    // 🔹 JSON
    fun toJson(content: List<UserContent>): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

        val items = content.map {
            linkedMapOf<String, Any?>(
                "id" to it.id,
                "title" to it.title,
                "type" to it.type.name,
                "status" to it.status.name,
                "imageUrl" to it.imageUrl,
                "progress" to progressMap(it.progress),
                "createdAt" to it.createdAt,
                "updatedAt" to it.updatedAt
            )
        }

        val root = linkedMapOf<String, Any?>(
            "app" to "MovieLog",
            "exportedAt" to isoFormat.format(Date()),
            "totalTitles" to content.size,
            "library" to items
        )

        return GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create()
            .toJson(root)
    }

    private fun progressMap(progress: Progress?): Map<String, Any?>? {
        return when (progress) {
            null -> null

            is Progress.MovieProgress -> linkedMapOf(
                "kind" to "MOVIE",
                "timestampSeconds" to progress.timestamp
            )

            is Progress.EpisodeProgress -> linkedMapOf(
                "kind" to "EPISODE",
                "season" to progress.season,
                "episode" to progress.episode.takeIf { it != Int.MAX_VALUE },
                "seasonCompleted" to (progress.episode == Int.MAX_VALUE),
                "timestampSeconds" to progress.timestamp
            )
        }
    }

    // 🔹 TEXT
    fun toText(content: List<UserContent>): String {
        val builder = StringBuilder()

        builder.append("My MovieLog library - ")
            .append(content.size)
            .append(if (content.size == 1) " title" else " titles")
            .append("\n")

        STATUS_ORDER.forEach { status ->
            val group = content.filter { it.status == status }

            if (group.isNotEmpty()) {
                builder.append("\n")
                    .append(status.displayName())
                    .append(" (")
                    .append(group.size)
                    .append(")\n")

                group.forEach { item ->
                    builder.append("- ")
                        .append(item.title)
                        .append(" (")
                        .append(item.type.label())
                        .append(")")

                    if (status == WatchStatus.ONGOING && item.progress != null) {
                        builder.append(" - ").append(progressText(item.progress))
                    }

                    builder.append("\n")
                }
            }
        }

        builder.append("\nShared from MovieLog")

        return builder.toString()
    }

    // 🔹 SHARED HELPERS
    private fun progressText(progress: Progress?): String {
        return when (progress) {
            null -> ""

            is Progress.MovieProgress -> {
                "Resume at ${formatDuration(progress.timestamp)}"
            }

            is Progress.EpisodeProgress -> when {
                progress.episode == Int.MAX_VALUE -> "Completed season ${progress.season}"
                progress.timestamp == 0L -> "Up to S${progress.season} E${progress.episode}"
                else -> "S${progress.season} E${progress.episode} at ${
                    formatDuration(progress.timestamp)
                }"
            }
        }
    }

    private fun ContentType.label(): String {
        return when (this) {
            ContentType.MOVIE -> "Movie"
            ContentType.SERIES -> "Series"
            ContentType.ANIME -> "Anime"
            ContentType.ANIME_MOVIE -> "Anime Movie"
        }
    }
}