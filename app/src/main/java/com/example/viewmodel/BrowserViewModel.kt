    fun addNewTab(url: String = "", title: String = "Creator Hub", isDesktop: Boolean = true, isPrivate: Boolean = false) {
        val newTab = BrowserTab(
            url = url,
            title = if (url.isBlank()) (if (isPrivate) "Private Dashboard" else "Creator Hub") else title,
            isDesktopMode = isDesktop,
            isPrivate = isPrivate
        )
        _tabs.value = _tabs.value + newTab
        _activeTabId.value = newTab.id
        _urlInputText.value = url
        _showTabOverview.value = false
        if (url.isNotBlank()) {
            _webAction.value = WebAction.LoadUrl(url)
        }
        checkBookmarkStatus(url)
    }
    
