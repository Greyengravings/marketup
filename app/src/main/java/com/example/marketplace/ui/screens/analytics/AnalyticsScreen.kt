package com.example.marketplace.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketplace.ui.theme.*

@Composable
fun AnalyticsScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AnalyticsHeader()
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item {
                MetricGrid()
            }
            item {
                AdPerformanceCard()
            }
            item {
                PlatformList()
            }
            item {
                DemographicsSection()
            }
            item {
                BestPostSection()
            }
        }
    }
}

@Composable
fun AnalyticsHeader() {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            "Analytics",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black
        )
        Text(
            "Last 7 days performance",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MetricGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AnalyticsMetricCard(
                label = "Engagement",
                value = "4,800",
                trend = "+12%",
                icon = Icons.Default.Visibility,
                modifier = Modifier.weight(1f)
            )
            AnalyticsMetricCard(
                label = "Followers",
                value = "+690",
                trend = "This week",
                icon = Icons.Default.Group,
                modifier = Modifier.weight(1f),
                trendColor = Success
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AnalyticsMetricCard(
                label = "Clicks",
                value = "2,400",
                trend = "+8%",
                icon = Icons.Default.Link,
                modifier = Modifier.weight(1f)
            )
            AnalyticsMetricCard(
                label = "Leads",
                value = "249",
                trend = "7.8% CR",
                icon = Icons.Default.Payments,
                modifier = Modifier.weight(1f),
                trendColor = Success
            )
        }
    }
}

@Composable
fun AnalyticsMetricCard(
    label: String,
    value: String,
    trend: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    trendColor: Color = Success
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Surface(
                shape = CircleShape,
                color = BrandOrange.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = BrandOrange, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(trend, style = MaterialTheme.typography.labelLarge, color = trendColor, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun AdPerformanceCard() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Ad performance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Ad spend", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("₹18,117", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("ROI", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("4.5x", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = BrandOrange)
                }
            }
        }
    }
}

@Composable
fun PlatformList() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("By platform", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        PlatformItem("Instagram", "21,600 reach · 3,600 engagements", "53%", Icons.Default.CameraAlt)
        PlatformItem("Facebook", "10,800 reach · 1,200 engagements", "26%", Icons.Default.Facebook)
        PlatformItem("Linkedin", "4,800 reach · 600 engagements", "12%", Icons.Default.Work)
        PlatformItem("Youtube", "3,600 reach · 360 engagements", "9%", Icons.Default.PlayCircle)
    }
}

@Composable
fun PlatformItem(name: String, stats: String, percentage: String, icon: ImageVector) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text(stats, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(percentage, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = BrandOrange)
        }
    }
}

@Composable
fun DemographicsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Audience demographics", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                DemographicBar("18-24", 0.22f, "22%")
                DemographicBar("25-34", 0.41f, "41%")
                DemographicBar("35-44", 0.24f, "24%")
                DemographicBar("45+", 0.13f, "13%")
            }
        }
    }
}

@Composable
fun DemographicBar(label: String, progress: Float, percentage: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.width(60.dp), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.weight(1f).height(12.dp).clip(CircleShape),
            color = BrandOrange,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text(percentage, modifier = Modifier.width(60.dp), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}

@Composable
fun BestPostSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Best performing post", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏆", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Diwali Launch Reel", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(48.dp)) {
                    Column {
                        Text("Reach", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("14,400", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    }
                    Column {
                        Text("Engagement", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("2,400", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}
