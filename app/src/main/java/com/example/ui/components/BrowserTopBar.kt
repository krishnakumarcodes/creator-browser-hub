package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BrowserTab
import com.example.viewmodel.BrowserViewModel

@Composable
fun BrowserTopBar(
    tab: BrowserTab,
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val searchQuery = if (tab.isHome) "" else tab.url

    val barBackgroundColor = if (isDarkMode) Color(0xFF0F0E17) else Color(0xFFFFFFFF)
    val searchBoxBackground = if (isDarkMode) Color(0xFF1F1D2B) else Color(0xFFF1F3F4)
    val primaryTextColor = if (isDarkMode) Color.White else Color(0xFF202124)
    val hintTextColor = if (isDarkMode) Color(0xFFA0A0B2) else Color(0xFF5F6368)
    val iconTint = if (isDarkMode) Color.White else Color(0xFF3C4043)
    val desktopBadgeColor = Color(0xFF6C5CE7)
    val googleBadgeColor = Color(0xFF4285F4)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(barBackgroundColor)
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Desktop टॉगल बैज
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (tab.isDesktopMode) desktopBadgeColor else Color.Gray,
            modifier = Modifier
                .height(38.dp)
                .clickable { viewModel.toggleDesktopMode() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (tab.isDesktopMode) "DESKTOP" else "MOBILE",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // सर्च बार (Google सर्च बॉक्स)
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = searchBoxBackground,
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clickable {
                    if (tab.isHome) {
                        viewModel.loadUrl("https://www.google.com")
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(googleBadgeColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "G",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (searchQuery.isNotEmpty()) searchQuery else "सर्च करें (Google)...",
                    color = if (searchQuery.isNotEmpty()) primaryTextColor else hintTextColor,
                    fontSize = 13.sp,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = hintTextColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // रीफ्रेश बटन
        IconButton(
            onClick = { viewModel.reload() },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = iconTint
            )
        }
    }
}
