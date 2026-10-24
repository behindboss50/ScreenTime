package com.example.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AppSession(
    val id: String,
    val packageName: String,
    val appName: String,
    val startTimeMs: Long,
    val endTimeMs: Long,
    val durationMs: Long = (endTimeMs - startTimeMs).coerceAtLeast(1000L),
    val category: AppCategory = AppCategory.fromPackage(packageName),
    val colorHex: Long = 0xFF06B6D4,
    val isNewPickup: Boolean = false,
    val gapBeforeMs: Long = 0L, // time phone was idle before this session began
    val sessionIndex: Int = 1
) {
    val formattedStartTime: String
        get() = timeFormat.format(Date(startTimeMs))

    val formattedEndTime: String
        get() = timeFormat.format(Date(endTimeMs))

    val formattedTimeRange: String
        get() = "$formattedStartTime — $formattedEndTime"

    val formattedDuration: String
        get() = formatDuration(durationMs)

    val formattedGapBefore: String
        get() = if (gapBeforeMs > 60_000L) {
            "Phone locked for ${formatDuration(gapBeforeMs)}"
        } else {
            "Brief pause (${(gapBeforeMs / 1000).coerceAtLeast(0)}s)"
        }

    companion object {
        private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val detailedTimeFormat = SimpleDateFormat("h:mm:ss a", Locale.getDefault())

        fun formatDuration(millis: Long): String {
            val totalSeconds = (millis / 1000).coerceAtLeast(0)
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60

            return when {
                hours > 0 -> {
                    if (minutes > 0) "${hours}h ${minutes}m" else "${hours}h"
                }
                minutes > 0 -> {
                    if (seconds > 0 && minutes < 5) "${minutes}m ${seconds}s" else "${minutes}m"
                }
                else -> "${seconds}s"
            }
        }
    }
}
