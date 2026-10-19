package com.example.marketplace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.marketplace.ui.theme.*

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    blurIntensity: Dp = 20.dp,
    contentPadding: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) GlassDark else GlassLight
    val borderColor = if (isDark) GlassBorderDark else GlassBorderLight

    Box(modifier = modifier) {
        // Blurred Background Layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(cornerRadius),
                    ambientColor = if (isDark) Color(0x33007AFF) else Color(0x1A000000),
                    spotColor = if (isDark) Color(0x33007AFF) else Color(0x1A000000)
                )
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
                .border(
                    width = 0.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(borderColor, Color.Transparent)
                    ),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .blur(blurIntensity)
        )

        // Sharp Content Layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun FloatingGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    GlassSurface(
        modifier = modifier,
        cornerRadius = 32.dp,
        blurIntensity = 30.dp,
        content = content
    )
}
