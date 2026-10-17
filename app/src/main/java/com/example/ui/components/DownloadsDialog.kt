package com.example.ui.components

import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DownloadedFileItem(
    val name: String,
    val size: String,
    val date: String,
    val file: File
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsDialog(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var filesList by remember { mutableStateOf<List<DownloadedFileItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val files = downloadsDir.listFiles()?.filter { it.isFile }?.sortedByDescending { it.lastModified() } ?: emptyList()
        val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

        filesList = files.take(30).map { f ->
            val sizeKb = f.length() / 1024
            val formattedSize = if (sizeKb > 1024) "${sizeKb / 1024} MB" else "$sizeKb KB"
            DownloadedFileItem(
                name = f.name,
                size = formattedSize,
                date = dateFormat.format(Date(f.lastModified())),
                file = f
            )
        }
    }

    ModalBottomSheet(
        onDismissRequest = { viewModel.setShowDownloadsSheet(false) },
        containerColor = DarkSurfaceElevated,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = CreatorCyanLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "DOWNLOADS (डाउनलोड्स)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = 0.8.sp
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CreatorCyanContainer
                ) {
                    Text(
                        text = "${filesList.size} फ़ाइलें",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = CreatorCyanLight,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            if (filesList.isEmpty()) {
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
                            imageVector = Icons.Default.FileDownloadOff,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(44.dp)
                        )
                        Text(
                            text = "अभी कोई डाउनलोड की गई फ़ाइल नहीं है",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filesList, key = { it.file.absolutePath }) { item ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            border = BorderStroke(1.dp, DarkBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    try {
                                        val uri = Uri.parse(item.file.absolutePath)
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            setDataAndType(uri, "*/*")
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        viewModel.showMessage("फ़ाइल खोलने में असमर्थ")
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = CreatorCyanContainer.copy(alpha = 0.3f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.InsertDriveFile,
                                            contentDescription = null,
                                            tint = CreatorCyanLight,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${item.size} • ${item.date}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                        fontSize = 11.sp
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (item.file.delete()) {
                                            filesList = filesList.filter { it.file.absolutePath != item.file.absolutePath }
                                            viewModel.showMessage("फ़ाइल हटा दी गई")
                                        }
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete",
                                        tint = CreatorRose,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
