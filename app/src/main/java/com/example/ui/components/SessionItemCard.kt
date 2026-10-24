package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppCategory
import com.example.model.AppSession
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SessionItemCard(
    session: AppSession,
    isFirstInList: Boolean,
    isLastInList: Boolean,
    onSessionClick: (AppSession) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appIconBitmap: ImageBitmap? = remember(session.packageName) {
        try {
            val drawable = context.packageManager.getApplicationIcon(session.packageName)
            drawableToBitmap(drawable).asImageBitmap()
        } catch (_: Exception) {
            null
        }
    }

    val railTimeLabel = remember(session.startTimeMs) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(session.startTimeMs))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Idle break gap between sessions if gap >= 2 minutes and not the first session
        if (!isFirstInList && session.gapBeforeMs >= 2 * 60 * 1000L) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.width(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(26.dp)
                            .background(Color(0xFF1E293B))
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFF0D1424),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E2B45))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NightlightRound,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = session.formattedGapBefore,
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = JetBrainsMonoFontFamily,
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // Main Session Row with Left Timeline Rail
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Left Timeline Rail with 24h Time Stamp & Node
            Column(
                modifier = Modifier
                    .width(46.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top connector line
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(12.dp)
                        .background(if (isFirstInList) Color.Transparent else Color(0xFF263552))
                )

                // Time Stamp Badge on Rail
                Text(
                    text = railTimeLabel,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = session.category.color
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Timeline Node Dot
                Box(
                    modifier = Modifier
                        .size(if (session.isNewPickup) 22.dp else 14.dp)
                        .clip(CircleShape)
                        .background(session.category.color.copy(alpha = 0.22f))
                        .border(
                            width = 2.dp,
                            color = session.category.color,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (session.isNewPickup) {
                        Icon(
                            imageVector = Icons.Default.LockOpen,
                            contentDescription = "Pickup",
                            tint = session.category.color,
                            modifier = Modifier.size(10.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(session.category.color)
                        )
                    }
                }

                // Bottom connector line
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = if (isLastInList) {
                                    listOf(Color.Transparent, Color.Transparent)
                                } else {
                                    listOf(
                                        session.category.color.copy(alpha = 0.5f),
                                        Color(0xFF263552)
                                    )
                                }
                            ),
                            shape = RoundedCornerShape(1.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Session Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                DarkSurface,
                                Color(0xFF131C2E)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = if (session.isNewPickup) session.category.color.copy(alpha = 0.35f) else DarkBorder,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable { onSessionClick(session) }
                    .testTag("session_card_${session.sessionIndex}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Top Row: Explicit Start -> End Time Capsule + Duration Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Start & End Time Pill Box
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF0A0F1A))
                                .border(1.dp, Color(0xFF1E2B42), RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Column {
                                Text(
                                    text = "START",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 8.sp,
                                    color = AccentEmerald,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = session.formattedStartTime,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "to",
                                tint = AccentCyan,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .size(14.dp)
                            )

                            Column {
                                Text(
                                    text = "END",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 8.sp,
                                    color = Color(0xFFF43F5E),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = session.formattedEndTime,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }

                        // Duration Pill
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = session.category.color.copy(alpha = 0.16f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                session.category.color.copy(alpha = 0.55f)
                            )
                        ) {
                            Text(
                                text = session.formattedDuration,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = session.category.color,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // App Identity & Metadata Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            // App Icon or Stylized Category Squircle
                            if (appIconBitmap != null) {
                                Image(
                                    bitmap = appIconBitmap,
                                    contentDescription = session.appName,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(11.dp))
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(11.dp))
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    session.category.color.copy(alpha = 0.35f),
                                                    session.category.color.copy(alpha = 0.12f)
                                                )
                                            )
                                        )
                                        .border(
                                            1.dp,
                                            session.category.color.copy(alpha = 0.6f),
                                            RoundedCornerShape(11.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = categoryIcon(session.category),
                                        contentDescription = session.category.displayName,
                                        tint = session.category.color,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = session.appName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(session.category.color)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = session.category.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = " • #${session.sessionIndex}",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (session.isNewPickup) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = AccentEmerald.copy(alpha = 0.15f),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        AccentEmerald.copy(alpha = 0.4f)
                                    ),
                                    modifier = Modifier.padding(end = 6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LockOpen,
                                            contentDescription = null,
                                            tint = AccentEmerald,
                                            modifier = Modifier.size(10.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "UNLOCK",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 9.sp,
                                            color = AccentEmerald,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "View Details",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Subtle session intensity bar (relative to 45 minutes max)
                    val fillFraction = (session.durationMs.toFloat() / (45 * 60 * 1000f)).coerceIn(0.06f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF0A0F1A))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fillFraction)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            session.category.color.copy(alpha = 0.6f),
                                            session.category.color
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

private fun categoryIcon(category: AppCategory): ImageVector {
    return when (category) {
        AppCategory.SOCIAL -> Icons.Default.People
        AppCategory.ENTERTAINMENT -> Icons.Default.PlayArrow
        AppCategory.COMMUNICATION -> Icons.Default.ChatBubble
        AppCategory.PRODUCTIVITY -> Icons.Default.Work
        AppCategory.BROWSING -> Icons.Default.Public
        AppCategory.GAMING -> Icons.Default.SportsEsports
        AppCategory.UTILITY -> Icons.Default.Build
        AppCategory.SYSTEM -> Icons.Default.Settings
    }
}

private fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable && drawable.bitmap != null) {
        return drawable.bitmap
    }
    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 96
    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 96
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
