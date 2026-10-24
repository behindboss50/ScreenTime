package com.example.data

import com.example.model.AppCategory
import com.example.model.AppSession
import com.example.model.AppUsageSummary
import com.example.model.DailyUsageSummary
import com.example.model.HourlyScreenTime
import com.example.model.TimePeriodGroup
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DemoDataProvider {

    data class RawSessionTemplate(
        val appName: String,
        val packageName: String,
        val category: AppCategory,
        val colorHex: Long,
        val startHour: Int,
        val startMinute: Int,
        val durationMinutes: Int,
        val durationSeconds: Int = 0
    )

    private val sampleDayTemplates = listOf(
        // Morning Wake-up & Commute (7:00 AM - 9:00 AM)
        RawSessionTemplate("Alarm & Clock", "com.google.android.deskclock", AppCategory.UTILITY, 0xFF64748B, 7, 5, 2, 15),
        RawSessionTemplate("WhatsApp", "com.whatsapp", AppCategory.COMMUNICATION, 0xFF25D366, 7, 12, 6, 40),
        RawSessionTemplate("Weather", "com.google.android.weather", AppCategory.UTILITY, 0xFF0284C7, 7, 24, 3, 10),
        RawSessionTemplate("Spotify", "com.spotify.music", AppCategory.ENTERTAINMENT, 0xFF1DB954, 7, 45, 14, 20),
        RawSessionTemplate("Gmail", "com.google.android.gm", AppCategory.PRODUCTIVITY, 0xFFEA4335, 8, 15, 8, 30),
        RawSessionTemplate("Google Maps", "com.google.android.apps.maps", AppCategory.UTILITY, 0xFF34A853, 8, 35, 18, 0),

        // Late Morning Work (9:00 AM - 12:00 PM)
        RawSessionTemplate("Slack", "com.slack", AppCategory.COMMUNICATION, 0xFF4A154B, 9, 10, 16, 45),
        RawSessionTemplate("Google Chrome", "com.android.chrome", AppCategory.BROWSING, 0xFF4285F4, 9, 40, 22, 10),
        RawSessionTemplate("Notion", "notion.id", AppCategory.PRODUCTIVITY, 0xFF0F172A, 10, 15, 12, 5),
        RawSessionTemplate("Instagram", "com.instagram.android", AppCategory.SOCIAL, 0xFFE1306C, 10, 48, 7, 30),
        RawSessionTemplate("Slack", "com.slack", AppCategory.COMMUNICATION, 0xFF4A154B, 11, 10, 14, 20),
        RawSessionTemplate("WhatsApp", "com.whatsapp", AppCategory.COMMUNICATION, 0xFF25D366, 11, 40, 5, 15),

        // Afternoon (12:00 PM - 5:00 PM)
        RawSessionTemplate("YouTube", "com.google.android.youtube", AppCategory.ENTERTAINMENT, 0xFFFF0000, 12, 15, 28, 40),
        RawSessionTemplate("Reddit", "com.reddit.frontpage", AppCategory.SOCIAL, 0xFFFF4500, 12, 50, 11, 20),
        RawSessionTemplate("Google Chrome", "com.android.chrome", AppCategory.BROWSING, 0xFF4285F4, 13, 30, 15, 50),
        RawSessionTemplate("Duolingo", "com.duolingo", AppCategory.PRODUCTIVITY, 0xFF58CC02, 14, 20, 9, 15),
        RawSessionTemplate("Slack", "com.slack", AppCategory.COMMUNICATION, 0xFF4A154B, 15, 5, 18, 30),
        RawSessionTemplate("Camera", "com.google.android.GoogleCamera", AppCategory.UTILITY, 0xFF6366F1, 16, 0, 4, 10),
        RawSessionTemplate("Instagram", "com.instagram.android", AppCategory.SOCIAL, 0xFFE1306C, 16, 25, 19, 45),

        // Evening (5:00 PM - 9:00 PM)
        RawSessionTemplate("Spotify", "com.spotify.music", AppCategory.ENTERTAINMENT, 0xFF1DB954, 17, 10, 25, 0),
        RawSessionTemplate("WhatsApp", "com.whatsapp", AppCategory.COMMUNICATION, 0xFF25D366, 18, 5, 12, 15),
        RawSessionTemplate("YouTube", "com.google.android.youtube", AppCategory.ENTERTAINMENT, 0xFFFF0000, 18, 40, 36, 10),
        RawSessionTemplate("Subway Surfers", "com.kiloo.subwaysurf", AppCategory.GAMING, 0xFFF97316, 19, 35, 15, 20),
        RawSessionTemplate("X (Twitter)", "com.twitter.android", AppCategory.SOCIAL, 0xFF1DA1F2, 20, 10, 16, 30),

        // Night (9:00 PM - 11:30 PM)
        RawSessionTemplate("Netflix", "com.netflix.mediaclient", AppCategory.ENTERTAINMENT, 0xFFE50914, 21, 0, 42, 15),
        RawSessionTemplate("WhatsApp", "com.whatsapp", AppCategory.COMMUNICATION, 0xFF25D366, 21, 55, 6, 10),
        RawSessionTemplate("Reddit", "com.reddit.frontpage", AppCategory.SOCIAL, 0xFFFF4500, 22, 15, 14, 40),
        RawSessionTemplate("Alarm & Clock", "com.google.android.deskclock", AppCategory.UTILITY, 0xFF64748B, 22, 45, 2, 0)
    )

    fun generateDailySummary(targetDateMillis: Long, isToday: Boolean): DailyUsageSummary {
        val cal = Calendar.getInstance().apply {
            timeInMillis = targetDateMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = cal.timeInMillis

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)

        // Filter sessions up to current time if isToday
        val rawList = if (isToday) {
            val nowMinutes = currentHour * 60 + currentMinute
            sampleDayTemplates.filter { (it.startHour * 60 + it.startMinute) <= nowMinutes }
                .ifEmpty { sampleDayTemplates.take(6) }
        } else {
            sampleDayTemplates
        }

        val sessions = mutableListOf<AppSession>()
        var previousEndTime = 0L

        rawList.forEachIndexed { index, item ->
            val sessionCal = Calendar.getInstance().apply {
                timeInMillis = startOfDay
                set(Calendar.HOUR_OF_DAY, item.startHour)
                set(Calendar.MINUTE, item.startMinute)
                set(Calendar.SECOND, (index * 7) % 60)
            }
            val startMs = sessionCal.timeInMillis
            val durationMs = (item.durationMinutes * 60L + item.durationSeconds) * 1000L
            val endMs = startMs + durationMs

            val gapBefore = if (previousEndTime > 0L) {
                (startMs - previousEndTime).coerceAtLeast(0L)
            } else {
                // First session of the day
                3600_000L * 7L // Slept for 7 hours
            }

            // Flag as pickup if gap > 2 minutes or first session of day
            val isPickup = index == 0 || gapBefore >= 2 * 60 * 1000L

            sessions.add(
                AppSession(
                    id = "demo_session_$index",
                    packageName = item.packageName,
                    appName = item.appName,
                    startTimeMs = startMs,
                    endTimeMs = endMs,
                    durationMs = durationMs,
                    category = item.category,
                    colorHex = item.colorHex,
                    isNewPickup = isPickup,
                    gapBeforeMs = gapBefore,
                    sessionIndex = index + 1
                )
            )
            previousEndTime = endMs
        }

        val totalScreenTimeMs = sessions.sumOf { it.durationMs }
        val pickupCount = sessions.count { it.isNewPickup }
        val firstPickupTimeMs = sessions.firstOrNull { it.isNewPickup }?.startTimeMs
        val longestSession = sessions.maxByOrNull { it.durationMs }
        val avgSessionMs = if (sessions.isNotEmpty()) totalScreenTimeMs / sessions.size else 0L

        // App Summaries
        val groupedByApp = sessions.groupBy { it.packageName }
        val appSummaries = groupedByApp.map { (pkg, appSessions) ->
            val appTotal = appSessions.sumOf { it.durationMs }
            val first = appSessions.first()
            val percentage = if (totalScreenTimeMs > 0) (appTotal.toFloat() / totalScreenTimeMs) * 100f else 0f
            AppUsageSummary(
                packageName = pkg,
                appName = first.appName,
                totalDurationMs = appTotal,
                sessionCount = appSessions.size,
                category = first.category,
                colorHex = first.colorHex,
                percentageOfTotal = percentage
            )
        }.sortedByDescending { it.totalDurationMs }

        // Hourly Distribution (24 hours)
        val hourly = (0..23).map { hour ->
            val hourStart = startOfDay + hour * 3600_000L
            val hourEnd = hourStart + 3600_000L
            var timeInHour = 0L
            var countInHour = 0

            sessions.forEach { s ->
                val overlapStart = maxOf(s.startTimeMs, hourStart)
                val overlapEnd = minOf(s.endTimeMs, hourEnd)
                if (overlapEnd > overlapStart) {
                    timeInHour += (overlapEnd - overlapStart)
                    countInHour++
                }
            }

            val label = when (hour) {
                0 -> "12 AM"
                12 -> "12 PM"
                in 1..11 -> "$hour AM"
                else -> "${hour - 12} PM"
            }

            HourlyScreenTime(
                hourOfDay = hour,
                label = label,
                screenTimeMs = timeInHour,
                sessionCount = countInHour
            )
        }

        // Time Period Groups: Morning, Afternoon, Evening, Night
        val morningSessions = sessions.filter {
            val h = Calendar.getInstance().apply { timeInMillis = it.startTimeMs }.get(Calendar.HOUR_OF_DAY)
            h in 6..11
        }
        val afternoonSessions = sessions.filter {
            val h = Calendar.getInstance().apply { timeInMillis = it.startTimeMs }.get(Calendar.HOUR_OF_DAY)
            h in 12..16
        }
        val eveningSessions = sessions.filter {
            val h = Calendar.getInstance().apply { timeInMillis = it.startTimeMs }.get(Calendar.HOUR_OF_DAY)
            h in 17..20
        }
        val nightSessions = sessions.filter {
            val h = Calendar.getInstance().apply { timeInMillis = it.startTimeMs }.get(Calendar.HOUR_OF_DAY)
            h >= 21 || h < 6
        }

        val periodGroups = mutableListOf<TimePeriodGroup>()
        if (morningSessions.isNotEmpty()) {
            periodGroups.add(
                TimePeriodGroup(
                    title = "Morning",
                    subtitle = "6:00 AM — 12:00 PM",
                    startHour = 6,
                    endHour = 12,
                    sessions = morningSessions
                )
            )
        }
        if (afternoonSessions.isNotEmpty()) {
            periodGroups.add(
                TimePeriodGroup(
                    title = "Afternoon",
                    subtitle = "12:00 PM — 5:00 PM",
                    startHour = 12,
                    endHour = 17,
                    sessions = afternoonSessions
                )
            )
        }
        if (eveningSessions.isNotEmpty()) {
            periodGroups.add(
                TimePeriodGroup(
                    title = "Evening",
                    subtitle = "5:00 PM — 9:00 PM",
                    startHour = 17,
                    endHour = 21,
                    sessions = eveningSessions
                )
            )
        }
        if (nightSessions.isNotEmpty()) {
            periodGroups.add(
                TimePeriodGroup(
                    title = "Night",
                    subtitle = "9:00 PM — 6:00 AM",
                    startHour = 21,
                    endHour = 24,
                    sessions = nightSessions
                )
            )
        }

        val dateLabel = if (isToday) {
            "Today, " + SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(targetDateMillis))
        } else {
            SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date(targetDateMillis))
        }

        return DailyUsageSummary(
            dateMillis = targetDateMillis,
            dateLabel = dateLabel,
            isToday = isToday,
            totalScreenTimeMs = totalScreenTimeMs,
            pickupCount = pickupCount,
            firstPickupTimeMs = firstPickupTimeMs,
            longestSession = longestSession,
            averageSessionMs = avgSessionMs,
            sessions = sessions,
            appSummaries = appSummaries,
            hourlyDistribution = hourly,
            timePeriodGroups = periodGroups
        )
    }
}
