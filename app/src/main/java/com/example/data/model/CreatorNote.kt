package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "creator_notes")
data class CreatorNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val tags: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
