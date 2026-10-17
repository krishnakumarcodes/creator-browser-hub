package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🌙 1. डार्क थीम कलर्स (गहरा काला - Dark Obsidian)
private val CreatorDarkColorScheme = darkColorScheme(
    primary = CreatorViolet,
    onPrimary = Color.White,
    primaryContainer = CreatorVioletContainer,
    onPrimaryContainer = CreatorVioletLight,
    secondary = CreatorCyan,
    onSecondary = Color.Black,
    secondaryContainer = CreatorCyanContainer,
    onSecondaryContainer = CreatorCyanLight,
    tertiary = CreatorRose,
    onTertiary = Color.White,
    tertiaryContainer = CreatorRoseContainer,
    onTertiaryContainer = CreatorRoseLight,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder,
    outlineVariant = DarkSurfaceVariant,
    error = CreatorRose,
    onError = Color.White
)

// ☀️ 2. लाइट थीम कलर्स (साफ़ सफ़ेद - Clean Light White)
private val CreatorLightColorScheme = lightColorScheme(
    primary = CreatorViolet,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = CreatorVioletContainer,
    secondary = CreatorCyan,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCFFAFE),
    onSecondaryContainer = CreatorCyanContainer,
    tertiary = CreatorRose,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE4E6),
    onTertiaryContainer = CreatorRoseContainer,
    background = Color(0xFFF8FAFC), // साफ़ सफ़ेद बैकग्राउंड
    onBackground = Color(0xFF0F172A), // गहरा स्पष्ट टेक्स्ट
    surface = Color(0xFFFFFFFF),    // कार्ड्स और पैनल्स के लिए प्योर व्हाइट
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    error = CreatorRose,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // डिफ़ॉल्ट रूप से डार्क, टॉगल करने पर लाइट
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CreatorDarkColorScheme else CreatorLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
