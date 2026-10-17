package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BrowserTab
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabOverviewSheet(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val tabs by viewModel.tabs.collectAsState()
    val activeTabId by viewModel.activeTabId.collectAsState()

    ModalBottomSheet(
        onDismissRequest = { viewModel.setShowTabOverview(false) },
        containerColor = DarkSurface,
        scrimColor = Color.Black.copy(alpha = 0.65f),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier.testTag("tab_overview_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "OPEN TABS",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CreatorVioletContainer
                    ) {
                        Text(
                            text = "${tabs.size}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = CreatorVioletLight,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                if (tabs.size > 1) {
                    TextButton(
                        onClick = { viewModel.closeAllTabs() },
                        colors = ButtonDefaults.textButtonColors(contentColor = CreatorRose)
                    ) {
                        Text("Close All", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            // Grid of Tabs
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
            ) {
                items(tabs, key = { it.id }) { tab ->
                    val isActive = tab.id == activeTabId
                    TabCard(
                        tab = tab,
                        isActive = isActive,
                        onSelect = { viewModel.switchTab(tab.id) },
                        onClose = { viewModel.closeTab(tab.id) }
                    )
                }
            }

            // Bottom Actions: New Tab button
            Button(
                onClick = { viewModel.addNewTab() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("new_tab_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CreatorViolet,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Open New Tab",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun TabCard(
    tab: BrowserTab,
    isActive: Boolean,
    onSelect: () -> Unit,
    onClose: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) DarkSurfaceElevated else DarkSurfaceVariant
        ),
        border = BorderStroke(
            width = if (isActive) 2.dp else 1.dp,
            color = if (isActive) CreatorViolet else DarkBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable(onClick = onSelect)
            .testTag("tab_card_${tab.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top row with tab indicator and close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (tab.isDesktopMode) CreatorVioletContainer else DarkSurface
                ) {
                    Text(
                        text = if (tab.isDesktopMode) "DESKTOP" else "MOBILE",
                        style = TextStyle(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (tab.isDesktopMode) CreatorVioletLight else TextMuted
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(26.dp).testTag("close_tab_${tab.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Tab",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Tab Center Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkBackground.copy(alpha = 0.5f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (tab.isHome) Icons.Default.Dashboard else Icons.Default.Language,
                    contentDescription = null,
                    tint = if (isActive) CreatorVioletLight else TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Tab Title / URL
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = tab.displayTitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                        color = if (isActive) TextPrimary else TextSecondary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
