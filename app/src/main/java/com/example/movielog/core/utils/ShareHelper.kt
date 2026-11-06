package com.example.movielog.core.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.movielog.features.export.domain.model.ExportFormat
import com.example.movielog.features.export.domain.model.ExportResult
import java.io.File

fun shareExport(context: Context, result: ExportResult) {
    val subject = "My MovieLog library"

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = result.format.mimeType
        putExtra(Intent.EXTRA_SUBJECT, subject)

        if (result.format == ExportFormat.TEXT) {
            putExtra(Intent.EXTRA_TEXT, result.content)
        } else {
            val directory = File(context.cacheDir, "exports").apply { mkdirs() }
            val file = File(directory, result.fileName)
            file.writeText(result.content)

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    context.startActivity(Intent.createChooser(intent, "Share library"))
}