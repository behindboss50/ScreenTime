package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DailyUsageSummary
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DailySummaryHeader(
    summary: DailyUsageSummary?,
    isDemoMode: Boolean,
    hasPermission: Boolean,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onToday: () -> Unit,
    onToggleDemo: () -> Unit,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Sleek Date Switcher Pill Bar
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF111827),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onPreviousDay,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceElevated)
                        .testTag("prev_day_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Day",
                        tint = TextPrimary
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onToday() }
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = summary?.dateLabel ?: "Today",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (summary?.isToday == true) "ACTIVE DAY • TAP TO REFRESH" else "TAP TO JUMP TO TODAY",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        letterSpacing = 0.8.sp,
                        color = AccentCyan
                    )
                }

                IconButton(
                    onClick = onNextDay,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceElevated)
                        .testTag("next_day_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Day",
                        tint = TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Atmospheric Hero Telemetry Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF141E33),
                            Color(0xFF0C1322),
                            Color(0xFF0A0F1D)
                        )
                    )
                )
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                AccentCyan.copy(alpha = 0.16f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.85f, size.height * 0.2f),
                            radius = size.width * 0.6f
                        )
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                AccentEmerald.copy(alpha = 0.12f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.15f, size.height * 0.85f),
                            radius = size.width * 0.5f
                        )
                    )
                }
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AccentCyan.copy(alpha = 0.4f),
                            DarkBorder,
                            AccentEmerald.copy(alpha = 0.25f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                // Top Status Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AccentEmerald)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TRUE SCREEN TIME",
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.4.sp,
                            color = AccentCyan
                        )
                    }

                    // Demo / Live Mode Switch Pill
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (isDemoMode) Color(0xFF281F12) else Color(0xFF0E2923),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDemoMode) Color(0xFFF59E0B).copy(alpha = 0.7f) else AccentEmerald.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier
                            .clickable { onToggleDemo() }
                            .testTag("toggle_demo_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = if (isDemoMode) Color(0xFFFBBF24) else AccentEmerald,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isDemoMode) "Sample Day" else "Live Usage",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isDemoMode) Color(0xFFFDE68A) else Color(0xFF6EE7B7)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Big Hero Time Display
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = summary?.formattedTotalTime ?: "0m",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-1).sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.padding(bottom = 7.dp)) {
                        Text(
                            text = "${summary?.sessions?.size ?: 0} SESSIONS",
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = AccentEmerald
                        )
                        Text(
                            text = "Tracked from morning",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Stacked Category Split Bar
                val appSummaries = summary?.appSummaries ?: emptyList()
                val totalMs = (summary?.totalScreenTimeMs ?: 1L).coerceAtLeast(1L)
                val categoryTotals = appSummaries
                    .groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.totalDurationMs } }
                    .entries
                    .sortedByDescending { it.value }

                if (categoryTotals.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF1E293B)),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        categoryTotals.forEach { (cat, catMs) ->
                            val weight = (catMs.toFloat() / totalMs.toFloat()).coerceAtLeast(0.03f)
                            Box(
                                modifier = Modifier
                                    .weight(weight)
                                    .fillMaxHeight()
                                    .background(cat.color)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        categoryTotals.take(4).forEach { (cat, catMs) ->
                            val pct = ((catMs.toFloat() / totalMs.toFloat()) * 100).toInt()
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(cat.color)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${cat.displayName} $pct%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4-Card Telemetry Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val firstPickupTime = summary?.firstPickupTimeMs?.let {
                        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(it))
                    } ?: "—"

                    MetricMiniCard(
                        icon = Icons.Default.WbSunny,
                        label = "FIRST WAKE",
                        value = firstPickupTime,
                        accentColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )

                    MetricMiniCard(
                        icon = Icons.Default.LockOpen,
                        label = "PICKUPS",
                        value = "${summary?.pickupCount ?: 0}x",
                        accentColor = AccentEmerald,
                        modifier = Modifier.weight(1f)
                    )

                    MetricMiniCard(
                        icon = Icons.Default.HourglassTop,
                        label = "AVG SESSION",
                        value = summary?.formattedAverageSession ?: "0m",
                        accentColor = AccentCyan,
                        modifier = Modifier.weight(1f)
                    )

                    val longestDur = summary?.longestSession?.formattedDuration ?: "0m"
                    MetricMiniCard(
                        icon = Icons.Default.Schedule,
                        label = "PEAK USE",
                        value = longestDur,
                        accentColor = AccentViolet,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Usage Permission Notice Banner if not granted and in live mode
        AnimatedVisibility(
            visible = !hasPermission && !isDemoMode,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF261D13))
                    .border(1.dp, Color(0xFFB45309), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Usage Access Permission Needed",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFDE68A)
                        )
                        Text(
                            text = "Enable Usage Access to track live app start & end times from your device",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFCD34D)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF59E0B),
                        modifier = Modifier
                            .clickable { onRequestPermission() }
                            .testTag("grant_permission_button")
                    ) {
                        Text(
                            text = "Grant",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricMiniCard(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF162033).copy(alpha = 0.85f))
            .border(1.dp, Color(0xFF25344F), RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = accentColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = value,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )
        }
    }
}
