package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.BrowserViewModel

// 🔮 असली साइबरपंक / नियॉन कलर पैलेट
val CyberBgDark = Color(0xFF07040D)
val CyberGlassBg = Color(0xCC140E24)
val NeonPink = Color(0xFFFF2A85)
val NeonPurple = Color(0xFFBD00FF)
val NeonCyan = Color(0xFF00F0FF)
val NeonOrange = Color(0xFFFF9900)
val NeonGreen = Color(0xFF00FF66)
val TextMuted = Color(0xFF8E8EA0)

@Composable
fun CreatorDashboardView(
    modifier: Modifier = Modifier,
    viewModel: BrowserViewModel? = null,
    onNavigateToUrl: (String) -> Unit = { url -> viewModel?.loadUrl(url) },
    isDark: Boolean = isSystemInDarkTheme()
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBgDark)
    ) {
        // 1. बैकग्राउंड साइबर सर्किट लाइन्स (Circuit Wallpaper Effect)
        CyberCircuitBackground()

        // 2. मुख्य स्क्रोल करने योग्य कंटेंट
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // [A] टॉप Creator Nexus कार्ड (तीनों फोल्डर सहित)
            CreatorNexusMasterCard()

            Spacer(modifier = Modifier.height(18.dp))

            // [B] 4 नियॉन सर्कल्स (WhatsApp, YouTube, Spotify, Add new)
            NeonAppsHorizontalBar(onNavigateToUrl = onNavigateToUrl)

            Spacer(modifier = Modifier.height(18.dp))

            // [C] 2x2 कैप्सूल टास्क कार्ड्स (Pill Shape)
            PillTaskCardsGrid2x2(onNavigateToUrl = onNavigateToUrl)

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// 🌐 बैकग्राउंड साइबर सर्किट (इमेज 2 जैसा लुक देने के लिए)
@Composable
private fun CyberCircuitBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // बैकग्राउंड पर्पल और सियान ग्लो सर्कल्स
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(NeonPurple.copy(alpha = 0.22f), Color.Transparent),
                center = Offset(w * 0.2f, h * 0.25f),
                radius = 450f
            )
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(NeonCyan.copy(alpha = 0.18f), Color.Transparent),
                center = Offset(w * 0.8f, h * 0.55f),
                radius = 400f
            )
        )

        // सर्किट ग्रिड लाइन्स और नोड्स
        val lineColor = NeonCyan.copy(alpha = 0.08f)
        drawLine(lineColor, Offset(w * 0.1f, 0f), Offset(w * 0.1f, h), strokeWidth = 1f)
        drawLine(lineColor, Offset(w * 0.9f, 0f), Offset(w * 0.9f, h), strokeWidth = 1f)
        drawLine(lineColor, Offset(0f, h * 0.35f), Offset(w, h * 0.35f), strokeWidth = 1f)
        drawLine(lineColor, Offset(0f, h * 0.65f), Offset(w, h * 0.65f), strokeWidth = 1f)

        // कोने के सर्किट नोड्स
        drawCircle(NeonPurple.copy(alpha = 0.25f), radius = 6f, center = Offset(w * 0.1f, h * 0.35f))
        drawCircle(NeonCyan.copy(alpha = 0.25f), radius = 6f, center = Offset(w * 0.9f, h * 0.65f))
    }
}

// [A] टॉप Creator Nexus कार्ड
@Composable
private fun CreatorNexusMasterCard() {
    val multiGradientBorder = Brush.sweepGradient(
        listOf(NeonPurple, NeonCyan, NeonPink, NeonPurple)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(28.dp), ambientColor = NeonPurple, spotColor = NeonCyan)
            .clip(RoundedCornerShape(28.dp))
            .background(CyberGlassBg)
            .border(2.dp, multiGradientBorder, RoundedCornerShape(28.dp))
            .padding(18.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Creator Nexus",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "DAILY CREATION FLOW: 4.2 HRS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonPink,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "PROJECTS ACTIVE: 3",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "INSPIRATION BLOCKS CLEARED: 12",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonGreen,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // तीन फोल्डर्स: WORKSPACE, COMMUNITY, ASSETS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FolderItemBox(
                    title = "WORKSPACE",
                    borderColor = NeonPurple,
                    icons = listOf("F", "N", "VS"),
                    modifier = Modifier.weight(1f)
                )
                FolderItemBox(
                    title = "COMMUNITY",
                    borderColor = NeonCyan,
                    icons = listOf("D", "S", "GH"),
                    modifier = Modifier.weight(1f)
                )
                FolderItemBox(
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
private fun FolderItemBox(
    title: String,
    borderColor: Color,
    icons: List<String>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = borderColor)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xE60E0B1A))
            .border(1.5.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp)
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
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = borderColor,
                    modifier = Modifier.size(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icons.forEach { iconText ->
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFF221A38))
                            .border(0.5.dp, borderColor.copy(alpha = 0.4f), RoundedCornerShape(7.dp)),
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

// [B] चार नियॉन राउंड शॉर्टकट्स (ग्लास कंटेनर में)
@Composable
private fun NeonAppsHorizontalBar(onNavigateToUrl: (String) -> Unit) {
    val barBorder = Brush.horizontalGradient(
        listOf(NeonPurple.copy(alpha = 0.7f), NeonCyan.copy(alpha = 0.7f))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(26.dp), ambientColor = NeonCyan)
            .clip(RoundedCornerShape(26.dp))
            .background(CyberGlassBg)
            .border(1.8.dp, barBorder, RoundedCornerShape(26.dp))
            .padding(vertical = 14.dp, horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeonRoundApp(
                name = "WhatsApp",
                symbol = "W",
                glowColor = NeonGreen,
                onClick = { onNavigateToUrl("https://web.whatsapp.com") }
            )
            NeonRoundApp(
                name = "YouTube",
                symbol = "▶",
                glowColor = Color(0xFFFF0033),
                onClick = { onNavigateToUrl("https://www.youtube.com") }
            )
            NeonRoundApp(
                name = "Spotify",
                symbol = "≈",
                glowColor = Color(0xFF1DB954),
                onClick = { onNavigateToUrl("https://open.spotify.com") }
            )
            NeonRoundApp(
                name = "Add new",
                symbol = "+",
                glowColor = NeonPurple,
                onClick = { /* New shortcut */ }
            )
        }
    }
}

@Composable
private fun NeonRoundApp(
    name: String,
    symbol: String,
    glowColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(12.dp, CircleShape, spotColor = glowColor)
                .clip(CircleShape)
                .background(Color(0xFF0F0B1E))
                .border(2.5.dp, glowColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = symbol,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = glowColor
            )
        }
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

// [C] 2x2 कैप्सूल टास्क कार्ड्स (Pill Shape — 100% Round Borders)
@Composable
private fun PillTaskCardsGrid2x2(onNavigateToUrl: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PillTaskCard(
                title = "Github Repo",
                subtitle = "Rework",
                symbol = "GH",
                borderColor = NeonCyan,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToUrl("https://github.com/krishnakumarcodes") }
            )
            PillTaskCard(
                title = "Figma",
                subtitle = "Wireframes",
                symbol = "F",
                borderColor = NeonPurple,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToUrl("https://www.figma.com") }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PillTaskCard(
                title = "Unsplash Curated",
                subtitle = "Photos",
                symbol = "U",
                borderColor = NeonGreen,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToUrl("https://unsplash.com") }
            )
            PillTaskCard(
                title = "Notion Content",
                subtitle = "Calendar",
                symbol = "N",
                borderColor = NeonOrange,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToUrl("https://www.notion.so") }
            )
        }
    }
}

@Composable
private fun PillTaskCard(
    title: String,
    subtitle: String,
    symbol: String,
    borderColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // पूर्ण कैप्सूल शेप (CircleShape / 50% radius)
    Box(
        modifier = modifier
            .shadow(10.dp, CircleShape, spotColor = borderColor)
            .clip(CircleShape)
            .background(CyberGlassBg)
            .border(1.8.dp, borderColor, CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // गोल सिंबल बॉक्स
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F0B1E))
                    .border(1.2.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = symbol,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = borderColor
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = borderColor.copy(alpha = 0.7f),
                modifier = Modifier.size(13.dp)
            )
        }
    }
}
