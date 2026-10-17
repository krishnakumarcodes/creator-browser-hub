package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Bookmark
import com.example.data.model.HistoryItem
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksHistoryDialog(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val bookmarks by viewModel.bookmarks.collectAsState()
    val history by viewModel.history.collectAsState()

    ModalBottomSheet(
        onDismissRequest = { viewModel.setShowBookmarksHistory(false) },
        containerColor = DarkSurfaceElevated,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier.testTag("bookmarks_history_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tab Selector
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkSurfaceElevated,
                contentColor = CreatorVioletLight,
                divider = { HorizontalDivider(color = DarkBorder) }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Bookmarks (${bookmarks.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) CreatorVioletLight else TextSecondary
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "History (${history.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) CreatorCyanLight else TextSecondary
                        )
                    }
                )
            }

            if (selectedTab == 0) {
                // Bookmarks Tab
                if (bookmarks.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = "No bookmarks saved yet",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                            )
                            Text(
                                text = "Tap the star icon in the address bar to bookmark any page",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(bookmarks, key = { it.id }) { bookmark ->
                            BookmarkRow(
                                bookmark = bookmark,
                                onClick = {
                                    viewModel.loadUrl(bookmark.url)
                                    viewModel.setShowBookmarksHistory(false)
                                },
                                onDelete = { viewModel.deleteBookmark(bookmark) }
                            )
                        }
                    }
                }
            } else {
                // History Tab
                if (history.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = "No browsing history",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { viewModel.clearAllHistory() },
                            colors = ButtonDefaults.textButtonColors(contentColor = CreatorRose)
                        ) {
                            Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear History", fontSize = 12.sp)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 340.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(history, key = { it.id }) { item ->
                            HistoryRow(
                                item = item,
                                onClick = {
                                    viewModel.loadUrl(item.url)
                                    viewModel.setShowBookmarksHistory(false)
                                },
                                onDelete = { viewModel.deleteHistoryItem(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookmarkRow(
    bookmark: Bookmark,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, DarkBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = CreatorAmber,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bookmark.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = bookmark.url,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun HistoryRow(
    item: HistoryItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()) }
    val formattedTime = remember(item.visitedAt) { timeFormat.format(Date(item.visitedAt)) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, DarkBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = CreatorCyan,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.url,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
