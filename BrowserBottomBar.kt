package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

// Neon Glow Colors for 3D Floating Bottom Bar
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
 * Fully compatible with all MainActivity caller signatures
 */
@Composable
fun BrowserBottomBar(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf(BottomNavItem.HOME) }

    BrowserBottomBarLayout(
        selectedItem = selectedItem,
        tabCount = 1,
        onItemSelected = { item ->
            selectedItem = item
        },
        modifier = modifier
    )
}

@Composable
fun BrowserBottomBar(
    selectedItem: BottomNavItem = BottomNavItem.HOME,
    onItemSelected: (BottomNavItem) -> Unit = {},
    tabCount: Int = 1,
    modifier: Modifier = Modifier
) {
    BrowserBottomBarLayout(
        selectedItem = selectedItem,
        tabCount = tabCount,
        onItemSelected = onItemSelected,
        modifier = modifier
    )
}

@Composable
fun BrowserBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    tabCount: Int = 1,
    modifier: Modifier = Modifier
) {
    val activeNav = when (currentRoute.lowercase()) {
        "bookmark", "bookmarks" -> BottomNavItem.BOOKMARK
        "search" -> BottomNavItem.SEARCH
        "tabs", "tab" -> BottomNavItem.TABS
        "settings", "setting" -> BottomNavItem.SETTINGS
        else -> BottomNavItem.HOME
    }

    BrowserBottomBarLayout(
        selectedItem = activeNav,
        tabCount = tabCount,
        onItemSelected = { item ->
            onNavigate(item.name.lowercase())
        },
        modifier = modifier
    )
}

@Composable
fun BrowserBottomBarLayout(
    selectedItem: BottomNavItem,
    tabCount: Int,
    onItemSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // 3D Floating Capsule Surface with Multi-color Neon Border and Glow Shadow
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
                    icon = if (selectedItem == BottomNavItem.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    isSelected = selectedItem == BottomNavItem.HOME,
                    onClick = { onItemSelected(BottomNavItem.HOME) },
                    modifier = Modifier.weight(1f)
                )

                // 2. Bookmark
                BottomBarItem(
                    label = "Bookmark",
                    icon = Icons.Outlined.BookmarkBorder,
                    isSelected = selectedItem == BottomNavItem.BOOKMARK,
                    onClick = { onItemSelected(BottomNavItem.BOOKMARK) },
                    modifier = Modifier.weight(1f)
                )

                // 3. Search
                BottomBarItem(
                    label = "Search",
                    icon = if (selectedItem == BottomNavItem.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                    isSelected = selectedItem == BottomNavItem.SEARCH,
                    onClick = { onItemSelected(BottomNavItem.SEARCH) },
                    modifier = Modifier.weight(1f)
                )

                // 4. Tabs Badge (Floating Box with Tab Count)
                BottomBarTabBadgeItem(
                    label = "Tabs ($tabCount)",
                    tabCount = tabCount,
                    isSelected = selectedItem == BottomNavItem.TABS,
                    onClick = { onItemSelected(BottomNavItem.TABS) },
                    modifier = Modifier.weight(1f)
                )

                // 5. Settings
                BottomBarItem(
                    label = "Settings",
                    icon = if (selectedItem == BottomNavItem.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                    isSelected = selectedItem == BottomNavItem.SETTINGS,
                    onClick = { onItemSelected(BottomNavItem.SETTINGS) },
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
            .clickable(onClick = onClick)
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
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Tab Box Icon (Numbered Square)
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
