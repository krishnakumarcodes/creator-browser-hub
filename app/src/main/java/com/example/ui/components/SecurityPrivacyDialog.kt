package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel

@Composable
fun SecurityPrivacyDialog(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val metrics by viewModel.securityMetrics.collectAsState()
    var showClearConfirmation by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { viewModel.setShowSecurityDialog(false) },
        shape = RoundedCornerShape(24.dp),
        containerColor = DarkSurfaceElevated,
        modifier = modifier.testTag("security_privacy_dialog"),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CreatorGreen.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = CreatorGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Local-First Sandbox Security",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Creator Browser Hub operates in strict isolated storage. No telemetry, third-party analytics, or external credential sync are enabled.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
                )

                // Sandbox Path Indicator
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "APP SANDBOX ROOT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CreatorVioletLight,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = metrics?.sandboxPath ?: "/data/user/0/com.aistudio.creatorbrowser.vptmqz",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                        )
                    }
                }

                // Security Properties
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SecurityItem(
                        icon = Icons.Default.Lock,
                        label = "Cookie & Session Persistence",
                        detail = "Active (Saved in Android CookieManager)",
                        statusColor = CreatorGreen
                    )
                    SecurityItem(
                        icon = Icons.Default.Storage,
                        label = "DOM & Web SQL Storage",
                        detail = "Enabled locally in app data sandbox",
                        statusColor = CreatorCyan
                    )
                    SecurityItem(
                        icon = Icons.Default.Folder,
                        label = "Local Storage Footprint",
                        detail = "Cache: ${metrics?.formattedCacheSize ?: "Calculating..."} | Total: ${metrics?.formattedTotalSize ?: "..."}",
                        statusColor = CreatorVioletLight
                    )
                }

                if (showClearConfirmation) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CreatorRoseContainer.copy(alpha = 0.3f)),
                        border = BorderStroke(1.dp, CreatorRose),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Clear All Cookies & Local Cache?",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "This will sign you out of all creator accounts (YouTube Studio, Instagram, Facebook, Gmail) and delete local page cache.",
                                style = MaterialTheme.typography.bodySmall.copy(color = CreatorRoseLight)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { showClearConfirmation = false }) {
                                    Text("Cancel", color = TextSecondary)
                                }
                                Button(
                                    onClick = {
                                        showClearConfirmation = false
                                        viewModel.clearCacheAndCookies()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CreatorRose)
                                ) {
                                    Text("Confirm Clear", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (!showClearConfirmation) {
                TextButton(
                    onClick = { showClearConfirmation = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = CreatorRose)
                ) {
                    Text("Clear Cache & Cookies")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.setShowSecurityDialog(false) },
                colors = ButtonDefaults.textButtonColors(contentColor = TextPrimary)
            ) {
                Text("Done")
            }
        }
    )
}

@Composable
private fun SecurityItem(
    icon: ImageVector,
    label: String,
    detail: String,
    statusColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = statusColor,
            modifier = Modifier.size(18.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
            )
        }
    }
}
