package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.BrowserViewModel

// नियॉन कलर पैलेट
val NeonPurple = Color(0xFFBD00FF)
val NeonCyan = Color(0xFF00F0FF)
val NeonOrange = Color(0xFFFF9900)
val NeonGreen = Color(0xFF00FF66)
val DarkCardBg = Color(0xFF13111C)
val SubtitleGray = Color(0xFF8E8EA0)

@Composable
fun CreatorDashboardView(
    modifier: Modifier = Modifier,
    viewModel: BrowserViewModel? = null,
    onNavigateToUrl: (String) -> Unit = { url -> viewModel?.loadUrl(url) },
    isDark: Boolean = isSystemInDarkTheme()
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF07050E)) // डीप साइबरपंक डार्क बैकग्राउंड
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. मुख्य Creator Nexus कार्ड
        CreatorNexusStatsCard()

        Spacer(modifier = Modifier.height(18.dp))

        // 2. 4 नियॉन सर्कल्स (WhatsApp, YouTube, Spotify, Add new)
        QuickNeonAppsRow(onNavigateToUrl = onNavigateToUrl)

        Spacer(modifier = Modifier.height(18.dp))

        // 3. 2x2 टास्क कार्ड्स
        CreatorTaskGrid2x2(onNavigateToUrl = onNavigateToUrl)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// 1. टॉप Creator Nexus और तीनों फोल्डर (Workspace, Community, Assets)
@Composable
private fun CreatorNexusStatsCard() {
    val gradientBorder = Brush.linearGradient(
        colors = listOf(NeonPurple.copy(alpha = 0.8f), NeonCyan.copy(alpha = 0.5f), NeonPurple.copy(alpha = 0.8f))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(DarkCardBg)
            .border(1.5.dp, gradientBorder, RoundedCornerShape(26.dp))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Creator Nexus",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "DAILY CREATION FLOW: 4.2 HRS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF2A85),
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "PROJECTS ACTIVE: 3",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "INSPIRATION BLOCKS CLEARED: 12",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonGreen,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // तीन फोल्डर्स: WORKSPACE, COMMUNITY, ASSETS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NexusFolderBox(
                    title = "WORKSPACE",
                    borderColor = NeonPurple,
                    icons = listOf("F", "N", "VS"),
                    modifier = Modifier.weight(1f)
                )
                NexusFolderBox(
                    title = "COMMUNITY",
                    borderColor = NeonCyan,
                    icons = listOf("D", "S", "GH"),
                    modifier = Modifier.weight(1f)
                )
                NexusFolderBox(
                    title = "ASSETS",
                    borderColor = NeonOrange,
                    icons = listOf("▲", "📦", "◎"),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NexusFolderBox(
    title: String,
    borderColor: Color,
    icons: List<String>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0D0B14))
            .border(1.2.dp, borderColor.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = borderColor
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = borderColor,
                    modifier = Modifier.size(9.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icons.forEach { iconText ->
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1E1A29)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = iconText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// 2. चार नियॉन राउंड शॉर्टकट्स
@Composable
private fun QuickNeonAppsRow(onNavigateToUrl: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(DarkCardBg)
            .border(1.2.dp, NeonCyan.copy(alpha = 0.35f), RoundedCornerShape(22.dp))
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NeonRoundApp(
                name = "WhatsApp",
                iconText = "💬",
                glowColor = NeonGreen,
                onClick = { onNavigateToUrl("https://web.whatsapp.com") }
            )
            NeonRoundApp(
                name = "YouTube",
                iconText = "▶",
                glowColor = Color(0xFFFF0033),
                onClick = { onNavigateToUrl("https://www.youtube.com") }
            )
            NeonRoundApp(
                name = "Spotify",
                iconText = "≈",
                glowColor = Color(0xFF1DB954),
                onClick = { onNavigateToUrl("https://open.spotify.com") }
            )
            NeonRoundApp(
                name = "Add new",
                iconText = "+",
                glowColor = NeonPurple,
                onClick = { /* Add custom shortcut */ }
            )
        }
    }
}

@Composable
private fun NeonRoundApp(
    name: String,
    iconText: String,
    glowColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0xFF0F0D18))
                .border(2.dp, glowColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = glowColor
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// 3. 2x2 टास्क और लिंक्स ग्रिड
@Composable
private fun CreatorTaskGrid2x2(onNavigateToUrl: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CyberpunkTaskCard(
                title = "Github Repo",
                subtitle = "Rework",
                iconText = "⌥",
                borderColor = NeonCyan,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToUrl("https://github.com/krishnakumarcodes") }
            )
            CyberpunkTaskCard(
                title = "Figma",
                subtitle = "Wireframes",
                iconText = "❖",
                borderColor = NeonPurple,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToUrl("https://www.figma.com") }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CyberpunkTaskCard(
                title = "Unsplash",
                subtitle = "Curated Photos",
                iconText = "◫",
                borderColor = NeonGreen,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToUrl("https://unsplash.com") }
            )
            CyberpunkTaskCard(
                title = "Notion Content",
                subtitle = "Calendar",
                iconText = "N",
                borderColor = NeonOrange,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToUrl("https://www.notion.so") }
            )
        }
    }
}

@Composable
private fun CyberpunkTaskCard(
    title: String,
    subtitle: String,
    iconText: String,
    borderColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(DarkCardBg)
            .border(1.2.dp, borderColor.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0F0D18))
                    .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = borderColor
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = SubtitleGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = SubtitleGray,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
