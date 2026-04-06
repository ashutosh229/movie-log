package com.example.movielog.core.utils

fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return when {
        hours > 0 -> {
            String.format("%d:%02d:%02d", hours, minutes, secs)
        }

        minutes > 0 -> {
            String.format("%d:%02d", minutes, secs)
        }

        else -> {
            "0:${secs.toString().padStart(2, '0')}"
        }
    }
}