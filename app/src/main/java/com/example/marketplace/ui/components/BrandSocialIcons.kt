package com.example.marketplace.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketplace.ui.theme.BrandOrange

@Composable
fun BrandSocialLogo(
    platform: String,
    modifier: Modifier = Modifier.size(34.dp)
) {
    val norm = platform.lowercase().trim()
    when {
        norm.contains("instagram") || norm == "ig" -> {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(10.dp),
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045))
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Instagram",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        norm.contains("facebook") || norm == "fb" -> {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF1877F2)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("f", color = Color.White, fontWeight = FontWeight.Black, fontSize = 21.sp)
                }
            }
        }
        norm.contains("twitter") || norm == "x" -> {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF000000)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("X", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                }
            }
        }
        norm.contains("linkedin") -> {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF0A66C2)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("in", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
            }
        }
        norm.contains("whatsapp") || norm == "wa" -> {
            Surface(
                modifier = modifier,
                shape = CircleShape,
                color = Color(0xFF25D366)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "WhatsApp",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        norm.contains("youtube") || norm == "yt" -> {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFF0000)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "YouTube",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
        norm.contains("pinterest") -> {
            Surface(
                modifier = modifier,
                shape = CircleShape,
                color = Color(0xFFE60023)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("P", color = Color.White, fontWeight = FontWeight.Black, fontSize = 19.sp)
                }
            }
        }
        else -> {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(10.dp),
                color = BrandOrange
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Language,
                        contentDescription = platform,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
