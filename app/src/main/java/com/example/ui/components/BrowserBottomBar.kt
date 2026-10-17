package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BrowserTab
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel

@Composable
fun BrowserBottomBar(
    tab: BrowserTab,
    tabCount: Int,
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val showMoreMenu by viewModel.showMoreMenu.collectAsState()

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = DarkSurface,
        tonalElevation = 6.dp,
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Back Button
            IconButton(
                onClick = { viewModel.goBack() },
                enabled = tab.canGoBack || !tab.isHome,
                modifier = Modifier.testTag("nav_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = if (tab.canGoBack || !tab.isHome) TextPrimary else TextMuted
                )
            }

            // 2. Forward Button
            IconButton(
                onClick = { viewModel.goForward() },
                enabled = tab.canGoForward,
                modifier = Modifier.testTag("nav_forward_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Forward",
                    tint = if (tab.canGoForward) TextPrimary else TextMuted
                )
            }

            // 3. Home Dashboard Button
            IconButton(
                onClick = { viewModel.navigateHome() },
                modifier = Modifier.testTag("nav_home_button")
            ) {
                Icon(
                    imageVector = if (tab.isHome) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                    contentDescription = "Home",
                    tint = if (tab.isHome) CreatorVioletLight else TextPrimary
                )
            }

            // 4. Tabs Counter Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkSurfaceElevated)
                    .clickable { viewModel.setShowTabOverview(true) }
                    .testTag("tabs_overview_button")
            ) {
                Text(
                    text = tabCount.toString(),
                    color = CreatorVioletLight,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )
            }

            // 5. Three-Dots Menu (⋮)
            Box {
                IconButton(
                    onClick = { viewModel.setShowMoreMenu(true) },
                    modifier = Modifier.testTag("nav_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = TextPrimary
                    )
                }

                DropdownMenu(
                    expanded = showMoreMenu,
                    onDismissRequest = { viewModel.setShowMoreMenu(false) },
                    modifier = Modifier.background(DarkSurfaceElevated)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (tab.isDesktopMode) "Switch to Mobile Site" else "Request Desktop Site",
                                color = TextPrimary
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (tab.isDesktopMode) Icons.Default.Smartphone else Icons.Default.Computer,
                                contentDescription = null,
                                tint = CreatorVioletLight
                            )
                        },
                        onClick = {
                            viewModel.setShowMoreMenu(false)
                            viewModel.toggleDesktopMode()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Bookmarks & History", color = TextPrimary) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Bookmarks,
                                contentDescription = null,
                                tint = CreatorCyan
                            )
                        },
                        onClick = {
                            viewModel.setShowMoreMenu(false)
                            viewModel.setShowBookmarksHistory(true)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Creator Quick Notes", color = TextPrimary) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = CreatorRose
                            )
                        },
                        onClick = {
                            viewModel.setShowMoreMenu(false)
                            viewModel.setShowNotesDrawer(true)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Local Security & Storage", color = TextPrimary) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = CreatorGreen
                            )
                        },
                        onClick = {
                            viewModel.setShowMoreMenu(false)
                            viewModel.setShowSecurityDialog(true)
                        }
                    )

                    if (!tab.isHome) {
                        DropdownMenuItem(
                            text = { Text("
                                          
