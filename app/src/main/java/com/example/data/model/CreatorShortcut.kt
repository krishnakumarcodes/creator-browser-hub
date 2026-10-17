package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

data class CreatorShortcut(
    val id: String,
    val name: String,
    val description: String = "",
    val url: String,
    val brandColor: Color,
    val category: String = "Web",
    val isPrimary: Boolean = false,
    val isCustom: Boolean = false
) {
    companion object {
        // होम स्क्रीन पर सबसे ऊपर दिखने वाले मुख्य 4 शॉर्टकट्स
        val TOP_SPEED_DIAL = listOf(
            CreatorShortcut(
                id = "google_search",
                name = "Google",
                url = "https://www.google.com",
                brandColor = Color(0xFF4285F4)
            ),
            CreatorShortcut(
                id = "yt_main",
                name = "YouTube",
                url = "https://www.youtube.com",
                brandColor = Color(0xFFFF0000)
            ),
            CreatorShortcut(
                id = "fb_main",
                name = "Facebook",
                url = "https://www.facebook.com",
                brandColor = Color(0xFF1877F2)
            ),
            CreatorShortcut(
                id = "ig_main",
                name = "Instagram",
                url = "https://www.instagram.com",
                brandColor = Color(0xFFE4405F)
            )
        )

        // पुराने क्रिएटर स्टूडियो शॉर्टकट्स (यथावत सुरक्षित)
        val DEFAULT_SHORTCUTS = listOf(
            CreatorShortcut(
                id = "yt_studio",
                name = "YouTube Studio",
                description = "Channel analytics, uploads, monetization & comments",
                url = "https://studio.youtube.com",
                brandColor = YouTubeColor,
                category = "Video & Audio",
                isPrimary = true
            ),
            CreatorShortcut(
                id = "ig_web",
                name = "Instagram Web",
                description = "Desktop feed, DMs, creator reels & post scheduler",
                url = "https://www.instagram.com",
                brandColor = InstagramGradientStart,
                category = "Social Media",
                isPrimary = true
            ),
            CreatorShortcut(
                id = "fb_creator",
                name = "Facebook Creator Studio",
                description = "Meta Business Suite, monetization & fan insights",
                url = "https://business.facebook.com/latest/home",
                brandColor = FacebookColor,
                category = "Social Media",
                isPrimary = true
            ),
            CreatorShortcut(
                id = "gmail",
                name = "Creator Gmail",
                description = "Brand sponsorships, client inquiries & collab mail",
                url = "https://mail.google.com",
                brandColor = GmailColor,
                category = "Productivity",
                isPrimary = true
            )
        )
    }
}
