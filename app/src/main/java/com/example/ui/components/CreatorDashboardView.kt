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
                Text("नया शॉर्टकट जोड़ें", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                            val cleanUrl = if (newUrl.startsWith("http")) newUrl.trim() else "https://${newUrl.trim()}"
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
                            viewModel.showMessage("जुड़ गया!")
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
        // 1. Search Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp))
                    .background(cyberCardBg)
                    .border(BorderStroke(1.5.dp, cyberGradient), RoundedCornerShape(27.dp))
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
                            .clickable { viewModel.loadUrl("https://www.google.com") }
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
                                Text("G", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }
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
                        onClick = { viewModel.showMessage("वॉइस सर्च सक्षम है") },
                        modifier = Modifier.size(36.dp)
                    ) {
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
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Creator Nexus", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("DAILY CREATION FLOW: 4.2 HRS", color = neonPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("PROJECTS ACTIVE: 3", color = neonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("INSPIRATION BLOCKS CLEARED: 12", color = neonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // WORKSPACE
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF16132D))
                                .border(BorderStroke(1.dp, neonPink.copy(alpha = 0.8f)), RoundedCornerShape(14.dp))
                                .padding(6.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("WORKSPACE", color = neonPink, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                    Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFA259FF)).clickable { viewModel.loadUrl("https://www.figma.com") }, contentAlignment = Alignment.Center) { Text("F", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                    Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFF000000)).clickable { viewModel.loadUrl("https://www.notion.so") }, contentAlignment = Alignment.Center) { Text("N", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                    Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFF007ACC)).clickable { viewModel.loadUrl("https://vscode.dev") }, contentAlignment = Alignment.Center) { Text("V", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                }
                            }
                        }

                        // COMMUNITY
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF16132D))
                                .border(BorderStroke(1.dp, neonCyan.copy(alpha = 0.8f)), RoundedCornerShape(14.dp))
                                .padding(6.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("COMMUNITY", color = neonCyan, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                    Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFF5865F2)).clickable { viewModel.loadUrl("https://discord.com") }, contentAlignment = Alignment.Center) { Text("D", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                    Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFE01E5A)).clickable { viewModel.loadUrl("https://slack.com") }, contentAlignment = Alignment.Center) { Text("S", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                    Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFF24292E)).clickable { viewModel.loadUrl("https://github.com") }, contentAlignment = Alignment.Center) { Text("G", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                }
                            }
                        }

                        // ASSETS
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF16132D))
                                .border(BorderStroke(1.dp, neonAmber.copy(alpha = 0.8f)), RoundedCornerShape(14.dp))
                                .padding(6.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("ASSETS", color = neonAmber, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                    Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFF0F9D58)).clickable { viewModel.loadUrl("https://drive.google.com") }, contentAlignment = Alignment.Center) { Text("D", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                    Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFF0061FF)).clickable { viewModel.loadUrl("https://www.dropbox.com") }, contentAlignment = Alignment.Center) { Text("B", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                    Box(modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFFEA4335)).clickable { viewModel.loadUrl("https://chrome.google.com") }, contentAlignment = Alignment.Center) { Text("C", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { viewModel.loadUrl(app.url) }.padding(4.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(54.dp).clip(CircleShape).background(Color(0xFF16132D)).border(BorderStroke(1.8.dp, app.color), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(app.initial, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                            }
                            Text(app.name, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }

                    // Add Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { showAddDialog = true }.padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(54.dp).clip(CircleShape).background(Color(0xFF1E1736)).border(BorderStroke(1.5.dp, neonPurple), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add new", tint = neonPink, modifier = Modifier.size(28.dp))
                        }
                        Text("Add new", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // 4. Project Pills
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                projectPills.chunked(2).forEach { rowPills ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        rowPills.forEach { pill ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(cyberCardBg)
                                    .border(BorderStroke(1.2.dp, cyberGradient), RoundedCornerShape(20.dp))
                                    .clickable { viewModel.loadUrl(pill.url) }
                                    .padding(horizontal = 10.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
   
