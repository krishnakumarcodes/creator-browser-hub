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

data class CyberAppItem(val id: String, val name: String, val url: String, val initial: String, val color: Color)
data class CyberProjectItem(val title: String, val url: String, val initial: String, val color: Color)
data class ToolkitItem(val initial: String, val url: String, val color: Color)
data class ToolkitSection(val title: String, val borderColor: Color, val items: List<ToolkitItem>)

@Composable
fun CreatorDashboardView(viewModel: BrowserViewModel, modifier: Modifier = Modifier) {
    val cyberBg = Color(0xFF08071A)
    val cyberCardBg = Color(0xE6121026)
    val neonPink = Color(0xFFFF2E93)
    val neonCyan = Color(0xFF00F0FF)
    val neonGreen = Color(0xFF10FFA0)
    val neonAmber = Color(0xFFFFB800)
    val neonPurple = Color(0xFF9D4EDD)

    val cyberGradient = Brush.horizontalGradient(listOf(neonPurple, neonPink, neonCyan, neonGreen))

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
            ToolkitSection("WORKSPACE", neonPink, listOf(
                ToolkitItem("F", "https://www.figma.com", Color(0xFFA259FF)),
                ToolkitItem("N", "https://www.notion.so", Color(0xFF000000)),
                ToolkitItem("V", "https://vscode.dev", Color(0xFF007ACC))
            )),
            ToolkitSection("COMMUNITY", neonCyan, listOf(
                ToolkitItem("D", "https://discord.com", Color(0xFF5865F2)),
                ToolkitItem("S", "https://slack.com", Color(0xFFE01E5A)),
                ToolkitItem("G", "https://github.com", Color(0xFF24292E))
            )),
            ToolkitSection("ASSETS", neonAmber, listOf(
                ToolkitItem("D", "https://drive.google.com", Color(0xFF0F9D58)),
                ToolkitItem("B", "https://www.dropbox.com", Color(0xFF0061FF)),
                ToolkitItem("C", "https://chrome.google.com", Color(0xFFEA4335))
            ))
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
            title = { Text("नया शॉर्टकट जोड़ें", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("नाम") }, singleLine = true)
                    OutlinedTextField(value = newUrl, onValueChange = { newUrl = it }, label = { Text("URL") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank() && newUrl.isNotBlank()) {
                            val cleanUrl = if (newUrl.startsWith("http")) newUrl.trim() else "https://${newUrl.trim()}"
                            val newItem = CyberAppItem("c_${System.currentTimeMillis()}", newName.trim(), cleanUrl, newName.trim().take(1).uppercase(), neonCyan)
                            favoriteApps = favoriteApps + newItem
                            newName = ""
                            newUrl = ""
                            showAddDialog = false
                            viewModel.showMessage("${newItem.name} होम स्क्रीन पर जुड़ गया!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = neonPurple)
                ) { Text("जोड़ें", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("रद्द करें", color = TextSecondary) }
            }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(cyberBg).padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Search Bar
        item {
            Box(
                modifier = Modifier.fillMaxWidth().height(54.dp).clip(RoundedCornerShape(27.dp))
                    .background(cyberCardBg).border(BorderStroke(1.5.dp, cyberGradient), RoundedCornerShape(27.dp)).padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { viewModel.loadUrl("https://www.google.com") }.padding(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFF4285F4)), contentAlignment = Alignment.Center) {
                                Text("G", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }
                    }
                    Text("Search Web...", color = Color(0xFF7A789E), fontSize = 15.sp, modifier = Modifier.weight(1f).clickable { viewModel.loadUrl("https://www.google.com") })
                    IconButton(onClick = { viewModel.showMessage("वॉइस सर्च सक्रिय है") }, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Mic, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(22.dp))
                    }
                }
            }
        }

        // 2. Creator Nexus Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cyberCardBg),
                border = BorderStroke(1.5.dp, cyberGradient)
            ) {
                Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Creator Nexus", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("DAILY CREATION FLOW: 4.2 HRS", color = neonPink, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                        Text("PROJECTS ACTIVE: 3", color = neonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                        Text("INSPIRATION BLOCKS CLEARED: 12", color = neonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        toolkitSections.forEach { sec ->
                            Box(
                                modifier = Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(Color(0xFF16132D))
                                    .border(BorderStroke(1.dp, sec.borderColor.copy(alpha = 0.8f)), RoundedCornerShape(14.dp)).padding(horizontal = 6.dp, vertical = 8.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(sec.title, color = sec.borderColor, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                        sec.items.forEach { t ->
                                            Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(t.color).clickable { viewModel.loadUrl(t.url) }, contentAlignment = Alignment.Center) {
                                                Text(t.initial, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Quick Action Apps
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = cyberCardBg),
                border = BorderStroke(1.2.dp, cyberGradient)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    favoriteApps.take(3).forEach { app ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { viewModel.loadUrl(app.url) }.padding(4.dp)) {
                            Box(modifier = Modifier.size(54.dp).clip(CircleShape).background(Color(0xFF16132D)).border(BorderStroke(1.8.dp, app.color), CircleShape), contentAlignment = Alignment.Center) {
                                Text(app.initial, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                            }
                            Text(app.name, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { showAddDialog = true }.padding(4.dp)) {
                        Box(modifier = Modifier.size(54.dp).clip(CircleShape).background(Color(0xFF1E1736)).border(BorderStroke(1.5.dp, neonPurple), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Add, contentDescription = "Add new", tint = neonPink, modifier = Modifier.size(28.dp))
                        }
                        Text("Add new", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // 4. Project Pills (2x2 Grid)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier.weight(1f).height(52.dp).clip(RoundedCornerShape(20.dp)).background(cyberCardBg)
                            .border(BorderStroke(1.2.dp, cyberGradient), RoundedCornerShape(20.dp)).clickable { viewModel.loadUrl(projectPills[0].url) }.padding(horizontal = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(projectPills[0].color), contentAlignment = Alignment.Center) {
                                Text(projectPills[0].initial, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(projectPills[0].title, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                        }
                    }
                    Box(
                        modifier = Modifier.weight(1f).height(52.dp).clip(RoundedCornerShape(20.dp)).background(cyberCardBg)
                            .border(BorderStroke(1.2.dp, cyberGradient), RoundedCornerShape(20.dp)).clickable { viewModel.loadUrl(projectPills[1].url) }.padding(horizontal = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(projectPills[1].color), contentAlignment = Alignment.Center) {
                                Text(projectPills[1].initial, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(projectPills[1].title, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier.weight(1f).height(52.dp).clip(RoundedCornerShape(20.dp)).background(cyberCardBg)
                            .border(BorderStroke(1.2.dp, cyberGradient), RoundedCornerShape(20.dp)).clickable { viewModel.loadUrl(projectPills[2].url) }.padding(horizontal = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(projectPills[2].color), contentAlignment = Alignment.Center) {
                                Text(projectPills[2].initial, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(projectPills[2].title, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                        }
                    }
                    Box(
                        modifier = Modifier.weight(1f).height(52.dp).clip(RoundedCornerShape(20.dp)).background(cyberCardBg)
                            .border(BorderStroke(1.2.dp, cyberGradient), RoundedCornerShape(20.dp)).clickable { viewModel.loadUrl(projectPills[3].url) }.padding(horizontal = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(projectPills[3].color), contentAlignment = Alignment.Center) {
                                Text(projectPills[3].initial, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(projectPills[3].title, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
