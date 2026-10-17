package com.example.data.local

import androidx.room.*
import com.example.data.model.Bookmark
import com.example.data.model.CreatorNote
import com.example.data.model.HistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BrowserDao {

    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark): Long

    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("SELECT COUNT(*) > 0 FROM bookmarks WHERE url = :url")
    suspend fun isBookmarked(url: String): Boolean

    @Query("DELETE FROM bookmarks WHERE url = :url")
    suspend fun deleteBookmarkByUrl(url: String)

    // History
    @Query("SELECT * FROM history ORDER BY visitedAt DESC LIMIT 100")
    fun getRecentHistory(): Flow<List<HistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: HistoryItem): Long

    @Query("DELETE FROM history WHERE id = :id")
    suspend fun deleteHistoryItem(id: Long)

    @Query("DELETE FROM history")
    suspend fun clearAllHistory()

    // Creator Quick Notes
    @Query("SELECT * FROM creator_notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<CreatorNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: CreatorNote): Long

    @Update
    suspend fun updateNote(note: CreatorNote)

    @Delete
    suspend fun deleteNote(note: CreatorNote)
}
