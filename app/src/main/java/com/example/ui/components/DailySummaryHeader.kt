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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Smartphone
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
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
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
        // Date Navigation Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousDay,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceElevated)
                    .testTag("prev_day_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous Day",
                    tint = TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onToday() }
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = summary?.dateLabel ?: "Today",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (summary?.isToday == true) "Tap to refresh" else "Tap for today",
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentCyan
                )
            }

            IconButton(
                onClick = onNextDay,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceElevated)
                    .testTag("next_day_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next Day",
                    tint = TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Hero Metric Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF131D31),
                            Color(0xFF0F1726)
                        )
                    )
                )
                .border(1.dp, DarkBorder, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column {
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
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TRUE SCREEN TIME",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.2.sp,
                            color = AccentCyan
                        )
                    }

                    // Demo / Live Toggle Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isDemoMode) Color(0xFF2E2413) else Color(0xFF132B25),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDemoMode) Color(0xFFF59E0B) else AccentEmerald
                        ),
                        modifier = Modifier
                            .clickable { onToggleDemo() }
                            .testTag("toggle_demo_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isDemoMode) Color(0xFFF59E0B) else AccentEmerald)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isDemoMode) "Sample Day" else "Live Device",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = if (isDemoMode) Color(0xFFFCD34D) else Color(0xFF6EE7B7)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Big Total Time
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = summary?.formattedTotalTime ?: "0m",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "across ${summary?.sessions?.size ?: 0} sessions",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4-Card Quick Metrics Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricMiniCard(
                        icon = Icons.Default.LockOpen,
                        label = "Pickups",
                        value = "${summary?.pickupCount ?: 0}",
                        accentColor = AccentEmerald,
                        modifier = Modifier.weight(1f)
                    )

                    val firstPickupTime = summary?.firstPickupTimeMs?.let {
                        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(it))
                    } ?: "—"

                    MetricMiniCard(
                        icon = Icons.Default.WbSunny,
                        label = "First Wake",
                        value = firstPickupTime,
                        accentColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )

                    MetricMiniCard(
                        icon = Icons.Default.HourglassTop,
                        label = "Avg Session",
                        value = summary?.formattedAverageSession ?: "0m",
                        accentColor = AccentCyan,
                        modifier = Modifier.weight(1f)
                    )

                    val longestDur = summary?.longestSession?.formattedDuration ?: "0m"
                    MetricMiniCard(
                        icon = Icons.Default.Schedule,
                        label = "Peak Session",
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
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF261D13))
                    .border(1.dp, Color(0xFFB45309), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Usage Access Required",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFDE68A)
                        )
                        Text(
                            text = "Grant permission to read live app sessions on this device",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFCD34D)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
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
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
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
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, Color(0xFF26334D), RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = accentColor,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextMuted,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )
        }
    }
}
