package com.example.marketplace.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = BrandOrange,
    onPrimary = Color.White,
    secondary = Grey700,
    surface = Color(0xFF1C1C1E),
    background = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = Color(0xFF1C1C1E),
    onPrimaryContainer = BrandOrange,
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = Grey400
)

private val LightColorScheme = lightColorScheme(
    primary = BrandOrange,
    onPrimary = Color.White,
    secondary = Grey700,
    surface = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    primaryContainer = BrandOrangeLight,
    onPrimaryContainer = BrandOrange,
    surfaceVariant = Grey100,
    onSurfaceVariant = Grey700
)

@Composable
fun MarketplaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
