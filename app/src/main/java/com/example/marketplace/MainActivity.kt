package com.example.marketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.marketplace.ui.navigation.AppNavigation
import com.example.marketplace.ui.navigation.bottomNavItems
import com.example.marketplace.ui.theme.MarketplaceTheme
import com.example.marketplace.ui.theme.BrandOrange
import com.example.marketplace.ui.theme.Grey600

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketplaceTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route !in listOf(
        com.example.marketplace.ui.navigation.Screen.Splash.route,
        com.example.marketplace.ui.navigation.Screen.Welcome.route,
        com.example.marketplace.ui.navigation.Screen.Login.route,
        com.example.marketplace.ui.navigation.Screen.Signup.route,
        com.example.marketplace.ui.navigation.Screen.BusinessType.route,
        com.example.marketplace.ui.navigation.Screen.BusinessDetails.route,
        com.example.marketplace.ui.navigation.Screen.ContactLocation.route,
        com.example.marketplace.ui.navigation.Screen.BrandVoice.route,
        com.example.marketplace.ui.navigation.Screen.GenerateContent.route
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                StandardBottomNav(navController = navController)
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = if (showBottomBar) {
                        padding.calculateBottomPadding()
                    } else {
                        0.dp
                    }
                )
        ) {
            AppNavigation(navController = navController)
        }
    }
}
@Composable
fun StandardBottomNav(navController: androidx.navigation.NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier.height(88.dp)
        ) {
            bottomNavItems.forEach { screen ->
                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    label = {
                        Text(
                            screen.title,
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandOrange,
                        selectedTextColor = BrandOrange,
                        unselectedIconColor = Grey600,
                        unselectedTextColor = Grey600,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
