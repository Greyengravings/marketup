package com.example.marketplace.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.marketplace.ui.theme.BrandOrange

@Composable
fun DataSecurityScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(44.dp),
                    onClick = onBack
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.ChevronLeft,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Text(
                    "Data Security & Sovereignty",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(shape = CircleShape, color = BrandOrange) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(10.dp).size(22.dp)
                        )
                    }
                    Text(
                        "Your Data Privacy Guarantee",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "MarketUp is engineered around the principle of data minimisation and client-side data sovereignty. Here is exactly how your data is protected.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Legal Clause 1: Registration Email & Authentication
            SecurityClauseCard(
                title = "1. Identity & Registration Email Handling",
                icon = Icons.Default.Lock,
                text = "Only your registration email address and encrypted authentication session tokens are transmitted and managed by our secure cloud identity providers (AWS Cognito / OAuth 2.0). Your password and personal authentication credentials are processed via TLS 1.3 encryption and are never stored in plaintext."
            )

            // Legal Clause 2: Local Device Storage Sovereignty
            SecurityClauseCard(
                title = "2. Local Device Storage & Data Sovereignty",
                icon = Icons.Default.PhonelinkSetup,
                text = "All of your business profiles, financial parameters, sector niches, contact information, target demographics, brand voice preferences, social handles, and generated content remain strictly stored on your local device's encrypted storage (Android DataStore). This business metadata is never uploaded to, aggregated by, or synchronized with central telemetry databases."
            )

            // Legal Clause 3: AI Inference Processing Policy
            SecurityClauseCard(
                title = "3. AI Content Generation Processing",
                icon = Icons.Default.AutoAwesome,
                text = "When you choose to generate content with AI, only the specific creative prompt parameters (e.g. occasion, post type, brand tone) and necessary brand profile context are transmitted via secure HTTPS APIs to run model inference. The resulting generated posts and creatives are stored directly on your local device."
            )

            // Legal Clause 4: Asset Rights & Ownership
            SecurityClauseCard(
                title = "4. Asset Rights & Zero Telemetry Tracking",
                icon = Icons.Default.VerifiedUser,
                text = "You retain 100% legal ownership, trademark rights, and operational control over all business data entered and AI content generated within MarketUp. MarketUp does not track user behavior across other applications or monetize user business records."
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SecurityClauseCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = BrandOrange,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )
        }
    }
}
