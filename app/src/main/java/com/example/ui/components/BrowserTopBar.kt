package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BrowserTab
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel

enum class SearchEngine(
    val displayName: String,
    val shortCode: String,
    val brandColor: Color,
    val searchUrlPrefix: String
) {
    GOOGLE("Google", "G", Color(0xFF4285F4), "https://www.google.com/search?q="),
    BRAVE("Brave Search", "B", Color(0xFFFF5722), "https://search.brave.com/search?q="),
    DUCKDUCKGO("DuckDuckGo", "D", Color(0xFFDE5833), "https://duckduckgo.com/?q="),
    STARTPAGE("Startpage", "S", Color(0xFF4B6B94), "https://www.startpage.com/sp/search?query=")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrowserTopBar(
    tab: BrowserTab,
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val focusManager = LocalFocusManager.current
    var inputText by remember(tab.url) { mutableStateOf(if (tab.isHome) "" else tab.url) }

    var selectedEngine by remember { mutableStateOf(SearchEngine.GOOGLE) }
    var showEngineMenu by remember { mutableStateOf(false) }

    val barBackgroundColor = if (isDarkMode) Color(0xFF0F0E17) else Color(0xFFFFFFFF)
    val searchBoxBackground = if (isDarkMode) Color(0xFF1F1D2B) else Color(0xFFF1F3F4)
    val primaryTextColor = if (isDarkMode) Color.White else Color(0xFF202124)
    val hintTextColor = if (isDarkMode) Color(0xFFA0A0B2) else Color(0xFF5F6368)
    val iconTint = if (isDarkMode) Color.White else Color(0xFF3C4043)
    val desktopBadgeColor = Color(0xFF6C5CE7)

    // रंग-बिरंगे लोडिंग ग्रेडिएंट कलर्स
    val rainbowBrush = Brush.horizontalGradient(
        listOf(
            Color(0xFF8B5CF6), // Violet
            Color(0xFF06B6D4), // Cyan
            Color(0xFFF43F5E), // Rose
            Color(0xFFF59E0B)  // Amber
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(barBackgroundColor)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Desktop / Mobile टॉगल बैज
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (tab.isDesktopMode) desktopBadgeColor else DarkSurfaceElevated,
                modifier = Modifier
                    .height(38.dp)
                    .clickable { viewModel.toggleDesktopMode() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (tab.isDesktopMode) "DESKTOP" else "MOBILE",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // सर्च बार (सर्च इंजन बैज + इनपुट टेक्स्ट फ़ील्ड)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = searchBoxBackground,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // सर्च इंजन बैज (1 टैप = अगला इंजन, लॉन्ग-प्रेस = पूरी लिस्ट मेन्यू)
                    Box {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(selectedEngine.brandColor)
                                .combinedClickable(
                                    onClick = {
                                        val all = SearchEngine.values()
                                        val nextIndex = (selectedEngine.ordinal + 1) % all.size
                                        selectedEngine = all[nextIndex]
                                        Toast.makeText(context, "सुरक्षित इंजन: ${selectedEngine.displayName}", Toast.LENGTH_SHORT).show()
                                    },
                                    onLongClick = {
                                        showEngineMenu = true
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = selectedEngine.shortCode,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // लॉन्ग-प्रेस पर खुलने वाला सर्च इंजन चयन मेन्यू
                        DropdownMenu(
                            expanded = showEngineMenu,
                            onDismissRequest = { showEngineMenu = false },
                            modifier = Modifier.background(DarkSurfaceElevated)
                        ) {
                            SearchEngine.values().forEach { engine ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = engine.displayName,
                                            color = TextPrimary,
                                            fontWeight = if (engine == selectedEngine) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(engine.brandColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = engine.shortCode,
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedEngine = engine
                                        showEngineMenu = false
                                        Toast.makeText(context, "सुरक्षित इंजन: ${engine.displayName}", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = primaryTextColor,
                            fontSize = 13.sp
                        ),
                        cursorBrush = SolidColor(CreatorVioletLight),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                        keyboardActions = KeyboardActions(onGo = {
                            val query = inputText.trim()
                            if (query.isNotBlank()) {
                                val targetUrl = when {
                                    query.startsWith("http://") || query.startsWith("https://") -> query
                                    query.contains(".") && !query.contains(" ") -> "https://$query"
                                    else -> "${selectedEngine.searchUrlPrefix}$query"
                                }
                                viewModel.loadUrl(targetUrl)
                                focusManager.clearFocus()
                            }
                        }),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (inputText.isEmpty()) {
                                Text(
                                    text = "Search or enter Creator URL...",
                                    color = hintTextColor,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            // रीफ्रेश बटन
            IconButton(
                onClick = { viewModel.reload() },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = iconTint
                )
            }
        }

        // 🌈 लोड होते वक्त रंग-बिरंगा प्रोग्रेस बार
        if (tab.isLoading && tab.progress < 100) {
            val progressFraction = (tab.progress.coerceIn(10, 100)) / 100f
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressFraction)
                    .height(3.dp)
                    .background(rainbowBrush)
            )
        }
    }
}
