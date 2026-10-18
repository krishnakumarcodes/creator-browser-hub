package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel

// 🎨 साइबरपंक नियॉन कलर्स और ग्रेडिएंट
private val CyberBg = Color(0xFF08071A)
private val CyberCardBg = Color(0xE6121026)
private val NeonPink = Color(0xFFFF2E93)
private val NeonCyan = Color(0xFF00F0FF)
private val NeonGreen = Color(0xFF10FFA0)
private val NeonAmber = Color(0xFFFFB800)
private val NeonPurple = Color(0xFF9D4EDD)

private val CyberGradient = Brush.horizontalGradient(
    listOf(NeonPurple, NeonPink, NeonCyan, NeonGreen)
)

data class QuickActionItem(
    val id: String,
    val name: String,
    val url: String,
    val initial: String,
    val glowColor: Color
)

data class ProjectPillItem(
    val title: String,
    val url: String,
    val iconInitial: String,
    val iconBgColor: Color
)

data class MiniApp(
    val initial: String,
    val name: String,
    val url: String,
    val color: Color
)

@Composable
fun CreatorDashboardView(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newShortcutName by remember { mutableStateOf("") }
    var newShortcutUrl by remember { mutableStateOf("") }

    // 🟢 पसंदीदा ऐप्स लिस्ट (WhatsApp, YouTube, Spotify)
    var favoriteApps by remember {
        mutableStateOf(
            listOf(
                QuickActionItem("whatsapp", "WhatsApp", "https://web.whatsapp.com", "W", Color(0xFF25D366)),
                QuickActionItem("youtube", "YouTube", "https://www.youtube.com", "Y", Color(0xFFFF0000)),
                QuickActionItem("spotify", "Spotify", "https://open.spotify.com", "S", Color(0xFF1DB954))
            )
        )
    }

    // 🟢 2-कॉलम प्रोजेक्ट पिल्स (Github, Figma, Unsplash, Notion)
    val projectPills = remember {
        listOf(
            ProjectPillItem("Github Repo Rework", "https://github.com", "G", Color(0xFF24292E)),
            ProjectPillItem("Figma Wireframes", "https://www.figma.com", "F", Color(0xFFA259FF)),
            ProjectPillItem("Unsplash Curated Photos", "https://unsplash.com", "U", Color(0xFF111111)),
            ProjectPillItem("Notion Content Calendar", "https://www.notion.so", "N", Color(0xFF000000))
        )
    }

    // ➕ कस्टम शॉर्टकट जोड़ने का डायलॉग
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
                        value = newShortcutName,
                        onValueChange = { newShortcutName = it },
                        label = { Text("नाम (उदा. Twitter / X)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                    OutlinedTextField(
                        value = newShortcutUrl,
                        onValueChange = { newShortcutUrl = it },
                        label = { Text("URL (उदा. x.com)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newShortcutName.isNotBlank() && newShortcutUrl.isNotBlank()) {
                            val cleanUrl = if (newShortcutUrl.startsWith("http://") || newShortcutUrl.startsWith("https://")) {
                                newShortcutUrl.trim()
                            } else {
                                "https://${newShortcutUrl.trim()}"
                            }
                            val newItem = QuickActionItem(
                                id = "custom_${System.currentTimeMillis()}",
                                name = newShortcutName.trim(),
                                url = cleanUrl,
                                initial = newShortcutName.trim().take(1).uppercase(),
                                glowColor = NeonCyan
                            )
                            favoriteApps = favoriteApps + newItem
                            newShortcutName = ""
                            newShortcutUrl = ""
                            showAddDialog = false
                            viewModel.showMessage("${newItem.name} होम स्क्रीन पर जुड़ गया!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
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
            .background(CyberBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. टॉप सर्च बार (Google G + Search Web + Mic)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                color = CyberCardBg,
                border = BorderStroke(1.5.dp, CyberGradient)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.loadUrl("https://www.google.com") }
                            .padding(4.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF4285F4),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "G",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Search Selector",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = "Search Web...",
                        color = Color(0xFF7A789E),
                        fontSize = 15.sp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.loadUrl("https://www.google.com") }
                    )

                    IconButton(
                        onClick = { viewModel.showMessage("वॉइस सर्च सक्रिय है") },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Search",
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // 2. मुख्य "Creator Nexus" कार्ड (Stats + Workspace, Community, Assets)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCardBg),
                border = BorderStroke(1.5.dp, CyberGradient)
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
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CategoryBox(
                            title = "WORKSPACE",
                            borderColor = NeonPink,
                            modifier = Modifier.weight(1f),
                            apps = listOf(
                                MiniApp("F", "Figma", "https://www.figma.com", Color(0xFFA259FF)),
                                MiniApp("N", "Notion", "https://www.notion.so", Color(0xFF000000)),
                                MiniApp("V", "VS Code", "https://vscode.dev", Color(0xFF007ACC))
                            ),
                            onAppClick = { viewModel.loadUrl(it) }
                        )

                        CategoryBox(
                            title = "COMMUNITY",
                            borderColor = NeonCyan,
                            modifier = Modifier.weight(1f),
                            apps = listOf(
                                MiniApp("D", "Discord", "https://discord.com", Color(0xFF5865F2)),
                                MiniApp("S", "Slack", "https://slack.com", Color(0xFFE01E5A)),
                                MiniApp("G", "GitHub", "https://github.com", Color(0xFF24292E))
                            ),
                            onAppClick = { viewModel.loadUrl(it) }
                        )

                        CategoryBox(
                            title = "ASSETS",
                            borderColor = NeonAmber,
                            modifier = Modifier.weight(1f),
                            apps = listOf(
                                MiniApp("D", "Drive", "https://drive.google.com", Color(0xFF0F9D58)),
                                MiniApp("B", "Dropbox", "https://www.dropbox.com", Color(0xFF0061FF)),
                                MiniApp("C", "Chrome", "https://chrome.google.com", Color(0xFFEA4335))
                            ),
                            onAppClick = { viewModel.loadUrl(it) }
                        )
                    }
                }
            }
        }

        // 3. क्विक एक्शन सर्कल्स (WhatsApp, YouTube, Spotify, Add new)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCardBg),
                border = BorderStroke(1.2.dp, CyberGradient)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    favoriteApps.take(3).forEach { app ->
                        CyberCircleApp(
                            name = app.name,
                            initial = app.initial,
                            glowColor = app.glowColor,
                            onClick = { viewModel.loadUrl(app.url) }
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showAddDialog = true }
                            .padding(4.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF1E1736),
                            border = BorderStroke(1.5.dp, NeonPurple),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add new",
                                    tint = NeonPink,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
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

        // 4. प्रोजेक्ट्स पिल्स (2-कॉलम: GitHub, Figma, Unsplash, Notion)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                projectPills.chunked(2).forEach { rowPills ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowPills.forEach { pill ->
                            CyberProjectPill(
                                title = pill.title,
                                iconInitial = pill.iconInitial,
                                iconBgColor = pill.iconBgColor,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.loadUrl(pill.url) }
                            )
                        }
                        if (rowPills.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryBox(
    title: String,
    borderColor: Color,
    apps: List<MiniApp>,
    modifier: Modifier = Modifier,
    onAppClick: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF16132D),
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.8f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    color = borderColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = borderColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(10.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = borderColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(12.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                apps.forEach { app ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = app.color,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            
