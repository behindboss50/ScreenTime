package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppSession
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Calendar

@Composable
fun TimelineBar(
    sessions: List<AppSession>,
    selectedPeriod: String?,
    onPeriodSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    // Compute durations per period
    val morningMs = sessions.filter {
        val h = Calendar.getInstance().apply { timeInMillis = it.startTimeMs }.get(Calendar.HOUR_OF_DAY)
        h in 6..11
    }.sumOf { it.durationMs }

    val afternoonMs = sessions.filter {
        val h = Calendar.getInstance().apply { timeInMillis = it.startTimeMs }.get(Calendar.HOUR_OF_DAY)
        h in 12..16
    }.sumOf { it.durationMs }

    val eveningMs = sessions.filter {
        val h = Calendar.getInstance().apply { timeInMillis = it.startTimeMs }.get(Calendar.HOUR_OF_DAY)
        h in 17..20
    }.sumOf { it.durationMs }

    val nightMs = sessions.filter {
        val h = Calendar.getInstance().apply { timeInMillis = it.startTimeMs }.get(Calendar.HOUR_OF_DAY)
        h >= 21 || h < 6
    }.sumOf { it.durationMs }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Visual 24h Activity Strip Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(DarkSurface)
                .border(1.dp, DarkBorder, RoundedCornerShape(18.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DAILY SESSION RIBBON",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "06:00 AM ➔ 11:59 PM",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        color = AccentCyan
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Draw dark track background
                    drawRoundRect(
                        color = Color(0xFF0B101B),
                        size = Size(canvasWidth, canvasHeight),
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                    )

                    // Highlight selected time period window if active
                    val totalMinutesInDay = 24 * 60f
                    val highlightRange: Pair<Int, Int>? = when (selectedPeriod) {
                        "Morning" -> 6 * 60 to 12 * 60
                        "Afternoon" -> 12 * 60 to 17 * 60
                        "Evening" -> 17 * 60 to 21 * 60
                        "Night" -> 21 * 60 to 24 * 60
                        else -> null
                    }
                    if (highlightRange != null) {
                        val x0 = (highlightRange.first / totalMinutesInDay) * canvasWidth
                        val x1 = (highlightRange.second / totalMinutesInDay) * canvasWidth
                        drawRect(
                            color = AccentCyan.copy(alpha = 0.12f),
                            topLeft = Offset(x0, 0f),
                            size = Size(x1 - x0, canvasHeight)
                        )
                    }

                    // Draw hour grid lines every 3 hours (3, 6, 9, 12, 15, 18, 21)
                    for (h in 3..21 step 3) {
                        val x = (h * 60f / totalMinutesInDay) * canvasWidth
                        drawLine(
                            color = Color(0xFF1E293B),
                            start = Offset(x, 0f),
                            end = Offset(x, canvasHeight),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // Render active sessions as vibrant colored capsules
                    sessions.forEach { session ->
                        val calStart = Calendar.getInstance().apply { timeInMillis = session.startTimeMs }
                        val calEnd = Calendar.getInstance().apply { timeInMillis = session.endTimeMs }

                        val startMinuteOfDay = calStart.get(Calendar.HOUR_OF_DAY) * 60 + calStart.get(Calendar.MINUTE)
                        val endMinuteOfDay = calEnd.get(Calendar.HOUR_OF_DAY) * 60 + calEnd.get(Calendar.MINUTE)

                        val xStart = (startMinuteOfDay / totalMinutesInDay) * canvasWidth
                        val xEnd = (endMinuteOfDay / totalMinutesInDay) * canvasWidth
                        val blockWidth = maxOf(xEnd - xStart, 3.dp.toPx())

                        drawRoundRect(
                            color = session.category.color,
                            topLeft = Offset(xStart, 4.dp.toPx()),
                            size = Size(blockWidth, canvasHeight - 8.dp.toPx()),
                            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Time Markers below strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("12A", "6A", "9A", "12P", "3P", "6P", "9P", "11P").forEach { label ->
                        Text(
                            text = label,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 9.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interactive Time Period Block Cards
        val periods = listOf(
            PeriodChipInfo("All", null, "All Day", "Full stream", sessions.sumOf { it.durationMs }),
            PeriodChipInfo("Morning", "Morning", "☀️ Morning", "6 AM–12 PM", morningMs),
            PeriodChipInfo("Afternoon", "Afternoon", "🌤️ Afternoon", "12 PM–5 PM", afternoonMs),
            PeriodChipInfo("Evening", "Evening", "🌆 Evening", "5 PM–9 PM", eveningMs),
            PeriodChipInfo("Night", "Night", "🌙 Night", "9 PM–6 AM", nightMs)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            periods.forEach { info ->
                val isSelected = selectedPeriod == info.filterValue
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) AccentCyan.copy(alpha = 0.16f) else DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) AccentCyan else DarkBorder
                    ),
                    modifier = Modifier
                        .clickable { onPeriodSelected(info.filterValue) }
                        .testTag("filter_period_${info.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = info.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) AccentCyan else TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (info.durationMs > 0) AppSession.formatDuration(info.durationMs) else "0m",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) AccentCyan else TextSecondary
                            )
                        }
                        Text(
                            text = info.subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

private data class PeriodChipInfo(
    val id: String,
    val filterValue: String?,
    val title: String,
    val subtitle: String,
    val durationMs: Long
)
