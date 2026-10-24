package com.example.model

data class AppUsageSummary(
    val packageName: String,
    val appName: String,
    val totalDurationMs: Long,
    val sessionCount: Int,
    val category: AppCategory,
    val colorHex: Long,
    val percentageOfTotal: Float
) {
    val formattedDuration: String
        get() = AppSession.formatDuration(totalDurationMs)
}

data class HourlyScreenTime(
    val hourOfDay: Int, // 0..23
    val label: String, // e.g. "9 AM"
    val screenTimeMs: Long,
    val sessionCount: Int
)

data class TimePeriodGroup(
    val title: String,
    val subtitle: String,
    val startHour: Int,
    val endHour: Int,
    val sessions: List<AppSession>,
    val totalTimeMs: Long = sessions.sumOf { it.durationMs },
    val pickupCount: Int = sessions.count { it.isNewPickup }
) {
    val formattedTotalTime: String
        get() = AppSession.formatDuration(totalTimeMs)
}

data class DailyUsageSummary(
    val dateMillis: Long,
    val dateLabel: String,
    val isToday: Boolean,
    val totalScreenTimeMs: Long,
    val pickupCount: Int,
    val firstPickupTimeMs: Long?,
    val longestSession: AppSession?,
    val averageSessionMs: Long,
    val sessions: List<AppSession>,
    val appSummaries: List<AppUsageSummary>,
    val hourlyDistribution: List<HourlyScreenTime>,
    val timePeriodGroups: List<TimePeriodGroup>
) {
    val formattedTotalTime: String
        get() = AppSession.formatDuration(totalScreenTimeMs)

    val formattedAverageSession: String
        get() = AppSession.formatDuration(averageSessionMs)
}
