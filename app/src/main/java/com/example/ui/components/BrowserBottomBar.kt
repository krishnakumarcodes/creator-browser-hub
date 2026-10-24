package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.BrowserViewModel

// Neon Glow Colors for 3D Floating Capsule
val BottomBarBg = Color(0xFF13152C).copy(alpha = 0.95f)
val NeonActivePurple = Color(0xFFA855F7)
val NeonActivePink = Color(0xFFEC4899)
val NeonActiveCyan = Color(0xFF06B6D4)
val InactiveIconColor = Color(0xFF8E8EA8)

enum class BottomNavItem(val title: String) {
    HOME("Home"),
    BOOKMARK("Bookmark"),
    SEARCH("Search"),
    TABS("Tabs"),
    SETTINGS("Settings")
}

/**
 * 3D Floating Neon Capsule Bottom Bar
 * Universal Composable supporting all possible parameter calls from MainActivity
 */
@Composable
fun BrowserBottomBar(
    modifier: Modifier = Modifier,
    viewModel: BrowserViewModel? = null,
    selectedItem: Any? = null,
    onItemSelected: ((Any) -> Unit)? = null,
    currentRoute: String = "home",
    onNavigate: ((String) -> Unit)? = null,
    tabCount: Int = 1,
    // Callback overloads
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onBookmarksClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onTabClick: () -> Unit = {},
    onTabsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onNewTabClick: () -> Unit = {},
    canGoBack: Boolean = false,
    canGoForward: Boolean = false,
    onBackClick: () -> Unit = {},
    onForwardClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {}
) {
    var internalSelected by remember { mutableStateOf(BottomNavItem.HOME) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // 3D Floating Capsule Surface
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = BottomBarBg,
            border = BorderStroke(
                1.5.dp,
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF8B5CF6).copy(alpha = 0.85f),
                        Color(0xFFEC4899).copy(alpha = 0.65f),
                        Color(0xFF06B6D4).copy(alpha = 0.85f)
                    )
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = NeonActivePurple.copy(alpha = 0.35f),
                    spotColor = NeonActiveCyan.copy(alpha = 0.35f)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Home
                BottomBarItem(
                    label = "Home",
                    icon = if (internalSelected == BottomNavItem.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    isSelected = internalSelected == BottomNavItem.HOME,
                    onClick = {
                        internalSelected = BottomNavItem.HOME
                        onItemSelected?.invoke(BottomNavItem.HOME)
                        onNavigate?.invoke("home")
                        onHomeClick()
                    },
                    modifier = Modifier.weight(1f)
                )

                // 2. Bookmark
                BottomBarItem(
                    label = "Bookmark",
                    icon = Icons.Outlined.BookmarkBorder,
                    isSelected = internalSelected == BottomNavItem.BOOKMARK,
                    onClick = {
                        internalSelected = BottomNavItem.BOOKMARK
                        onItemSelected?.invoke(BottomNavItem.BOOKMARK)
                        onNavigate?.invoke("bookmark")
                        onBookmarkClick()
                        onBookmarksClick()
                    },
                    modifier = Modifier.weight(1f)
                )

                // 3. Search
                BottomBarItem(
                    label = "Search",
                    icon = if (internalSelected == BottomNavItem.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                    isSelected = internalSelected == BottomNavItem.SEARCH,
                    onClick = {
                        internalSelected = BottomNavItem.SEARCH
                        onItemSelected?.invoke(BottomNavItem.SEARCH)
                        onNavigate?.invoke("search")
                        onSearchClick()
                    },
                    modifier = Modifier.weight(1f)
                )

                // 4. Tabs Badge (3D Square Box)
                BottomBarTabBadgeItem(
                    label = "Tabs ($tabCount)",
                    tabCount = tabCount,
                    isSelected = internalSelected == BottomNavItem.TABS,
                    onClick = {
                        internalSelected = BottomNavItem.TABS
                        onItemSelected?.invoke(BottomNavItem.TABS)
                        onNavigate?.invoke("tabs")
                        onTabClick()
                        onTabsClick()
                    },
                    modifier = Modifier.weight(1f)
                )

                // 5. Settings
                BottomBarItem(
                    label = "Settings",
                    icon = if (internalSelected == BottomNavItem.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                    isSelected = internalSelected == BottomNavItem.SETTINGS,
                    onClick = {
                        internalSelected = BottomNavItem.SETTINGS
                        onItemSelected?.invoke(BottomNavItem.SETTINGS)
                        onNavigate?.invoke("settings")
                        onSettingsClick()
                        onMenuClick()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun BottomBarItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (isSelected) NeonActivePurple else InactiveIconColor
    val textFontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 24.dp),
                onClick = onClick
            )
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = label,
            color = contentColor,
            fontSize = 10.sp,
            fontWeight = textFontWeight,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun BottomBarTabBadgeItem(
    label: String,
    tabCount: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (isSelected) NeonActiveCyan else InactiveIconColor
    val textFontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 24.dp),
                onClick = onClick
            )
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color.Transparent,
            border = BorderStroke(1.8.dp, contentColor),
            modifier = Modifier.size(20.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = tabCount.toString(),
                    color = contentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
        Text(
            text = label,
            color = contentColor,
            fontSize = 10.sp,
            fontWeight = textFontWeight,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(top = 3.dp)
        )
    }
}
