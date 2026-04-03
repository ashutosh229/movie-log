package com.example.movielog.core.utils

fun calculateRelevance(query: String, title: String): Int {
    val q = query.lowercase()
    val t = title.lowercase()

    return when {
        t == q -> 100                    // exact match
        t.startsWith(q) -> 80            // starts with query
        t.contains(q) -> 60              // contains query
        q.split(" ").all { t.contains(it) } -> 40  // all words present
        else -> 0
    }
}