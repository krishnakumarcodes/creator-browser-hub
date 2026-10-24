package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.BrowserViewModel

// Neon Glow Colors
val GlowDarkBg = Color(0xFF090A16)
val GlassCard = Color(0xFF13152C)
val NeonPinkColor = Color(0xFFEC4899)
val NeonCyanColor = Color(0xFF06B6D4)
val NeonGreenColor = Color(0xFF10B981)
val NeonPurpleColor = Color(0xFFA855F7)
val NeonOrangeColor = Color(0xFFF97316)

data class ShortcutItem(
    val title: String,
    val badge: String,
    val url: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreatorDashboardView(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
                ShortcutItem("Github Repo Rework", "GH", "https://github.com"),
                ShortcutItem("Figma Wireframes", "FG", "https://figma.com"),
                ShortcutItem("Unsplash Curated Photos", "UN", "https://unsplash.com"),
                ShortcutItem("Notion Content Calendar", "NO", "https://notion.so")
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
                        if (editingTarget == "WORKSPACE") {
                            workspaceTitle = editTextValue
                        } else if (editingTarget == "COMMUNITY") {
                            communityTitle = editTextValue
                        } else if (editingTarget == "ASSETS") {
                            assetsTitle = editTextValue
                        } else {
                            customShortcuts = customShortcuts.map { item ->
                                if (item.title == editingTarget) {
                                    item.copy(title = editTextValue)
                                } else {
                                    item
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
                            val cleanUrl = if (newUrl.startsWith("http")) newUrl else "https://" + newUrl
                            val badge = if (newName.length >= 2) newName.take(2).uppercase() else newName.uppercase()
                            customShortcuts = customShortcuts + ShortcutItem(newName, badge, cleanUrl)
                            newName = ""
                            newUrl = ""
                            showAddDialog = false
                            Toast.makeText(context, "Shortcut added!", Toast.LENGTH_SHORT).show()
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
                    color = Color(0xFF161533),
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
                                        Toast.makeText(context, "Search: Google", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Brave Search", color = Color.White) },
                                    onClick = {
                                        selectedSearchEngine = "Brave"
                                        showSearchEngineMenu = false
                                        Toast.makeText(context, "Search: Brave", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("DuckDuckGo", color = Color.White) },
                                    onClick = {
                                        selectedSearchEngine = "DuckDuckGo"
                                        showSearchEngineMenu = false
                                        Toast.makeText(context, "Search: DuckDuckGo", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Startpage", color = Color.White) },
                                    onClick = {
                                        selectedSearchEngine = "Startpage"
                                        showSearchEngineMenu = false
                                        Toast.makeText(context, "Search: Startpage", Toast.LENGTH_SHORT).show()
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
                                    Toast.makeText(context, "Voice search activated", Toast.LENGTH_SHORT).show()
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
                                    Toast.makeText(context, "Opening Workspace...", Toast.LENGTH_SHORT).show()
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
                                    Toast.makeText(context, "Opening Community...", Toast.LENGTH_SHORT).show()
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
                                    Toast.makeText(context, "Opening Assets...", Toast.LENGTH_SHORT).show()
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
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        NeonCircularShortcut(
                            name = "WhatsApp",
                            symbol = "W",
                            glowColor = NeonGreenColor,
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
                            glowColor = NeonPurpleColor,
                            onClick = { showAddDialog = true }
                        )
                    }
                }
            }

            // 4. Two-Column Project Cards with Isolated Edit Button
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    customShortcuts.chunked(2).forEach { rowCards ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowCards.forEach { card ->
                                ProjectCardInteractive(
                                    item = card,
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.loadUrl(card.url) },
                                    onEdit = {
                                        editingTarget = card.title
                                        editTextValue = card.title
                                        showEditDialog = true
                                    },
                                    onLongPress = {
                                        customShortcuts = customShortcuts.filter { it.title != card.title }
                                        Toast.makeText(context, "Removed " + card.badge, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                            if (rowCards.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InteractiveFolderChip(
    title: String,
    borderColor: Color,
    icons: List<String>,
    urls: List<String>,
    onEditName: () -> Unit,
    onOpenAll: () -> Unit,
    onAppClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF110E26),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 9.sp,
                    color = borderColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(2.dp))
                IconButton(
                    onClick = onEditName,
                    modifier = Modifier.size(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Folder Name",
                        tint = borderColor,
                        modifier = Modifier.size(11.dp)
                    )
                }
                IconButton(
                    onClick = onOpenAll,
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Open Folder",
                        tint = borderColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icons.forEachIndexed { index, iconLetter ->
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFF221F45),
                        modifier = Modifier
                            .size(18.dp)
                            .clickable {
                                if (index < urls.size) {
                                    onAppClick(urls[index])
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = iconLetter,
                                fontSize = 8.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
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
            color = Color(0xFF161432),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectCardInteractive(
    item: ShortcutItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onLongPress: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = GlassCard,
        border = BorderStroke(1.dp, Color(0xFF2D2756)),
        modifier = modifier
            .height(64.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress
            )
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
                color = Color(0xFF231F46),
                border = BorderStroke(1.dp, NeonPurpleColor),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = item.badge,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 13.sp,
                modifier = Modifier.weight(1f)
            )
            // Isolated edit button: clicking pencil only opens edit dialog, does NOT trigger card click
            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Name",
                    tint = Color(0xFF75719B),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
