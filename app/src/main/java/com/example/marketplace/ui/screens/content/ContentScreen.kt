package com.example.marketplace.ui.screens.content

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import coil.compose.AsyncImage

import com.example.marketplace.data.models.PostResponse
import com.example.marketplace.ui.theme.*
import com.example.marketplace.ui.viewmodel.ContentViewModel


// ============================================================
// CONTENT SCREEN
// ============================================================

@Composable
fun ContentScreen(
    onAddClick: () -> Unit,
    viewModel: ContentViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            ContentHeader()
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = BrandOrange,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 100.dp, end = 16.dp)
                    .size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add content",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            ContentFilterChips()

            when (val state = uiState) {

                // ------------------------------------------------
                // LOADING
                // ------------------------------------------------

                is ContentViewModel.ContentUiState.Loading -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 20.dp,
                            vertical = 12.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        item {
                            SkeletonPostCard()
                        }
                    }
                }

                // ------------------------------------------------
                // GENERATING AI CONTENT
                // ------------------------------------------------

                is ContentViewModel.ContentUiState.Generating -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 20.dp,
                            vertical = 12.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        item {
                            SkeletonPostCard(
                                showGeneratingMessage = true
                            )
                        }
                    }
                }

                // ------------------------------------------------
                // SUCCESS
                // ------------------------------------------------

                is ContentViewModel.ContentUiState.Success -> {

                    if (state.posts.isEmpty()) {

                        EmptyContentState()

                    } else {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = 20.dp,
                                vertical = 12.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {

                            items(
                                items = state.posts,
                                key = { post -> post.id }
                            ) { post ->

                                PostItem(
                                    post = post,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }

                // ------------------------------------------------
                // ERROR
                // ------------------------------------------------

                is ContentViewModel.ContentUiState.Error -> {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}


// ============================================================
// SKELETON POST CARD
// ============================================================

@Composable
fun SkeletonPostCard(
    showGeneratingMessage: Boolean = false
) {

    val infiniteTransition = rememberInfiniteTransition(
        label = "post_skeleton"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.30f,
        targetValue = 0.80f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeleton_alpha"
    )

    val skeletonColor = Grey200.copy(alpha = alpha)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.dp,
            Grey100
        ),
        shadowElevation = 2.dp
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // Occasion and status placeholders

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(105.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(skeletonColor)
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(50))
                        .background(skeletonColor)
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // Generated image placeholder

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(skeletonColor),
                contentAlignment = Alignment.Center
            ) {

                if (showGeneratingMessage) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Surface(
                            shape = CircleShape,
                            color = BrandOrange.copy(alpha = 0.12f),
                            modifier = Modifier.size(64.dp)
                        ) {

                            Box(
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = BrandOrange,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Text(
                            text = "Creating your content",
                            style = MaterialTheme.typography.titleMedium,
                            color = Grey600,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "AI is generating your creative...",
                            style = MaterialTheme.typography.bodySmall,
                            color = Grey600,
                            textAlign = TextAlign.Center
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        CircularProgressIndicator(
                            color = BrandOrange,
                            modifier = Modifier.size(26.dp),
                            strokeWidth = 3.dp
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // Download and share placeholders

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Box(
                    modifier = Modifier
                        .width(112.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(skeletonColor)
                )

                Box(
                    modifier = Modifier
                        .width(126.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(skeletonColor)
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // Caption placeholders

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(skeletonColor)
            )

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.80f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(skeletonColor)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // Hashtag placeholder

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(13.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(skeletonColor)
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // Approval action placeholders

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(skeletonColor)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(skeletonColor)
                )
            }
        }
    }
}


// ============================================================
// POST STATUS BADGE
// ============================================================

@Composable
fun PostStatusBadge(
    status: String
) {

    val normalizedStatus = status.trim().lowercase()

    val label: String
    val containerColor: Color
    val contentColor: Color
    val icon: androidx.compose.ui.graphics.vector.ImageVector

    when (normalizedStatus) {

        "pending" -> {

            label = "Pending"
            containerColor = Color(0xFFFFF4E5)
            contentColor = Color(0xFFB76E00)
            icon = Icons.Default.HourglassTop
        }

        "approved" -> {

            label = "Approved"
            containerColor = Color(0xFFE8F7EE)
            contentColor = Color(0xFF16834A)
            icon = Icons.Default.CheckCircle
        }

        "rejected" -> {

            label = "Rejected"
            containerColor = Color(0xFFFFEBEE)
            contentColor = Color(0xFFC62828)
            icon = Icons.Default.Cancel
        }

        "scheduled" -> {

            label = "Scheduled"
            containerColor = Color(0xFFEDE9FE)
            contentColor = Color(0xFF6D43C6)
            icon = Icons.Default.Schedule
        }

        else -> {

            label = normalizedStatus
                .replaceFirstChar { it.uppercase() }
                .ifBlank { "Unknown" }

            containerColor = Grey100
            contentColor = Grey600
            icon = Icons.Default.HourglassTop
        }
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = containerColor
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = contentColor
            )

            Text(
                text = label,
                color = contentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}


// ============================================================
// POST ITEM CARD
// ============================================================

@Composable
fun PostItem(
    post: PostResponse,
    viewModel: ContentViewModel
) {

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.dp,
            Grey100
        ),
        shadowElevation = 2.dp
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // --------------------------------------------
            // OCCASION AND STATUS
            // --------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BrandOrange.copy(alpha = 0.10f),
                    modifier = Modifier.weight(1f, fill = false)
                ) {

                    Text(
                        text = post.occasion
                            ?.takeIf { it.isNotBlank() }
                            ?: "General Post",
                        modifier = Modifier
                            .widthIn(max = 160.dp)
                            .padding(
                                horizontal = 10.dp,
                                vertical = 7.dp
                            ),
                        style = MaterialTheme.typography.labelMedium,
                        color = BrandOrange,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                PostStatusBadge(
                    status = post.status
                )
            }

            // --------------------------------------------
            // GENERATED IMAGE
            // --------------------------------------------

            if (!post.generatedImage.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                AsyncImage(
                    model = post.generatedImage,
                    contentDescription = "Generated creative",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // ----------------------------------------
                // DOWNLOAD AND SHARE
                // ----------------------------------------

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    AssistChip(
                        onClick = {
                            downloadImageToDevice(
                                context = context,
                                imageUrl = post.generatedImage,
                                title = post.occasion ?: "Post"
                            )
                        },
                        label = {
                            Text("Download")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    AssistChip(
                        onClick = {
                            sharePostToSystem(
                                context = context,
                                caption = post.caption,
                                hashtags = post.hashtags
                            )
                        },
                        label = {
                            Text("Share Post")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // --------------------------------------------
            // CAPTION
            // --------------------------------------------

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = post.caption,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // --------------------------------------------
            // HASHTAGS
            // --------------------------------------------

            if (post.hashtags.isNotEmpty()) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = post.hashtags.joinToString(" "),
                    style = MaterialTheme.typography.bodySmall,
                    color = Info
                )
            }

            // --------------------------------------------
            // APPROVAL ACTIONS
            // --------------------------------------------

            if (post.status.equals("pending", ignoreCase = true)) {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Reject

                    OutlinedButton(
                        onClick = {
                            viewModel.rejectPost(post.id)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFE57373)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFC62828)
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Reject",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Approve

                    Button(
                        onClick = {
                            viewModel.approvePost(post.id)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF16834A),
                            contentColor = Color.White
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Approve",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}


// ============================================================
// DOWNLOAD GENERATED IMAGE
// ============================================================

fun downloadImageToDevice(
    context: Context,
    imageUrl: String?,
    title: String
) {

    if (imageUrl.isNullOrBlank()) {
        Toast.makeText(
            context,
            "Image URL is unavailable",
            Toast.LENGTH_SHORT
        ).show()

        return
    }

    try {

        val uri = Uri.parse(imageUrl)

        val request = DownloadManager.Request(uri).apply {

            setTitle("Downloading MarketUp Post Image")

            setDescription(
                "Saving AI creative image ($title) to Pictures..."
            )

            setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )

            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "MarketUp_${System.currentTimeMillis()}.jpg"
            )

            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }

        val manager = context.getSystemService(
            Context.DOWNLOAD_SERVICE
        ) as DownloadManager

        manager.enqueue(request)

        Toast.makeText(
            context,
            "Downloading image to Pictures folder...",
            Toast.LENGTH_SHORT
        ).show()

    } catch (e: Exception) {

        Toast.makeText(
            context,
            "Unable to start image download",
            Toast.LENGTH_SHORT
        ).show()
    }
}


// ============================================================
// SHARE POST
// ============================================================

fun sharePostToSystem(
    context: Context,
    caption: String,
    hashtags: List<String>
) {

    val fullShareText = buildString {

        append(caption)

        if (hashtags.isNotEmpty()) {
            append("\n\n")
            append(hashtags.joinToString(" "))
        }
    }

    val intent = Intent(Intent.ACTION_SEND).apply {

        type = "text/plain"

        putExtra(
            Intent.EXTRA_TEXT,
            fullShareText
        )
    }

    val chooser = Intent.createChooser(
        intent,
        "Share AI Post via"
    )

    context.startActivity(chooser)
}


// ============================================================
// CONTENT HEADER
// ============================================================

@Composable
fun ContentHeader() {

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            )
    ) {

        Text(
            text = "Content",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black
        )

        Text(
            text = "AI-generated posts & approval flow",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


// ============================================================
// CONTENT FILTER CHIPS
// ============================================================

@Composable
fun ContentFilterChips() {

    LazyRow(
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            bottom = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(
            listOf(
                "All",
                "Review",
                "Approved",
                "Scheduled"
            )
        ) { filter ->

            FilterChip(
                selected = filter == "All",
                onClick = {
                    // Filtering can be connected to UI state later.
                },
                label = {
                    Text(filter)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BrandOrange,
                    selectedLabelColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}


// ============================================================
// EMPTY CONTENT STATE
// ============================================================

@Composable
fun EmptyContentState() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(
                horizontal = 48.dp
            )
        ) {

            Surface(
                shape = CircleShape,
                color = BrandOrangeLight,
                modifier = Modifier.size(100.dp)
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = BrandOrange,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Text(
                text = "No content yet",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Tap the + button to generate your first AI post",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}