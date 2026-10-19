package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.viewmodel.BrowserViewModel

// 1. शॉर्टकट डेटा मॉडल
data class DashboardShortcut(
    val id: String,
    val title: String,
    val url: String,
    val initial: String = title.take(1).uppercase()
)

@Composable
fun CreatorDashboardView(
    modifier: Modifier = Modifier,
    viewModel: BrowserViewModel? = null,
    onNavigateToUrl: (String) -> Unit = { url -> viewModel?.loadUrl(url) },
    isDark: Boolean = isSystemInDarkTheme()
) {
    // 4x4 डिफ़ॉल्ट क्विक एक्सेस लिस्ट
    val shortcuts = remember {
        mutableStateListOf(
            DashboardShortcut("1", "Google", "https://www.google.com"),
            DashboardShortcut("2", "YouTube", "https://www.youtube.com"),
            DashboardShortcut("3", "Studio", "https://studio.youtube.com"),
            DashboardShortcut("4", "GitHub", "https://www.github.com"),
            DashboardShortcut("5", "ChatGPT", "https://chatgpt.com"),
            DashboardShortcut("6", "Wikipedia", "https://www.wikipedia.org"),
            DashboardShortcut("7", "Canva", "https://www.canva.com")
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newUrl by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA))
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // हेडर: पसंदीदा वेबसाइट्स (4x4 ग्रिड)
        Text(
            text = "पसंदीदा वेबसाइट्स (4x4 ग्रिड)",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color(0xFFE8EAED) else Color(0xFF202124),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 4x4 ग्रिड कंटेनर (Fixed 4 Columns)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF1E1F22) else Color(0xFFFFFFFF)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                userScrollEnabled = false
            ) {
                // शॉर्टकट आइटम्स
                items(shortcuts, key = { it.id }) { item ->
                    DashboardShortcutItem(
                        title = item.title,
                        letter = item.initial,
                        isDark = isDark,
                        onClick = { onNavigateToUrl(item.url) }
                    )
                }

                // '+' नया शॉर्टकट जोड़ने वाला बटन
                item {
                    AddDashboardItem(
                        isDark = isDark,
                        onClick = { showAddDialog = true }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // क्रिएटर डेस्कटॉप टूल्स सेक्शन
        Text(
            text = "क्रिएटर स्टूडियो (डेस्कटॉप मोड)",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color(0xFFE8EAED) else Color(0xFF202124),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CreatorStudioCard(
                title = "YT Studio",
                subtitle = "एनालिटिक्स व अपलोड",
                iconText = "▶",
                isDark = isDark,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToUrl("https://studio.youtube.com") }
            )

            CreatorStudioCard(
                title = "AI Studio",
                subtitle = "मॉडल व वर्कस्पेस",
                iconText = "✦",
                isDark = isDark,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateToUrl("https://aistudio.google.com") }
            )
        }
    }

    // नया शॉर्टकट जोड़ने का डायलॉग (AlertDialog)
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "नई वेबसाइट जोड़ें",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("वेबसाइट का नाम") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newUrl,
                        onValueChange = { newUrl = it },
                        label = { Text("URL (उदा. https://...)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newTitle.isNotBlank() && newUrl.isNotBlank()) {
                            val cleanUrl = if (!newUrl.startsWith("http://") && !newUrl.startsWith("https://")) {
                                "https://$newUrl"
                            } else newUrl

                            shortcuts.add(
                                DashboardShortcut(
                                    id = System.currentTimeMillis().toString(),
                                    title = newTitle.trim(),
                                    url = cleanUrl.trim()
                                )
                            )
                            newTitle = ""
                            newUrl = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("जोड़ें")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("रद्द करें")
                }
            }
        )
    }
}

@Composable
private fun DashboardShortcutItem(
    title: String,
    letter: String,
    isDark: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(if (isDark) Color(0xFF2B2D31) else Color(0xFFE8EAED)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color(0xFF8AB4F8) else Color(0xFF1A73E8)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDark) Color(0xFFE8EAED) else Color(0xFF3C4043),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AddDashboardItem(
    isDark: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(if (isDark) Color(0xFF1E1F22) else Color(0xFFF1F3F4))
                .border(
                    width = 1.dp,
                    color = if (isDark) Color(0xFF3C4043) else Color(0xFFDADCE0),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Website",
                tint = if (isDark) Color(0xFF9AA0A6) else Color(0xFF5F6368),
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "जोड़ें",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDark) Color(0xFF9AA0A6) else Color(0xFF5F6368),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CreatorStudioCard(
    title: String,
    subtitle: String,
    iconText: String,
    isDark: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1F22) else Color(0xFFFFFFFF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0xFF2B2D31) else Color(0xFFE8EAED)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconText,
                    fontSize = 16.sp,
                    color = if (isDark) Color(0xFF8AB4F8) else Color(0xFF1A73E8)
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color(0xFF202124)
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = if (isDark) Color(0xFF9AA0A6) else Color(0xFF5F6368)
                )
            }
        }
    }
}
