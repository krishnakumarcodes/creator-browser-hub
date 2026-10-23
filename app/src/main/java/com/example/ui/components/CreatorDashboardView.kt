package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.BrowserViewModel

// Neon & Glow Colors
val GlowDarkBg = Color(0xFF090A16)
val GlassCard = Color(0xFF13152C).copy(alpha = 0.92f)
val NeonPinkColor = Color(0xFFEC4899)
val NeonCyanColor = Color(0xFF06B6D4)
val NeonGreenColor = Color(0xFF10B981)
val NeonPurpleColor = Color(0xFFA855F7)
val NeonOrangeColor = Color(0xFFF97316)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreatorDashboardView(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    var showSearchEngineMenu by remember { mutableStateOf(false) }
    var selectedSearchEngine by remember { mutableStateOf("Google") }

    // Folder Names
    var workspaceTitle by remember { mutableStateOf("WORKSPACE") }
    var communityTitle by remember { mutableStateOf("COMMUNITY") }
    var assetsTitle by remember { mutableStateOf("ASSETS") }

    // Dialog States
    var showEditDialog by remember { mutableStateOf(false) }
    var editingTarget by remember { mutableStateOf("") }
    var editTextValue by remember { mutableStateOf("") }

    // Shortcuts State
    var customShortcuts by remember {
        mutableStateOf(
            listOf(
                Triple("Github Repo\nRework", "GH", "https://github.com"),
                Triple("Figma\nWireframes", "FG", "https://figma.com"),
                Triple("Unsplash Curated\nPhotos", "UN", "https://unsplash.com"),
                Triple("Notion Content\nCalendar", "NO", "https://notion.so")
            )
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newUrl by remember { mutableStateOf("") }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            containerColor = Color(0xFF1A1736),
            title = { Text("Edit Name", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = editTextValue,
                    onValueChange = { editTextValue = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NeonPurpleColor,
                        unfocusedBorderColor = Color(0xFF3B2D68)
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        when (editingTarget) {
                            "WORKSPACE" -> workspaceTitle = editTextValue
                            "COMMUNITY" -> communityTitle = editTextValue
                            "ASSETS" -> assetsTitle = editTextValue
                            else -> {
                                customShortcuts = customShortcuts.map {
                                    if (it.first == editingTarget) Triple(editTextValue, it.second, it.third) else it
                                }
                            }
                        }
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurpleColor)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = Color(0xFF1A1736),
            title = { Text("Add New Shortcut", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newUrl,
                        onValueChange = { newUrl = it },
                        label = { Text("URL (e.g. google.com)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank() && newUrl.isNotBlank()) {
                            val cleanUrl = if (newUrl.startsWith("http")) newUrl else "https://$newUrl"
                            val badge = if (newName.length >= 2) newName.take(2).uppercase() else newName.uppercase()
                            customShortcuts = customShortcuts + Triple(newName, badge, cleanUrl)
                            newName = ""
                            newUrl = ""
                            showAddDialog = false
                            viewModel.showMessage("Shortcut added!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurpleColor)
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0E0B25), Color(0xFF080716), Color(0xFF050510))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Cyber Search Bar with Search Engine Selector Dropdown
            item {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFF161533).copy(alpha = 0.95f),
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
                            color = Color(0xFF23224C),
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    val searchUrl = when (selectedSearchEngine) {
                                        "Brave" -> "https://search.brave.com"
                                        "DuckDuckGo" -> "https://duckduckgo.com"
                                        "Startpage" -> "https://www.startpage.com"
                                        else -> "https://www.google.com"
                                    }
                                    viewModel.loadUrl(searchUrl)
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = selectedSearchEngine.take(1),
                                    color = Color(0xFF4285F4),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 17.sp
                                )
                            }
                        }

                        Box {
                            IconButton(
                                onClick = { showSearchEngineMenu = true },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Search Engine",
                                    tint = Color(0xFF8E8EA8)
                                )
                            }

                            DropdownMenu(
                                expanded = showSearchEngineMenu,
                                onDismissRequest = { showSearchEngineMenu = false },
                                modifier = Modifier.background(Color(0xFF1B1838))
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Google", color = Color.White) },
                                    onClick = {
                                        selectedSearchEngine = "Google"
                                        showSearchEngineMenu = false
                                        viewModel.showMessage("Search engine: Google")
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Brave Search", color = Color.White) },
                                    onClick = {
                                        selectedSearchEngine = "Brave"
                                        showSearchEngineMenu = false
                                        viewModel.showMessage("Search engine: Brave")
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("DuckDuckGo", color = Color.White) },
                                    onClick = {
                                        selectedSearchEngine = "DuckDuckGo"
                                        showSearchEngineMenu = false
                                        viewModel.showMessage("Search engine: DuckDuckGo")
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Startpage", color = Color.White) },
                                    onClick = {
                                        selectedSearchEngine = "Startpage"
                                        showSearchEngineMenu = false
                                        viewModel.showMessage("Search engine: Startpage")
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Search Web...",
                            color = Color(0xFF8E8EA8),
                            fontSize = 15.sp,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val searchUrl = when (selectedSearchEngine) {
                                        "Brave" -> "https://search.brave.com"
                                        "DuckDuckGo" -> "https://duckduckgo.com"
                                        "Startpage" -> "https://www.startpage.com"
                                        else -> "https://www.google.com"
                                    }
                                    viewModel.loadUrl(searchUrl)
                                }
                        )

                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Search",
                            tint = Color.White,
                            modifier = Modifier
                                .size(22.dp)
                                .clickable {
                                    viewModel.showMessage("Voice search activated")
                                }
                        )
                    }
                }
            }

            // 2. Creator Nexus Status Card
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = GlassCard,
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
                            .padding(16.dp),
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
                            color = NeonPinkColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )

                        Text(
                            text = "PROJECTS ACTIVE: 3",
                            color = NeonCyanColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )

                        Text(
                            text = "INSPIRATION BLOCKS CLEARED: 12",
                            color = NeonGreenColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Category Folders (WORKSPACE, COMMUNITY, ASSETS)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InteractiveFolderChip(
                                title = workspaceTitle,
                                borderColor = NeonPurpleColor,
                                icons = listOf("F", "N", "V"),
                                urls = listOf("https://figma.com", "https://notion.so", "https://code.visualstudio.com"),
                                onEditName = {
                                    editingTarget = "WORKSPACE"
                                    editTextValue = workspaceTitle
                                    showEditDialog = true
                                },
                                onOpenAll = {
                                    viewModel.loadUrl("https://figma.com")
                                    viewModel.showMessage("Opening Workspace...")
                                },
                                onAppClick = { url -> viewModel.loadUrl(url) },
                                modifier = Modifier.weight(1f)
                            )

                            InteractiveFolderChip(
                                title = communityTitle,
                                borderColor = NeonCyanColor,
                                icons = listOf("D", "S", "G"),
                                urls = listOf("https://discord.com", "https://slack.com", "https://github.com"),
                                onEditName = {
                                    editingTarget = "COMMUNITY"
                                    editTextValue = communityTitle
                                    showEditDialog = true
                                },
                                onOpenAll = {
                                    viewModel.loadUrl("https://discord.com")
                                    viewModel.showMessage("Opening Community...")
                                },
                                onAppClick = { url -> viewModel.loadUrl(url) },
                                modifier = Modifier.weight(1f)
                            )

                            InteractiveFolderChip(
                                title = assetsTitle,
                                borderColor = NeonOrangeColor,
                                icons = listOf("D", "B", "C"),
                                urls = listOf("https://drive.google.com", "https://dropbox.com", "https://google.com"),
                                onEditName = {
                                    editingTarget = "ASSETS"
                                    editTextValue = assetsTitle
                                    showEditDialog = true
                                },
                                onOpenAll = {
                                    viewModel.loadUrl("https://drive.google.com")
                                    viewModel.showMessage("Opening Assets...")
                                },
                                onAppClick = { url -> viewModel.loadUrl(url) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 3. Circular Quick Shortcuts (WhatsApp, YouTube, Spotify, Add new)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GlassCard,
                    border = BorderStroke(1.dp, Color(0xFF2C2754)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp, horizontal = 8.dp),
                        ho
