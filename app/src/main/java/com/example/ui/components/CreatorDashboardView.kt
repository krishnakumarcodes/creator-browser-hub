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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel

data class CyberAppItem(
    val id: String,
    val name: String,
    val url: String,
    val initial: String,
    val color: Color
)

data class CyberProjectItem(
    val title: String,
    val url: String,
    val initial: String,
    val color: Color
)

data class ToolkitItem(
    val initial: String,
    val url: String,
    val color: Color
)

data class ToolkitSection(
    val title: String,
    val borderColor: Color,
    val items: List<ToolkitItem>
)

@Composable
fun CreatorDashboardView(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val cyberBg = Color(0xFF08071A)
    val cyberCardBg = Color(0xE6121026)
    val neonPink = Color(0xFFFF2E93)
    val neonCyan = Color(0xFF00F0FF)
    val neonGreen = Color(0xFF10FFA0)
    val neonAmber = Color(0xFFFFB800)
    val neonPurple = Color(0xFF9D4EDD)

    val cyberGradient = Brush.horizontalGradient(
        listOf(neonPurple, neonPink, neonCyan, neonGreen)
    )

    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newUrl by remember { mutableStateOf("") }

    var favoriteApps by remember {
        mutableStateOf(
            listOf(
                CyberAppItem("whatsapp", "WhatsApp", "https://web.whatsapp.com", "W", Color(0xFF25D366)),
                CyberAppItem("youtube", "YouTube", "https://www.youtube.com", "Y", Color(0xFFFF0000)),
                CyberAppItem("spotify", "Spotify", "https://open.spotify.com", "S", Color(0xFF1DB954))
            )
        )
    }

    val toolkitSections = remember {
        listOf(
            ToolkitSection(
                title = "WORKSPACE",
                borderColor = neonPink,
                items = listOf(
                    ToolkitItem("F", "https://www.figma.com", Color(0xFFA259FF)),
                    ToolkitItem("N", "https://www.notion.so", Color(0xFF000000)),
                    ToolkitItem("V", "https://vscode.dev", Color(0xFF007ACC))
                )
            ),
            ToolkitSection(
                title = "COMMUNITY",
                borderColor = neonCyan,
                items = listOf(
                    ToolkitItem("D", "https://discord.com", Color(0xFF5865F2)),
                    ToolkitItem("S", "https://slack.com", Color(0xFFE01E5A)),
                    ToolkitItem("G", "https://github.com", Color(0xFF24292E))
                )
            ),
            ToolkitSection(
                title = "ASSETS",
                borderColor = neonAmber,
                items = listOf(
                    ToolkitItem("D", "https://drive.google.com", Color(0xFF0F9D58)),
                    ToolkitItem("B", "https://www.dropbox.com", Color(0xFF0061FF)),
                    ToolkitItem("C", "https://chrome.google.com", Color(0xFFEA4335))
                )
            )
        )
    }

    val projectPills = remember {
        listOf(
            CyberProjectItem("Github Repo Rework", "https://github.com", "G", Color(0xFF24292E)),
            CyberProjectItem("Figma Wireframes", "https://www.figma.com", "F", Color(0xFFA259FF)),
            CyberProjectItem("Unsplash Curated Photos", "https://unsplash.com", "U", Color(0xFF111111)),
            CyberProjectItem("Notion Content Calendar", "https://www.notion.so", "N", Color(0xFF000000))
        )
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(
                    text = "नया शॉर्टकट जोड़ें",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("नाम") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newUrl,
                        onValueChange = { newUrl = it },
                        label = { Text("URL") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank() && newUrl.isNotBlank()) {
                            val cleanUrl = if (newUrl.startsWith("http")) {
                                newUrl.trim()
                            } else {
                                "https://${newUrl.trim()}"
                            }
                            val newItem = CyberAppItem(
                                id = "custom_${System.currentTimeMillis()}",
                                name = newName.trim(),
                                url = cleanUrl,
                                initial = newName.trim().take(1).uppercase(),
                                color = neonCyan
                            )
                            favoriteApps = favoriteApps + newItem
                            newName = ""
                            newUrl = ""
                            showAddDialog = false
                            viewModel.showMessage("${newItem.name} होम स्क्रीन पर जुड़ गया!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = neonPurple)
                ) {
                    Text("जोड़ें", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("रद्द करें", color = TextSecondary)
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(cyberBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CreatorSearchBar(
                cyberCardBg = cyberCardBg,
                cyberGradient = cyberGradient,
                onSearchClick = { viewModel.loadUrl("https://www.google.com") },
                onVoiceClick = { viewModel.showMessage("वॉइस सर्च सक्रिय है") }
            )
        }

        item {
            CreatorNexusCard(
                cyberCardBg = cyberCardBg,
                cyberGradient = cyberGradient,
                neonPink = neonPink,
                neonCyan = neonCyan,
                neonGreen = neonGreen,
                toolkitSections = toolkitSections,
                onToolClick = { url -> viewModel.loadUrl(url) }
            )
        }

        item {
            QuickAppsCard(
                cyberCardBg = cyberCardBg,
                cyberGradient = cyberGradient,
                neonPurple = neonPurple,
                neonPink = neonPink,
                apps = favoriteApps.take(3),
                onAppClick = { url -> viewModel.loadUrl(url) },
                onAddClick = { showAddDialog = true }
            )
        }

        item {
            ProjectPillsGrid(
                cyberCardBg = cyberCardBg,
                cyberGradient = cyberGradient,
                projects = projectPills,
                onProjectClick = { url -> viewModel.loadUrl(url) }
            )
        }
    }
}

@Composable
private fun CreatorSearchBar(
    cyberCardBg: Color,
    cyberGradient: Brush,
    onSearchClick: () -> Unit,
    onVoiceClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(27.dp))
            .background(cyberCardBg)
            .border(
                border = BorderStroke(1.5.dp, cyberGradient),
                shape = RoundedCornerShape(27.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSearchClick() }
                    .padding(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4285F4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = "Search Web...",
                color = Color(0xFF7A789E),
                fontSize = 15.sp,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSearchClick() }
            )

            IconButton(
                onClick = onVoiceClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun CreatorNexusCard(
    cyberCardBg: Color,
    cyberGradient: Brush,
    neonPink: Color,
    neonCyan: Color,
    neonGreen: Color,
    toolkitSections: List<ToolkitSection>,
    onToolClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cyberCardBg),
        border = BorderStroke(1.5.dp, cyberGradient)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Creator Nexus",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "DAILY CREATION FLOW: 4.2 HRS",
                    color = neonPink,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "PROJECTS ACTIVE: 3",
                    color = neonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "INSPIRATION BLOCKS CLEARED: 12",
                    color = neonGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                toolkitSections.forEach { section ->
                    ToolkitBox(
                        section = section,
                        onToolClick = onToolClick
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.ToolkitBox(
    section: ToolkitSection,
    onToolClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF16132D))
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = section.borderColor.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = section.title,
                color = section.borderColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                section.items.forEach { tool ->
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(tool.color)
                            .clickable { onToolClick(tool.url) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tool.initial,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickAppsCard(
    cyberCardBg: Color,
    cyberGradient: Brush,
    neonPurple: Color,
    neonPink: Color,
    apps: List<CyberAppItem>,
    onAppClick: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cyberCardBg),
        border = BorderStroke(1.2.dp, cyberGradient)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            apps.forEach { app ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onAppClick(app.url) }
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF16132D))
                            .border(
                                border = BorderStroke(1.8.dp, app.color),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = app.initial,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Text(
                        text = app.name,
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onAddClick() }
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E1736))
                        .border(
                            border = BorderStroke(1.5.dp, neonPurple),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new",
                        tint = neonPink,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Add new",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ProjectPillsGrid(
    cyberCardBg: Color,
    cyberGradient: Brush,
    projects: List<CyberProjectItem>,
    onProjectClick: (String) -> Unit
) {
    if (projects.size < 4) return
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProjectPillItem(
                project = projects[0],
                cyberCardBg = cyberCardBg,
                cyberGradient = cyberGradient,
                onClick = { onProjectClick(projects[0].url) }
            )
            ProjectPillItem(
                project = projects[1],
                cyberCardBg = cyberCardBg,
                cyberGradient = cyberGradient,
                onClick = { onProjectClick(projects[1].url) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            hor
