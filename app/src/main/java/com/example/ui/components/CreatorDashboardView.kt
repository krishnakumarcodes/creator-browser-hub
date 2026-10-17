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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CreatorShortcut
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreatorDashboardView(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    var customShortcuts by remember {
        mutableStateOf(
            listOf(
                CreatorShortcut(
                    id = "google",
                    name = "Google",
                    url = "https://www.google.com",
                    brandColor = Color(0xFF4285F4)
                ),
                CreatorShortcut(
                    id = "twitter",
                    name = "Twitter",
                    url = "https://x.com",
                    brandColor = TwitterColor
                ),
                CreatorShortcut(
                    id = "reddit",
                    name = "Reddit",
                    url = "https://www.reddit.com",
                    brandColor = Color(0xFFFF4500)
                )
            )
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var newShortcutName by remember { mutableStateOf("") }
    var newShortcutUrl by remember { mutableStateOf("") }

    val primaryShortcuts = CreatorShortcut.DEFAULT_SHORTCUTS

    // ➕ कस्टम शॉर्टकट जोड़ने का डायलॉग
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(
                    text = "Add Shortcut",
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
                        label = { Text("Name (e.g. YouTube)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CreatorViolet,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                    OutlinedTextField(
                        value = newShortcutUrl,
                        onValueChange = { newShortcutUrl = it },
                        label = { Text("URL (e.g. youtube.com)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CreatorViolet,
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
                            val newShortcut = CreatorShortcut(
                                id = "custom_${System.currentTimeMillis()}",
                                name = newShortcutName.trim(),
                                url = cleanUrl,
                                brandColor = CreatorViolet,
                                isCustom = true
                            )
                            customShortcuts = customShortcuts + newShortcut
                            newShortcutName = ""
                            newShortcutUrl = ""
                            showAddDialog = false
                            viewModel.showMessage("${newShortcut.name} added!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CreatorViolet)
                ) {
                    Text("Add", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = CreatorVioletContainer,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = CreatorVioletLight,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "CREATOR BROWSER HUB",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Native Multi-Tab Workspace",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DarkSurfaceElevated,
                            border = BorderStroke(1.dp, DarkBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Computer,
                                    contentDescription = null,
                                    tint = CreatorVioletLight,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Desktop Mode Default",
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DarkSurfaceElevated,
                            border = BorderStroke(1.dp, DarkBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = CreatorGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Local Cookie Sandbox",
                                    color = CreatorGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. ⭐ FAVORITE WEBSITES (QUICK ACCESS) 4-कॉलम ग्रिड (Add पहले, फिर ऐप्स)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "FAVORITE WEBSITES (QUICK ACCESS)",
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.8.sp
                    )

                    val totalCount = 1 + customShortcuts.size
                    val rowCount = (totalCount + 3) / 4

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        for (rowIndex in 0 until rowCount) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (colIndex in 0 until 4) {
                                    val itemIndex = rowIndex * 4 + colIndex
                                    if (itemIndex == 0) {
                                        // पहला स्थान हमेशा (+) Add बटन
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier
                                                .width(68.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable { showAddDialog = true }
                                                .padding(vertical = 4.dp)
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = DarkSurfaceElevated,
                                                border = BorderStroke(1.dp, CreatorViolet.copy(alpha = 0.6f)),
                                                modifier = Modifier.size(50.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(
                                                        imageVector = Icons.Default.Add,
                                                        contentDescription = "Add",
                                                        tint = CreatorVioletLight,
                                                        modifier = Modifier.size(26.dp)
                                                    )
                                                }
                                            }
                                            Text(
                                                text = "Add",
                                                color = CreatorVioletLight,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 11.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    } else if (itemIndex <= customShortcuts.size) {
                                        val shortcut = customShortcuts[itemIndex - 1]
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier
                                                .width(68.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .combinedClickable(
                                                    onClick = { viewModel.loadUrl(shortcut.url) },
                                                    onLongClick = {
                                                        if (shortcut.isCustom) {
                                                            customShortcuts = customShortcuts.filter { it.id != shortcut.id }
                                                            viewModel.showMessage("${shortcut.name} removed")
                                                        }
                                                    }
                                                )
                                                .padding(vertical = 4.dp)
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = DarkSurfaceElevated,
                                                border = BorderStroke(1.dp, shortcut.brandColor),
                                                modifier = Modifier.size(50.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = shortcut.name.take(1).uppercase(),
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Black,
                                                        fontSize = 18.sp
                                                    )
                                                }
                                            }
                                            Text(
                                                text = shortcut.name,
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 11.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.width(68.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. PRIMARY CREATOR STUDIOS Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PRIMARY CREATOR STUDIOS",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CreatorVioletContainer
                ) {
                    Text(
                        text = "Instant Desktop Load",
                        color = CreatorVioletLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // 4. Primary Studios Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                primaryShortcuts.forEach { shortcut ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.loadUrl(shortcut.url) }
                    ) {
                        Row(
           
