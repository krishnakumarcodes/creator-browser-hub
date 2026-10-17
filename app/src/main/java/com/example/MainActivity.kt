package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.BrowserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val browserViewModel: BrowserViewModel = viewModel()
            val isDarkMode by browserViewModel.isDarkMode.collectAsState()

            MyApplicationTheme(darkTheme = isDarkMode) {
                CreatorBrowserApp(viewModel = browserViewModel)
            }
        }
    }
}

@Composable
fun CreatorBrowserApp(
    viewModel: BrowserViewModel = viewModel()
) {
    val activeTab by viewModel.activeTab.collectAsState()
    val tabs by viewModel.tabs.collectAsState()
    val showTabOverview by viewModel.showTabOverview.collectAsState()
    val showBookmarksHistory by viewModel.showBookmarksHistory.collectAsState()
    val showSecurityDialog by viewModel.showSecurityDialog.collectAsState()
    val showNotesDrawer by viewModel.showNotesDrawer.collectAsState()
    val showDownloadsSheet by viewModel.showDownloadsSheet.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentBgColor = if (isDarkMode) DarkBackground else Color(0xFFF8FAFC)

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissUiMessage()
        }
    }

    BackHandler(
        enabled = showTabOverview ||
                showBookmarksHistory ||
                showSecurityDialog ||
                showNotesDrawer ||
                showDownloadsSheet ||
                (activeTab?.canGoBack == true) ||
                (activeTab?.isHome == false) ||
                tabs.size > 1
    ) {
        when {
            showTabOverview -> viewModel.setShowTabOverview(false)
            showBookmarksHistory -> viewModel.setShowBookmarksHistory(false)
            showSecurityDialog -> viewModel.setShowSecurityDialog(false)
            showNotesDrawer -> viewModel.setShowNotesDrawer(false)
            showDownloadsSheet -> viewModel.setShowDownloadsSheet(false)
            activeTab?.canGoBack == true -> viewModel.goBack()
            activeTab?.isHome == false -> viewModel.navigateHome()
            tabs.size > 1 -> activeTab?.let { viewModel.closeTab(it.id) }
        }
    }

    activeTab?.let { currentTab ->
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(currentBgColor),
            containerColor = currentBgColor,
            topBar = {
                BrowserTopBar(
                    tab = currentTab,
                    viewModel = viewModel
                )
            },
            bottomBar = {
                BrowserBottomBar(
                    tab = currentTab,
                    tabCount = tabs.size,
                    viewModel = viewModel
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(currentBgColor)
            ) {
                Crossfade(
                    targetState = currentTab.isHome,
                    label = "ContentCrossfade"
                ) { isHome ->
                    if (isHome) {
                        CreatorDashboardView(viewModel = viewModel)
                    } else {
                        key(currentTab.id) {
                            CreatorWebView(
                                tab = currentTab,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }

            if (showTabOverview) {
                TabOverviewSheet(viewModel = viewModel)
            }
            if (showBookmarksHistory) {
                BookmarksHistoryDialog(viewModel = viewModel)
            }
            if (showSecurityDialog) {
                SecurityPrivacyDialog(viewModel = viewModel)
            }
            if (showNotesDrawer) {
                CreatorNotesDrawer(viewModel = viewModel)
            }
            if (showDownloadsSheet) {
                DownloadsDialog(viewModel = viewModel)
            }
        }
    }
}
