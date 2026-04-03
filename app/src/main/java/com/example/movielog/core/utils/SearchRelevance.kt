package com.example.movielog.core.utils

import kotlin.math.max

fun calculateRelevance(query: String, title: String): Int {
    val q = query.lowercase().trim()
    val t = title.lowercase().trim()

    if (t == q) return 1000
    if (t.startsWith(q)) return 900
    if (t.contains(q)) return 800
    val qWords = q.split(" ")
    val tWords = t.split(" ")
    val wordMatches = qWords.count { qw ->
        tWords.any { tw -> tw.startsWith(qw) }
    }
    val wordScore = wordMatches * 100

    val distance = levenshteinDistance(q, t)
    val maxLen = max(q.length, t.length)
    val similarity = if (maxLen == 0) 1.0
    else 1.0 - (distance.toDouble() / maxLen)
    val fuzzyScore = (similarity * 500).toInt()

    return wordScore + fuzzyScore
}