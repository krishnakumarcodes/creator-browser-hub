package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.Bookmark
import com.example.data.model.BrowserTab
import com.example.data.model.CreatorNote
import com.example.data.model.HistoryItem
import com.example.data.repository.BrowserRepository
import com.example.data.repository.LocalSecurityMetrics
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BrowserViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = BrowserRepository(database.browserDao(), application)

    companion object {
        const val DESKTOP_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"
        const val MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36"
    }

    sealed interface WebAction {
        data object None : WebAction
        data class LoadUrl(val url: String) : WebAction
        data object GoBack : WebAction
        data object GoForward : WebAction
        data object Reload : WebAction
        data object StopLoading : WebAction
        data class ToggleDesktop(val enabled: Boolean) : WebAction
    }

    private val initialTab = BrowserTab(url = "", title = "Creator Hub", isDesktopMode = true)
    private val _tabs = MutableStateFlow<List<BrowserTab>>(listOf(initialTab))
    val tabs: StateFlow<List<BrowserTab>> = _tabs.asStateFlow()

    private val _activeTabId = MutableStateFlow(initialTab.id)
    val activeTabId: StateFlow<String> = _activeTabId.asStateFlow()

    val activeTab: StateFlow<BrowserTab?> = combine(_tabs, _activeTabId) { tabList, activeId ->
        tabList.find { it.id == activeId } ?: tabList.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialTab)

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _showTabOverview = MutableStateFlow(false)
    val showTabOverview: StateFlow<Boolean> = _showTabOverview.asStateFlow()

    private val _showBookmarksHistory = MutableStateFlow(false)
    val showBookmarksHistory: StateFlow<Boolean> = _showBookmarksHistory.asStateFlow()

    private val _showSecurityDialog = MutableStateFlow(false)
    val showSecurityDialog: StateFlow<Boolean> = _showSecurityDialog.asStateFlow()

    private val _showNotesDrawer = MutableStateFlow(false)
    val showNotesDrawer: StateFlow<Boolean> = _showNotesDrawer.asStateFlow()

    private val _showDownloadsSheet = MutableStateFlow(false)
    val showDownloadsSheet: StateFlow<Boolean> = _showDownloadsSheet.asStateFlow()

    private val _showMoreMenu = MutableStateFlow(false)
    val showMoreMenu: StateFlow<Boolean> = _showMoreMenu.asStateFlow()

    private val _webAction = MutableStateFlow<WebAction>(WebAction.None)
    val webAction: StateFlow<WebAction> = _webAction.asStateFlow()

    private val _printJobTrigger = MutableStateFlow(false)
    val printJobTrigger: StateFlow<Boolean> = _printJobTrigger.asStateFlow()

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

    private val _securityMetrics = MutableStateFlow<LocalSecurityMetrics?>(null)
    val securityMetrics: StateFlow<LocalSecurityMetrics?> = _securityMetrics.asStateFlow()

    val bookmarks: StateFlow<List<Bookmark>> = repository.bookmarks
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val history: StateFlow<List<HistoryItem>> = repository.history
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val notes: StateFlow<List<CreatorNote>> = repository.notes
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadSecurityMetrics()
    }

    fun addNewTab(url: String = "", title: String = "Creator Hub", isDesktop: Boolean = true, isPrivate: Boolean = false) {
        val newTab = BrowserTab(
            url = url,
            title = if (url.isBlank()) (if (isPrivate) "Private Dashboard" else "Creator Hub") else title,
            isDesktopMode = isDesktop,
            isPrivate = isPrivate
        )
        _tabs.value = _tabs.value + newTab
        _activeTabId.value = newTab.id
        _showTabOverview.value = false
        if (url.isNotBlank()) {
            _webAction.value = WebAction.LoadUrl(url)
        }
    }

    fun switchTab(tabId: String) {
        _activeTabId.value = tabId
        _showTabOverview.value = false
    }

    fun closeTab(tabId: String) {
        val currentList = _tabs.value
        if (currentList.size <= 1) {
            val freshTab = BrowserTab(url = "", title = "Creator Hub", isDesktopMode = true)
            _tabs.value = listOf(freshTab)
            _activeTabId.value = freshTab.id
            return
        }
        val remaining = currentList.filter { it.id != tabId }
        _tabs.value = remaining
        if (_activeTabId.value == tabId) {
            _activeTabId.value = remaining.last().id
        }
    }

    fun closeAllTabs() {
        val freshTab = BrowserTab(url = "", title = "Creator Hub", isDesktopMode = true)
        _tabs.value = listOf(freshTab)
        _activeTabId.value = freshTab.id
        _showTabOverview.value = false
    }

    fun loadUrl(url: String) {
        val active = activeTab.value ?: return
        val cleanUrl = when {
            url.startsWith("http://") || url.startsWith("https://") -> url
            url.contains(".") -> "https://$url"
            else -> "https://www.google.com/search?q=$url"
        }
        updateActiveTab { it.copy(url = cleanUrl, title = cleanUrl) }
        _webAction.value = WebAction.LoadUrl(cleanUrl)
    }

    fun reload() {
        _webAction.value = WebAction.Reload
    }

    fun goBack() {
        _webAction.value = WebAction.GoBack
    }

    fun goForward() {
        _webAction.value = WebAction.GoForward
    }

    fun navigateHome() {
        updateActiveTab { it.copy(url = "", title = "Creator Hub") }
        _webAction.value = WebAction.None
    }

    fun toggleDesktopMode() {
        val active = activeTab.value ?: return
        val newMode = !active.isDesktopMode
        updateActiveTab { it.copy(isDesktopMode = newMode) }
        _webAction.value = WebAction.ToggleDesktop(newMode)
    }

    fun toggleThemeMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun triggerPrintJob() {
        _printJobTrigger.value = true
    }

    fun consumePrintTrigger() {
        _printJobTrigger.value = false
    }

    fun consumeWebAction() {
        _webAction.value = WebAction.None
    }

    fun onPageStarted(url: String) {
        updateActiveTab { it.copy(url = url, isLoading = true) }
    }

    fun onPageFinished(url: String, title: String?, canGoBack: Boolean, canGoForward: Boolean) {
        updateActiveTab {
            it.copy(
                url = url,
                title = title ?: it.title,
                isLoading = false,
                canGoBack = canGoBack,
                canGoForward = canGoForward
            )
        }
        viewModelScope.launch {
            if (url.isNotBlank() && !url.startsWith("about:") && !url.startsWith("creatorhub:")) {
                repository.recordHistory(title ?: url, url)
            }
        }
    }

    fun onProgressChanged(progress: Int) {
        updateActiveTab { it.copy(progress = progress, isLoading = progress < 100) }
    }

    fun onTitleReceived(title: String) {
        updateActiveTab { it.copy(title = title) }
    }

    fun setShowTabOverview(show: Boolean) { _showTabOverview.value = show }
    fun setShowBookmarksHistory(show: Boolean) { _showBookmarksHistory.value = show }
    fun setShowSecurityDialog(show: Boolean) {
        _showSecurityDialog.value = show
        if (show) loadSecurityMetrics()
    }
    fun setShowNotesDrawer(show: Boolean) { _showNotesDrawer.value = show }
    fun setShowDownloadsSheet(show: Boolean) { _showDownloadsSheet.value = show }
    fun setShowMoreMenu(show: Boolean) { _showMoreMenu.value = show }

    fun showMessage(msg: String) { _uiMessage.value = msg }
    fun dismissUiMessage() { _uiMessage.value = null }

    fun addBookmark(title: String, url: String) {
        viewModelScope.launch { repository.addBookmark(title, url) }
    }

    fun deleteBookmark(bookmark: Bookmark) {
        viewModelScope.launch { repository.removeBookmark(bookmark) }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch { repository.deleteHistoryItem(id) }
    }

    fun clearAllHistory() {
        viewModelScope.launch { repository.clearHistory() }
    }

    fun saveNote(title: String, content: String, tags: String) {
        viewModelScope.launch { repository.saveNote(title, content, tags) }
    }

    fun deleteNote(note: CreatorNote) {
        viewModelScope.launch { repository.deleteNote(note) }
    }

    fun clearCacheAndCookies() {
        viewModelScope.launch {
            repository.clearBrowserCacheAndCookies()
            loadSecurityMetrics()
            showMessage("कैश और कुकीज़ सफलतापूर्वक साफ़ कर दी गईं!")
        }
    }

    private fun loadSecurityMetrics() {
        viewModelScope.launch {
            _securityMetrics.value = repository.getLocalSecurityMetrics()
        }
    }

    private fun updateActiveTab(transform: (BrowserTab) -> BrowserTab) {
        val activeId = _activeTabId.value
        _tabs.value = _tabs.value.map { tab ->
            if (tab.id == activeId) transform(tab) else tab
        }
    }
}

