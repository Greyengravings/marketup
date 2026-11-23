package com.example.marketplace.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.marketplace.ui.navigation.Screen
import com.example.marketplace.ui.theme.BrandOrange

data class TourStepData(
    val stepNumber: Int,
    val title: String,
    val highlightLabel: String,
    val description: String,
    val targetRoute: String,
    val icon: ImageVector
)

val tourSteps = listOf(
    TourStepData(
        stepNumber = 1,
        title = "Workspace Overview",
        highlightLabel = "Dashboard & Summary",
        description = "Welcome to MarketUp! Here you can view your business profile, connected social accounts, and recently generated posts at a glance.",
        targetRoute = Screen.Home.route,
        icon = Icons.Default.Home
    ),
    TourStepData(
        stepNumber = 2,
        title = "AI Content Generator",
        highlightLabel = "Content Hub",
        description = "Create engaging social media captions, hashtags, and AI graphics customized to your business sector and brand voice.",
        targetRoute = Screen.Content.route,
        icon = Icons.Default.AutoAwesome
    ),
    TourStepData(
        stepNumber = 3,
        title = "Business Marketplace",
        highlightLabel = "Growth Templates",
        description = "Explore curated marketing templates, promotional tools, and partner offers to expand your business reach.",
        targetRoute = Screen.Marketplace.route,
        icon = Icons.Default.Storefront
    ),
    TourStepData(
        stepNumber = 4,
        title = "Performance Analytics",
        highlightLabel = "Metrics & Insights",
        description = "Track your content performance, post engagement metrics, and growth trends over time.",
        targetRoute = Screen.Analytics.route,
        icon = Icons.Default.Analytics
    ),
    TourStepData(
        stepNumber = 5,
        title = "Profile, Settings & Help",
        highlightLabel = "Account & Settings",
        description = "Manage your business profile details, brand voice, social handles, and theme settings. You can restart this tour anytime from Help!",
        targetRoute = Screen.Profile.route,
        icon = Icons.Default.Person
    )
)

@Composable
fun AppTourOverlay(
    navController: NavHostController,
    isTourActive: Boolean,
    currentStepIndex: Int,
    onStepChange: (newIndex: Int) -> Unit,
    onFinishTour: () -> Unit
) {
    if (!isTourActive) return

    val currentStep = tourSteps.getOrNull(currentStepIndex) ?: return

    // Auto-navigate to target screen if not already there
    LaunchedEffect(currentStepIndex) {
        val target = currentStep.targetRoute
        if (navController.currentDestination?.route != target) {
            navController.navigate(target) {
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.65f))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(18.dp)
    ) {
        // Floating Tooltip Card at the bottom/center
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF141416),
            border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(BrandOrange, Color(0xFFFF9A69)))),
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header: Step Counter & Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = BrandOrange.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, BrandOrange.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                currentStep.icon,
                                contentDescription = null,
                                tint = BrandOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "QUICK TOUR • Step ${currentStep.stepNumber} of ${tourSteps.size}",
                                color = BrandOrange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = onFinishTour,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close Tour",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Highlight Area Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.08f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.TouchApp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            currentStep.highlightLabel,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Title & Description
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        currentStep.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        currentStep.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )
                }

                // Step Dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(tourSteps.size) { index ->
                        val active = index == currentStepIndex
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (active) 10.dp else 6.dp)
                                .background(
                                    color = if (active) BrandOrange else Color.White.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        )
                    }
                }

                // Navigation Controls (Previous / Next / Finish)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            if (currentStepIndex > 0) {
                                onStepChange(currentStepIndex - 1)
                            }
                        },
                        enabled = currentStepIndex > 0,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            1.dp,
                            if (currentStepIndex > 0) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            disabledContentColor = Color.White.copy(alpha = 0.3f)
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Previous", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            if (currentStepIndex < tourSteps.size - 1) {
                                onStepChange(currentStepIndex + 1)
                            } else {
                                onFinishTour()
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
                    ) {
                        if (currentStepIndex < tourSteps.size - 1) {
                            Text("Next", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        } else {
                            Text("Finish Tour", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(6.dp))
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
