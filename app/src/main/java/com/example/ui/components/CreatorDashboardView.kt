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
    
