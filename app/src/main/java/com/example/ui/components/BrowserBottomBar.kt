package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BrowserTab
import com.example.viewmodel.BrowserViewModel

val BottomBarBg = Color(0xFF110E28).copy(alpha = 0.95f)
val BottomBorder = Color(0xFF2A2355)
val IconActiveColor = Color(0xFFC084FC)
val IconInactiveColor = Color(0xFF7E7A9E)

@Composable
fun BrowserBottomBar(
    tab: BrowserTab,
    tabCount: Int,
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = BottomBarBg,
        border = BorderStroke(1.dp, BottomBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Filled.Home,
                label = "Home",
                isSelected = tab.isHome,
                onClick = { viewModel.navigateHome() }
            )
            BottomNavItem(
                icon = Icons.Outlined.BookmarkBorder,
                label = "Bookmark",
                isSelected = false,
                onClick = { viewModel.setShowBookmarksHistory(true) }
            )
            BottomNavItem(
                icon = Icons.Default.Search,
                label = "Search",
                isSelected = false,
                onClick = { viewModel.navigateHome() }
            )
            BottomNavItem(
                icon = Icons.Default.Filter1,
                label = "Tabs ($tabCount)",
                isSelected = false,
                onClick = { viewModel.setShowTabOverview(true) }
            )
            BottomNavItem(
                icon = Icons.Outlined.Settings,
                label = "Settings",
                isSelected = false,
                onClick = { viewModel.setShowMoreMenu(true) }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) IconActiveColor else IconInactiveColor,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            color = if (isSelected) IconActiveColor else IconInactiveColor,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
