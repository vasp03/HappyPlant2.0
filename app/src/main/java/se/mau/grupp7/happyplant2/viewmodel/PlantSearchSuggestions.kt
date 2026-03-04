package se.mau.grupp7.happyplant2.viewmodel

import kotlin.math.abs

internal fun boundedLevenshtein(a: String, b: String, max: Int): Int {
    val s = a.lowercase()
    val t = b.lowercase()

    val n = s.length
    val m = t.length

    if (abs(n - m) > max) return max + 1
    if (n == 0) return m
    if (m == 0) return n

    var prev = IntArray(m + 1) { it }
    var curr = IntArray(m + 1)

    for (i in 1..n) {
        curr[0] = i
        var rowMin = curr[0]
        val sc = s[i - 1]

        for (j in 1..m) {
            val cost = if (sc == t[j - 1]) 0 else 1
            val del = prev[j] + 1
            val ins = curr[j - 1] + 1
            val sub = prev[j - 1] + cost
            val v = minOf(del, ins, sub)
            curr[j] = v
            if (v < rowMin) rowMin = v
        }

        if (rowMin > max) return max + 1

        val tmp = prev
        prev = curr
        curr = tmp
    }

    return prev[m]
}

internal fun suggestQuery(
    query: String,
    candidates: List<String>,
    maxDistance: Int = 2
): List<String> {
    val q = query.trim().lowercase()
    if (q.length < 3) return emptyList()

    return candidates
        .asSequence()
        .map { it to boundedLevenshtein(q, it, maxDistance) }
        .filter { (_, distance) -> distance <= maxDistance }
        .sortedBy { it.second }
        .take(5)
        .map { it.first }
        .toList()
}