package com.example.model

import androidx.compose.ui.graphics.Color

enum class AppCategory(
    val displayName: String,
    val color: Color
) {
    SOCIAL("Social", Color(0xFFEC4899)),
    ENTERTAINMENT("Entertainment", Color(0xFF8B5CF6)),
    COMMUNICATION("Communication", Color(0xFF06B6D4)),
    PRODUCTIVITY("Productivity", Color(0xFF10B981)),
    BROWSING("Browsing", Color(0xFF3B82F6)),
    GAMING("Gaming", Color(0xFFF97316)),
    UTILITY("Utility", Color(0xFF64748B)),
    SYSTEM("System", Color(0xFF94A3B8));

    companion object {
        fun fromPackage(pkg: String): AppCategory {
            val lower = pkg.lowercase()
            return when {
                lower.contains("instagram") || lower.contains("tiktok") ||
                lower.contains("facebook") || lower.contains("twitter") ||
                lower.contains("x.android") || lower.contains("reddit") ||
                lower.contains("threads") || lower.contains("snapchat") ||
                lower.contains("pinterest") -> SOCIAL

                lower.contains("youtube") || lower.contains("netflix") ||
                lower.contains("spotify") || lower.contains("prime") ||
                lower.contains("disney") || lower.contains("twitch") ||
                lower.contains("music") || lower.contains("podcast") ||
                lower.contains("media") || lower.contains("vlc") -> ENTERTAINMENT

                lower.contains("whatsapp") || lower.contains("telegram") ||
                lower.contains("slack") || lower.contains("discord") ||
                lower.contains("messenger") || lower.contains("dialer") ||
                lower.contains("mms") || lower.contains("signal") ||
                lower.contains("wechat") || lower.contains("contacts") -> COMMUNICATION

                lower.contains("gmail") || lower.contains("outlook") ||
                lower.contains("docs") || lower.contains("sheets") ||
                lower.contains("calendar") || lower.contains("notion") ||
                lower.contains("keep") || lower.contains("drive") ||
                lower.contains("notes") || lower.contains("task") ||
                lower.contains("duolingo") -> PRODUCTIVITY

                lower.contains("chrome") || lower.contains("browser") ||
                lower.contains("firefox") || lower.contains("opera") ||
                lower.contains("edge") || lower.contains("duckduckgo") -> BROWSING

                lower.contains("game") || lower.contains("supercell") ||
                lower.contains("roblox") || lower.contains("minecraft") ||
                lower.contains("pubg") || lower.contains("genshin") ||
                lower.contains("candycrush") -> GAMING

                lower.contains("settings") || lower.contains("systemui") ||
                lower.contains("launcher") || lower.contains("nexuslauncher") ||
                lower.contains("trebuchet") -> SYSTEM

                else -> UTILITY
            }
        }
    }
}
