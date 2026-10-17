package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CreatorNote
import com.example.ui.theme.*
import com.example.viewmodel.BrowserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorNotesDrawer(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val notes by viewModel.notes.collectAsState()
    var isAddingNote by remember { mutableStateOf(false) }
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var noteTags by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = { viewModel.setShowNotesDrawer(false) },
        containerColor = DarkSurfaceElevated,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier.testTag("creator_notes_sheet")
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
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        tint = CreatorRoseLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "CREATOR SCRATCHPAD",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = 0.8.sp
                        )
                    )
                }

                if (!isAddingNote) {
                    Button(
                        onClick = { isAddingNote = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CreatorRose),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Note", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (isAddingNote) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, CreatorRoseContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "New Note / Video Tags",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )

                        OutlinedTextField(
                            value = noteTitle,
                            onValueChange = { noteTitle = it },
                            label = { Text("Title or Video Topic") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CreatorRose,
                                unfocusedBorderColor = DarkBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        OutlinedTextField(
                            value = noteContent,
                            onValueChange = { noteContent = it },
                            label = { Text("Tags, Timestamps, Captions, Links") },
                            minLines = 3,
                            maxLines = 6,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CreatorRose,
                                unfocusedBorderColor = DarkBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        OutlinedTextField(
                            value = noteTags,
                            onValueChange = { noteTags = it },
                            label = { Text("Hashtags / Category (e.g. #gaming, #vlog)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CreatorRose,
                                unfocusedBorderColor = DarkBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { isAddingNote = false }) {
                                Text("Cancel", color = TextSecondary)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (noteTitle.isNotBlank() || noteContent.isNotBlank()) {
                                        viewModel.saveNote(
                                            title = noteTitle.ifBlank { "Creator Note" },
                                            content = noteContent,
                                            tags = noteTags
                                        )
                                        noteTitle = ""
                                        noteContent = ""
                                        noteTags = ""
                                        isAddingNote = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CreatorRose)
                            ) {
                                Text("Save Note", color = Color.White)
                            }
                        }
                    }
                }
            }

            // List of Notes
            if (notes.isEmpty() && !isAddingNote) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NoteAlt,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "No notes yet",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                        Text(
                            text = "Keep video ideas, tags, and timestamps handy while managing channels",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                            fontSize = 11.sp
                        )
                    }
                }
            } else if (!isAddingNote) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(notes, key = { it.id }) { note ->
                        NoteCard(
                            note = note,
                            onCopy = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Creator Note", "${note.title}\n${note.content}")
                                clipboard.setPrimaryClip(clip)
                                viewModel.showMessage("Note copied to clipboard!")
                            },
                            onDelete = { viewModel.deleteNote(note) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteCard(
    note: CreatorNote,
    onCopy: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, DarkBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onCopy, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = CreatorCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            if (note.content.isNotBlank()) {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                )
            }

            if (note.tags.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CreatorRoseContainer.copy(alpha = 0.3f)
                ) {
                    Text(
                        text = note.tags,
                        style = MaterialTheme.typography.labelSmall.copy(color = CreatorRoseLight),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
