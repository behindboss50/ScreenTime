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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Section Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Day Activity Panorama",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Morning → Night",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Visual 24h Activity Strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DarkSurface)
                .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Column {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Draw dark track background
                    drawRoundRect(
                        color = Color(0xFF162032),
                        size = Size(canvasWidth, canvasHeight),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )

                    // Draw hour grid lines every 3 hours (3, 6, 9, 12, 15, 18, 21)
                    val totalMinutesInDay = 24 * 60f
                    for (h in 3..21 step 3) {
                        val x = (h * 60f / totalMinutesInDay) * canvasWidth
                        drawLine(
                            color = Color(0xFF263552),
                            start = Offset(x, 0f),
                            end = Offset(x, canvasHeight),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // Render active sessions as colored blocks
                    sessions.forEach { session ->
                        val calStart = Calendar.getInstance().apply { timeInMillis = session.startTimeMs }
                        val calEnd = Calendar.getInstance().apply { timeInMillis = session.endTimeMs }

                        val startMinuteOfDay = calStart.get(Calendar.HOUR_OF_DAY) * 60 + calStart.get(Calendar.MINUTE)
                        val endMinuteOfDay = calEnd.get(Calendar.HOUR_OF_DAY) * 60 + calEnd.get(Calendar.MINUTE)

                        val xStart = (startMinuteOfDay / totalMinutesInDay) * canvasWidth
                        val xEnd = (endMinuteOfDay / totalMinutesInDay) * canvasWidth
                        val blockWidth = maxOf(xEnd - xStart, 2.5.dp.toPx())

                        drawRoundRect(
                            color = session.category.color,
                            topLeft = Offset(xStart, 3.dp.toPx()),
                            size = Size(blockWidth, canvasHeight - 6.dp.toPx()),
                            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Time Labels below strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("6 AM", "9 AM", "12 PM", "3 PM", "6 PM", "9 PM").forEach { label ->
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Time Period Filter Chips Row
        val periods = listOf(
            Triple("All", null, "All Day"),
            Triple("Morning", "Morning", "Morning (6-12)"),
            Triple("Afternoon", "Afternoon", "Afternoon (12-5)"),
            Triple("Evening", "Evening", "Evening (5-9)"),
            Triple("Night", "Night", "Night (9-6)")
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            periods.forEach { (id, filterValue, title) ->
                val isSelected = selectedPeriod == filterValue
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) AccentCyan.copy(alpha = 0.2f) else DarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) AccentCyan else DarkBorder
                    ),
                    modifier = Modifier
                        .clickable { onPeriodSelected(filterValue) }
                        .testTag("filter_period_$id")
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) AccentCyan else TextSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
