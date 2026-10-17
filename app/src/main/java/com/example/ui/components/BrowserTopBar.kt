package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BrowserTab
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel

@Composable
fun BrowserTopBar(
    tab: BrowserTab,
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val focusManager = LocalFocusManager.current
    var inputText by remember(tab.url) { mutableStateOf(if (tab.isHome) "" else tab.url) }

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

            // टाइपिंग वाला सर्च बार (Search or enter Creator URL...)
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
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = hintTextColor,
                        modifier = Modifier.size(18.dp)
                    )
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
                            if (inputText.isNotBlank()) {
                                viewModel.loadUrl(inputText.trim())
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

