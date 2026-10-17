package com.example.data.model

import java.util.UUID

data class BrowserTab(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "Creator Hub",
    val url: String, // empty means Home/Dashboard
    val favicon: String? = null,
    val isDesktopMode: Boolean = true, // Default to Desktop Mode per requirement
    val isLoading: Boolean = false,
    val progress: Int = 0,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val isSecure: Boolean = true,
    val isPrivate: Boolean = false, // 🕶️ न्यू प्राइवेट टैब (Incognito Mode) फ्लैग
    val createdAt: Long = System.currentTimeMillis()
) {
    val isHome: Boolean
        get() = url.isBlank() || url == "about:blank" || url == "creatorhub://home"

    val displayTitle: String
        get() = when {
            isHome -> if (isPrivate) "Private Dashboard" else "Creator Dashboard"
            title.isNotBlank() -> title
            else -> url.removePrefix("https://").removePrefix("http://").take(30)
        }
}
