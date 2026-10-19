package com.example.marketplace.ui.screens.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.marketplace.ui.theme.*

@Composable
fun MarketplaceScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MarketplaceHeader()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            FilterSections()
            CreatorGrid()
        }
    }
}

@Composable
fun MarketplaceHeader() {
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Marketplace",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black
            )
            Text(
                "Hire creators near you",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun FilterSections() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listOf("All", "Reel Creator", "Influencer", "Product Photographer")) { category ->
                FilterChip(
                    selected = category == "All",
                    onClick = { },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandOrange,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
        
        LazyRow(
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listOf("All", "Mumbai", "Delhi", "Bangalore", "Hyderabad")) { location ->
                FilterChip(
                    selected = location == "All",
                    onClick = { },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (location != "All") {
                                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(location)
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandOrangeLight,
                        selectedLabelColor = BrandOrange,
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}

@Composable
fun CreatorGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(mockCreators) { creator ->
            CreatorCard(creator)
        }
    }
}

@Composable
fun CreatorCard(creator: Creator) {
    Surface(
        modifier = Modifier.aspectRatio(0.75f).fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box {
            AsyncImage(
                model = creator.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            // Scrim for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )
            
            // Rating and Status
            Row(
                modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = BrandOrange, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(creator.rating, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.size(8.dp).clip(CircleShape).background(Success)
                )
            }
            
            // Info at bottom
            Column(
                modifier = Modifier.padding(16.dp).align(Alignment.BottomStart)
            ) {
                Text(
                    creator.name,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    "${creator.role} · ${creator.location}",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = BrandOrange, fontWeight = FontWeight.Black)) {
                            append("₹${creator.price}")
                        }
                        withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.6f))) {
                            append(" / post")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

data class Creator(
    val name: String,
    val role: String,
    val location: String,
    val price: String,
    val rating: String,
    val imageUrl: String
)

val mockCreators = listOf(
    Creator("Aditi Sharma", "Reel Creator", "Mumbai", "12,000", "4.9", "https://picsum.photos/id/177/400/600"),
    Creator("Rahul Mehta", "Product Photogr...", "Delhi", "8,000", "4.8", "https://picsum.photos/id/64/400/600"),
    Creator("Sneha Kap...", "Influencer", "Bangalore", "25,000", "4.7", "https://picsum.photos/id/65/400/600"),
    Creator("Vikram Patel", "Video Editor", "Mumbai", "3,500", "4.9", "https://picsum.photos/id/101/400/600"),
)
