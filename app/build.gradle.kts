package com.example.ui.components
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Message
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.BrowserTab
import com.example.viewmodel.BrowserViewModel

@SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
@Composable
fun CreatorWebView(
    tab: BrowserTab,
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var filePathCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    // PDF प्रिंट जॉब ट्रिगर
    val printJobTrigger by viewModel.printJobTrigger.collectAsState()
    LaunchedEffect(printJobTrigger) {
        if (printJobTrigger && webViewInstance != null) {
            val wv = webViewInstance
            if (wv != null) {
                try {
                    val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
                    val jobName = "${tab.title.ifBlank { "CreatorBrowser" }}_Document"
                    val printAdapter = wv.createPrintDocumentAdapter(jobName)
                    printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
                } catch (e: Exception) {
                    Toast.makeText(context, "PDF सेव करने में असमर्थ", Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.consumePrintTrigger()
        }
    }

    // लाइट / डार्क मोड बैकग्राउंड और थीम सिंक
    LaunchedEffect(isDarkMode) {
        webViewInstance?.let { wv ->
            if (isDarkMode) {
                wv.setBackgroundColor(AndroidColor.parseColor("#090A10"))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    wv.settings.isAlgorithmicDarkeningAllowed = true
                }
            } else {
                wv.setBackgroundColor(AndroidColor.WHITE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    wv.settings.isAlgorithmicDarkeningAllowed = false
                }
            }
        }
    }

    var customView by remember { mutableStateOf<View?>(null) }
    var customViewCallback by remember { mutableStateOf<WebChromeClient.CustomViewCallback?>(null) }

    BackHandler(enabled = customView != null) {
        customViewCallback?.onCustomViewHidden()
        customView = null
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    val fileChooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val clipData = result.data?.clipData
            val data = result.data?.data
            val results: Array<Uri> = when {
                clipData != null -> Array(clipData.itemCount) { i -> clipData.getItemAt(i).uri }
                data != null -> arrayOf(data)
                else -> emptyArray()
            }
            filePathCallback?.onReceiveValue(results)
        } else {
            filePathCallback?.onReceiveValue(null)
        }
        filePathCallback = null
    }

    val webAction by viewModel.webAction.collectAsState()
    LaunchedEffect(webAction) {
        val wv = webViewInstance ?: return@LaunchedEffect
        when (val action = webAction) {
            is BrowserViewModel.WebAction.LoadUrl -> {
                if (action.url.isNotBlank() && action.url != "about:blank") {
                    wv.loadUrl(action.url)
                }
                viewModel.consumeWebAction()
            }
            is BrowserViewModel.WebAction.GoBack -> {
                if (wv.canGoBack()) wv.goBack()
                viewModel.consumeWebAction()
            }
            is BrowserViewModel.WebAction.GoForward -> {
                if (wv.canGoForward()) wv.goForward()
                viewModel.consumeWebAction()
            }
            is BrowserViewModel.WebAction.Reload -> {
                wv.reload()
                viewModel.consumeWebAction()
            }
            is BrowserViewModel.WebAction.StopLoading -> {
                wv.stopLoading()
                viewModel.consumeWebAction()
            }
            is BrowserViewModel.WebAction.ToggleDesktop -> {
                wv.settings.userAgentString = if (action.enabled) {
                    BrowserViewModel.DESKTOP_USER_AGENT
                } else {
                    BrowserViewModel.MOBILE_USER_AGENT
                }
                wv.reload()
                viewModel.consumeWebAction()
            }
            is BrowserViewModel.WebAction.None -> {}
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    setBackgroundColor(if (isDarkMode) AndroidColor.parseColor("#090A10") else AndroidColor.WHITE)

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        builtInZoomControls = true
                        displayZoomControls = false
                        setSupportZoom(true)
                        mediaPlaybackRequiresUserGesture = false
                        allowFileAccess = true
                        allowContentAccess = true
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        javaScriptCanOpenWindowsAutomatically = true
                        setSupportMultipleWindows(true) // HDHub4u और पॉपअप विंडो के लिए अनिवार्य

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            isAlgorithmicDarkeningAllowed = isDarkMode
                        }

                        userAgentString = if (tab.isDesktopMode) {
                            BrowserViewModel.DESKTOP_USER_AGENT
                        } else {
                            BrowserViewModel.MOBILE_USER_AGENT
                        }
                    }

                    val cookieManager = CookieManager.getInstance()
                    cookieManager.setAcceptCookie(true)
                    cookieManager.setAcceptThirdPartyCookies(this, true)

                    var startY = 0f
