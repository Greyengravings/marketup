package com.example.marketplace.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector = Icons.Default.Home) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object BusinessType : Screen("business_type")
    object BusinessDetails : Screen("business_details")
    object ContactLocation : Screen("contact_location")
    object BrandVoice : Screen("brand_voice")
    object GenerateContent : Screen("generate_content")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Content : Screen("content", "Content", Icons.Default.AutoAwesome)
    object Marketplace : Screen("marketplace", "Market", Icons.Default.Groups)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.BarChart)
    object Profile : Screen("profile", "Profile", Icons.Default.AccountCircle)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Content,
    Screen.Marketplace,
    Screen.Analytics,
    Screen.Profile
)
