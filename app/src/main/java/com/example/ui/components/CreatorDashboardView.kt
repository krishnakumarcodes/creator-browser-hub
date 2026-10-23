package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.BrowserViewModel

// Neon & Cyber Theme Palette
val CyberBackground = Color(0xFF070714)
val CardBackground = Color(0xFF13132B).copy(alpha = 0.85f)
val NeonPurpleBorder = Color(0xFFA855F7)
val NeonPink = Color(0xFFEC4899)
val NeonCyan = Color(0xFF06B6D4)
val NeonGreen = Color(0xFF10B981)
val NeonOrange = Color(0xFFF97316)
val GlowCardBg = Color(0xFF1A1635)

@Composable
fun CreatorDashboardView(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0B0820),
                        Color(0xFF050510),
                        Color(0xFF090A1A)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Cyber Search Bar
            item {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = Color(0xFF161530).copy(alpha = 0.9f),
                    border = BorderStroke(
                        1.5.dp,
                        Brush.horizontalGradient(
                            listOf(Color(0xFF8B5CF6), Color(0xFFEC4899), Color(0xFF06B6D4))
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF232247),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "G",
                                    color = Color(0xFF4285F4),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Search Web...",
                            color = Color(0xFF8E8EA8),
                            fontSize = 15.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Search",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // 2. Creator Nexus Dashboard Main Card
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = GlowCardBg.copy(alpha = 0.9f),
                    border = BorderStroke(
                        1.5.dp,
                        Brush.linearGradient(
                            listOf(Color(0xFF9333EA), Color(0xFF06B6D4), Color(0xFFEC4899))
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Creator Nexus",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )

                        Text(
                            text = "DAILY CREATION FLOW: 4.2 HRS",
                            color = NeonPink,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )

                        Text(
                            text = "PROJECTS ACTIVE: 3",
                            color = NeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )

                        Text(
                            text = "INSPIRATION BLOCKS CLEARED: 12",
                            color = NeonGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Category Folders (WORKSPACE, COMMUNITY, ASSETS)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FolderChip(
                                title = "WORKSPACE",
                                borderColor = NeonPurpleBorder,
                                icons = listOf("F", "N", "V"),
                                modifier = Modifier.weight(1f)
                            )
                            FolderChip(
                                title = "COMMUNITY",
                                borderColor = NeonCyan,
                                icons = listOf("D", "S", "G"),
                                modifier = Modifier.weight(1f)
                            )
                            FolderChip(
                                title = "ASSETS",
                                borderColor = NeonOrange,
                                icons = listOf("D", "B", "C"),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 3. Quick Access Circular Shortcuts (WhatsApp, YouTube, Spotify, Add New)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CardBackground,
                    border = BorderStroke(1.dp, Color(0xFF2C2754)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        NeonCircularShortcut(
                            name = "WhatsApp",
                            symbol = "W",
                            glowColor = NeonGreen,
                            onClick = { viewModel.loadUrl("https://web.whatsapp.com") }
                        )
                        NeonCircularShortcut(
                            name = "YouTube",
                            symbol = "Y",
                            glowColor = Color(0xFFFF0033),
                            onClick = { viewModel.loadUrl("https://youtube.com") }
                        )
                        NeonCircularShortcut(
                            name = "Spotify",
                            symbol = "S",
                            glowColor = Color(0xFF1DB954),
                            onClick = { viewModel.loadUrl("https://open.spotify.com") }
                        )
                        NeonCircularShortcut(
                            name = "Add new",
                            symbol = "+",
                            glowColor = Color(0xFFA855F7),
                            onClick = { }
                        )
                    }
                }
            }

            // 4. Two-Column Project Cards Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProjectCard(
                        title = "Github Repo\nRework",
                        badge = "GH",
                        accentColor = Color.White,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.loadUrl("https://github.com") }
                    )
                    ProjectCard(
                        title = "Figma\nWireframes",
                        badge = "FG",
                        accentColor = NeonPink,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.loadUrl("https://figma.com") }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProjectCard(
                        title = "Unsplash Curated\nPhotos",
                        badge = "UN",
                        accentColor = NeonCyan,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.loadUrl("https://unsplash.com") }
                    )
                    ProjectCard(
                        title = "Notion Content\nCalendar",
                        badge = "NO",
                        accentColor = Color(0xFFE2E8F0),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.loadUrl("https://notion.so") }
                    )
                }
            }
        }
    }
}

@Composable
fun FolderChip(
    title: String,
    borderColor: Color,
    icons: List<String>,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF0F0E24),
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.7f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 9.sp,
                color = borderColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icons.forEach { iconLetter ->
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFF1F1D3D),
                        modifier = Modifier.size(16.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = iconLetter,
                                fontSize = 8.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NeonCircularShortcut(
    name: String,
    symbol: String,
    glowColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFF171533),
            border = BorderStroke(2.dp, glowColor),
            modifier = Modifier.size(54.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = symbol,
                    color = glowColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = name,
            color = Color(0xFFC7C5E0),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProjectCard(
    title: String,
    badge: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, Color(0xFF2E2A54)),
        modifier = modifier
            .height(64.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF221F45),
                border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = badge,
                        color = accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 13.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = Color(0xFF75719B),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
