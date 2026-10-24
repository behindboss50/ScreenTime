package com.example.ui.components

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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
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

@Composable
fun SessionItemCard(
    session: AppSession,
    isFirstInList: Boolean,
    isLastInList: Boolean,
    onSessionClick: (AppSession) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Idle break gap between sessions if gap >= 2 minutes
        if (session.gapBeforeMs >= 2 * 60 * 1000L) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timeline spacer line continuation
                Box(
                    modifier = Modifier
                        .width(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(24.dp)
                            .background(Color(0xFF263552))
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Idle gap badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF0F1726),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "💤",
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = session.formattedGapBefore,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 11.sp
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
            // Left Timeline Rail
            Column(
                modifier = Modifier
                    .width(36.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top connector line
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(14.dp)
                        .background(if (isFirstInList) Color.Transparent else Color(0xFF263552))
                )

                // Timeline Node Dot
                Box(
                    modifier = Modifier
                        .size(if (session.isNewPickup) 22.dp else 16.dp)
                        .clip(CircleShape)
                        .background(
                            if (session.isNewPickup)
                                session.category.color.copy(alpha = 0.25f)
                            else
                                session.category.color.copy(alpha = 0.15f)
                        )
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
                                .size(6.dp)
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
                        .background(if (isLastInList) Color.Transparent else Color(0xFF263552))
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Session Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                    .clickable { onSessionClick(session) }
                    .padding(14.dp)
                    .testTag("session_card_${session.sessionIndex}")
            ) {
                Column {
                    // Header: Start & End Time + Duration Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Clear Start and End Times
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = session.formattedStartTime,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                            Text(
                                text = "  →  ",
                                style = MaterialTheme.typography.labelMedium,
                                color = AccentCyan
                            )
                            Text(
                                text = session.formattedEndTime,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                        }

                        // Duration Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = session.category.color.copy(alpha = 0.18f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                session.category.color.copy(alpha = 0.5f)
                            )
                        ) {
                            Text(
                                text = session.formattedDuration,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = session.category.color,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // App Information Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Stylized App Avatar with Category Color
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(session.category.color.copy(alpha = 0.25f))
                                    .border(
                                        1.dp,
                                        session.category.color.copy(alpha = 0.6f),
                                        RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = session.appName.take(1).uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = session.category.color
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = session.appName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = session.category.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = " • Session #${session.sessionIndex}",
                                        style = MaterialTheme.typography.labelSmall,
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
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LockOpen,
                                            contentDescription = null,
                                            tint = AccentEmerald,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "Pickup",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
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
                }
            }
        }
    }
}
