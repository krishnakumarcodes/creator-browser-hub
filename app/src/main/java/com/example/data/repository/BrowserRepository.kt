package com.example.data.repository

import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebStorage
import com.example.data.local.BrowserDao
import com.example.data.model.Bookmark
import com.example.data.model.CreatorNote
import com.example.data.model.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File

class BrowserRepository(
    private val browserDao: BrowserDao,
    private val context: Context
) {
    val bookmarks: Flow<List<Bookmark>> = browserDao.getAllBookmarks()
    val history: Flow<List<HistoryItem>> = browserDao.getRecentHistory()
    val notes: Flow<List<CreatorNote>> = browserDao.getAllNotes()

    suspend fun addBookmark(title: String, url: String) = withContext(Dispatchers.IO) {
        if (url.isNotBlank() && !isBookmarked(url)) {
            browserDao.insertBookmark(Bookmark(title = title.ifBlank { url }, url = url))
        }
    }

    suspend fun removeBookmark(bookmark: Bookmark) = withContext(Dispatchers.IO) {
        browserDao.deleteBookmark(bookmark)
    }

    suspend fun removeBookmarkByUrl(url: String) = withContext(Dispatchers.IO) {
        browserDao.deleteBookmarkByUrl(url)
    }

    suspend fun isBookmarked(url: String): Boolean = withContext(Dispatchers.IO) {
        browserDao.isBookmarked(url)
    }

    suspend fun recordHistory(title: String, url: String) = withContext(Dispatchers.IO) {
        if (url.isNotBlank() && !url.startsWith("about:") && !url.startsWith("creatorhub:")) {
            browserDao.insertHistory(HistoryItem(title = title.ifBlank { url }, url = url))
        }
    }

    suspend fun deleteHistoryItem(id: Long) = withContext(Dispatchers.IO) {
        browserDao.deleteHistoryItem(id)
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        browserDao.clearAllHistory()
    }

    suspend fun saveNote(title: String, content: String, tags: String = "") = withContext(Dispatchers.IO) {
        browserDao.insertNote(CreatorNote(title = title, content = content, tags = tags))
    }

    suspend fun updateNote(note: CreatorNote) = withContext(Dispatchers.IO) {
        browserDao.updateNote(note)
    }

    suspend fun deleteNote(note: CreatorNote) = withContext(Dispatchers.IO) {
        browserDao.deleteNote(note)
    }

    // Local Storage & Sandboxing security info
    suspend fun getLocalSecurityMetrics(): LocalSecurityMetrics = withContext(Dispatchers.IO) {
        val cacheSize = getDirSize(context.cacheDir) + getDirSize(context.codeCacheDir)
        val dataDir = context.filesDir.parentFile
        val totalAppDataSize = if (dataDir != null) getDirSize(dataDir) else cacheSize
        val cookieManager = CookieManager.getInstance()
        val hasCookies = cookieManager.hasCookies()

        LocalSecurityMetrics(
            sandboxPath = context.applicationInfo.dataDir,
            cacheSizeBytes = cacheSize,
            totalDataSizeBytes = totalAppDataSize,
            cookiesActive = hasCookies,
            isIsolatedSandbox = true
        )
    }

    suspend fun clearBrowserCacheAndCookies() = withContext(Dispatchers.IO) {
        // Clear cookies
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()

        // Clear web storage / DOM storage
        WebStorage.getInstance().deleteAllData()

        // Clean cache files
        deleteRecursively(context.cacheDir)
    }

    private fun getDirSize(dir: File?): Long {
        if (dir == null || !dir.exists()) return 0L
        var size = 0L
        val files = dir.listFiles() ?: return 0L
        for (file in files) {
            size += if (file.isDirectory) getDirSize(file) else file.length()
        }
        return size
    }

    private fun deleteRecursively(file: File?): Boolean {
        if (file == null || !file.exists()) return false
        if (file.isDirectory) {
            val children = file.listFiles()
            if (children != null) {
                for (child in children) {
                    deleteRecursively(child)
                }
            }
        }
        return file.delete()
    }
}

data class LocalSecurityMetrics(
    val sandboxPath: String,
    val cacheSizeBytes: Long,
    val totalDataSizeBytes: Long,
    val cookiesActive: Boolean,
    val isIsolatedSandbox: Boolean
) {
    val formattedCacheSize: String
        get() = formatBytes(cacheSizeBytes)

    val formattedTotalSize: String
        get() = formatBytes(totalDataSizeBytes)

    private fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 * 1024 -> String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
            bytes >= 1024 * 1024 -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
            bytes >= 1024 -> String.format("%.1f KB", bytes / 1024.0)
            else -> "$bytes B"
        }
    }
}
