package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.BrowserTab
import com.example.data.model.CreatorShortcut
import com.example.viewmodel.BrowserViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Creator Browser Hub", appName)
  }

  @Test
  fun `browser tab defaults to desktop mode`() {
    val tab = BrowserTab()
    assertTrue("Tab must default to desktop mode", tab.isDesktopMode)
    assertTrue("New tab without URL is home", tab.isHome)
    assertEquals("Creator Hub", tab.title)
  }

  @Test
  fun `creator shortcuts list contains top studio platforms`() {
    val shortcuts = CreatorShortcut.DEFAULT_SHORTCUTS
    val ids = shortcuts.map { it.id }
    assertTrue(ids.contains("yt_studio"))
    assertTrue(ids.contains("ig_web"))
    assertTrue(ids.contains("fb_creator"))
    assertTrue(ids.contains("gmail"))

    val ytStudio = shortcuts.find { it.id == "yt_studio" }
    assertEquals("https://studio.youtube.com", ytStudio?.url)
  }

  @Test
  fun `desktop user agent contains Windows Chrome identifier`() {
    assertTrue(BrowserViewModel.DESKTOP_USER_AGENT.contains("Windows NT 10.0"))
    assertTrue(BrowserViewModel.DESKTOP_USER_AGENT.contains("Chrome/"))
  }
}
