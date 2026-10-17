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
    var speedDialList by remember { mutableStateOf(CreatorShortcut.TOP_SPEED_DIAL) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newShortcutName by remember { mutableStateOf("") }
    var newShortcutUrl by remember { mutableStateOf("") }

    val studioShortcuts = remember { CreatorShortcut.DEFAULT_SHORTCUTS }

    // ➕ कस्टम शॉर्टकट जोड़ने का पॉपअप डायलॉग
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = DarkSurfaceElevated,
            title = {
                Text(
                    text = "नया शॉर्टकट जोड़ें",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
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
                            focusedBorderColor = CreatorViolet,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                    OutlinedTextField(
                        value = newShortcutUrl,
                        onValueChange = { newShortcutUrl = it },
                        label = { Text("वेबसाइट URL (उदा. x.com)") },
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
                                newShortcutUrl
                            } else {
                                "https://$newShortcutUrl"
                            }
                            val custom = CreatorShortcut(
                                id = "custom_${System.currentTimeMillis()}",
                                name = newShortcutName.trim(),
                                url = cleanUrl,
                                brandColor = CreatorViolet,
                                isCustom = true
                            )
                            speedDialList = speedDialList + custom
                            newShortcutName = ""
                            newShortcutUrl = ""
                            showAddDialog = false
                            viewModel.showMessage("शॉर्टकट होम स्क्रीन पर जुड़ गया!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CreatorViolet)
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
            .background(DarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 🌟 1. पसंदीदा ऐप्स ग्रिड (Google, YouTube, Facebook, Instagram + Add)
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
                        text = "पसंदीदा वेबसाइट्स (QUICK ACCESS)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        speedDialList.chunked(4).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                rowItems.forEach { item ->
                                    SpeedDialItem(
                                        shortcut = item,
                                        onClick = { viewModel.loadUrl(item.url) },
                                        onLongClick = {
                                            if (item.isCustom) {
                                                speedDialList = speedDialList.filter { it.id != item.id }
                                                viewModel.showMessage("${item.name} हटा दिया गया")
                                            }
                                        }
                                    )
                                }
                                repeat(4 - rowItems.size) {
                                    Spacer(modifier = Modifier.width(60.dp))
                                }
                            }
                        }

                        // '+' नया शॉर्टकट बटन
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
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
                                    border = BorderStroke(1.dp, CreatorViolet.copy(alpha = 0.5f)),
                                    modifier = Modifier.size(50.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add Shortcut",
                                            tint = CreatorVioletLight,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Add",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CreatorVioletLight,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // 🌟 2. क्रिएटर स्टूडियोज़ (यथावत सुरक्षित)
        item {
            Text(
                text = "CREATOR STUDIOS (DESKTOP MODE)",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                studioShortcuts.forEach { shortcut ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.loadUrl(shortcut.url) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = shortcut.brandColor.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, shortcut.brandColor.copy(alpha = 0.4f)),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Hub,
                                        contentDescription = shortcut.name,
                                        tint = shortcut.brandColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = shortcut.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = shortcut.description,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Open",
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpeedDialItem(
    shortcut: CreatorShortcut,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .width(68.dp)
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = 4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = shortcut.brandColor.copy(alpha = 0.2f),
            border = BorderStroke(1.dp, shortcut.brandColor.copy(alpha = 0.4f)),
            modifier = Modifier.size(50.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = shortcut.name.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                )
            }
        }
        Text(
            text = shortcut.name,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}
