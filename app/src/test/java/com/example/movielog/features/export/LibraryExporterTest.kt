package com.example.movielog.features.export

import com.example.movielog.features.export.domain.exporter.LibraryExporter
import com.example.movielog.features.library.domain.model.Progress
import com.example.movielog.features.library.domain.model.UserContent
import com.example.movielog.features.library.domain.model.WatchStatus
import com.example.movielog.features.search.domain.model.ContentType
import org.junit.Assert.assertTrue
import org.junit.Test

class LibraryExporterTest {

    private fun item(
        title: String,
        status: WatchStatus = WatchStatus.TO_WATCH,
        progress: Progress? = null
    ) = UserContent(
        id = "1",
        title = title,
        imageUrl = null,
        type = ContentType.SERIES,
        status = status,
        progress = progress
    )

    @Test
    fun csv_escapesCommasAndQuotes() {
        val csv = LibraryExporter.toCsv(listOf(item("Hello, \"World\"")))
        assertTrue(csv.contains("\"Hello, \"\"World\"\"\""))
    }

    @Test
    fun csv_neutralisesFormulaTitles() {
        val csv = LibraryExporter.toCsv(listOf(item("=SUM(A1)")))
        assertTrue(csv.contains("'=SUM(A1)"))
    }

    @Test
    fun json_marksCompletedSeason() {
        val json = LibraryExporter.toJson(
            listOf(
                item(
                    "Show",
                    WatchStatus.ONGOING,
                    Progress.EpisodeProgress(2, Int.MAX_VALUE, 0L)
                )
            )
        )
        assertTrue(json.contains("\"seasonCompleted\": true"))
    }

    @Test
    fun text_groupsByStatusAndShowsProgress() {
        val text = LibraryExporter.toText(
            listOf(
                item("Show", WatchStatus.ONGOING, Progress.EpisodeProgress(1, 3, 0L)),
                item("Film")
            )
        )
        assertTrue(text.contains("Ongoing (1)"))
        assertTrue(text.contains("Up to S1 E3"))
        assertTrue(text.contains("To Watch (1)"))
    }
}