package com.example.marketplace.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.marketplace.MarketplaceApplication
import com.example.marketplace.ui.components.GenerateContentSheet
import com.example.marketplace.ui.screens.analytics.AnalyticsScreen
import com.example.marketplace.ui.screens.content.ContentScreen
import com.example.marketplace.ui.screens.home.HomeScreen
import com.example.marketplace.ui.screens.marketplace.MarketplaceScreen
import com.example.marketplace.ui.screens.onboarding.*
import com.example.marketplace.ui.screens.profile.ProfileScreen
import com.example.marketplace.ui.theme.Grey100
import com.example.marketplace.ui.viewmodel.OnboardingViewModel
import com.example.marketplace.ui.viewmodel.ContentViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val app = context.applicationContext as MarketplaceApplication
    
    val onboardingViewModel: OnboardingViewModel = viewModel(
        factory = OnboardingViewModel.Factory(app.businessRepository)
    )

    val contentViewModel: ContentViewModel = viewModel(
        factory = ContentViewModel.Factory(app.postRepository, app.businessRepository)
    )

    // Bypassing login & onboarding for now -> Direct to Home
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onTimeout = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onCreateAccount = { navController.navigate(Screen.Home.route) },
                onLogin = { navController.navigate(Screen.Home.route) }
            )
        }
        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = { navController.navigate(Screen.Home.route) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
                onGoToSignup = { navController.navigate(Screen.Home.route) }
            )
        }
        composable(Screen.BusinessType.route) {
            BusinessTypeScreen(
                onContinue = { navController.navigate(Screen.BusinessDetails.route) },
                onBack = { navController.popBackStack() },
                viewModel = onboardingViewModel
            )
        }
        composable(Screen.BusinessDetails.route) {
            BusinessDetailsScreen(
                onContinue = { navController.navigate(Screen.ContactLocation.route) },
                onBack = { navController.popBackStack() },
                viewModel = onboardingViewModel
            )
        }
        composable(Screen.ContactLocation.route) {
            ContactLocationScreen(
                onContinue = { navController.navigate(Screen.BrandVoice.route) },
                onBack = { navController.popBackStack() },
                viewModel = onboardingViewModel
            )
        }
        composable(Screen.BrandVoice.route) {
            BrandVoiceScreen(
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
                viewModel = onboardingViewModel
            )
        }
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Content.route) { 
            ContentScreen(
                onAddClick = { navController.navigate(Screen.GenerateContent.route) },
                viewModel = contentViewModel
            ) 
        }
        composable(Screen.GenerateContent.route) {
            Scaffold(
                containerColor = androidx.compose.ui.graphics.Color.White,
                topBar = {
                    Row(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(24.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Grey100,
                            modifier = Modifier.size(48.dp),
                            onClick = { navController.popBackStack() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.ChevronLeft, contentDescription = null)
                            }
                        }
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    GenerateContentSheet(
                        onClose = { navController.popBackStack() },
                        viewModel = contentViewModel
                    )
                }
            }
        }
        composable(Screen.Marketplace.route) { MarketplaceScreen() }
        composable(Screen.Analytics.route) { AnalyticsScreen() }
        composable(Screen.Profile.route) {
            ProfileScreen(onSignOut = {
                navController.navigate(Screen.Splash.route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
    }
}
