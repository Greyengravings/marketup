package com.example.marketplace.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketplace.ui.theme.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import com.example.marketplace.ui.viewmodel.ContentViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun GenerateContentSheet(onClose: () -> Unit, viewModel: ContentViewModel) {
    var occasion by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Post") }
    var selectedTone by remember { mutableStateOf("Professional") }
    var selectedGoal by remember { mutableStateOf("Brand Awareness") }
    
    var includeHashtags by remember { mutableStateOf(true) }
    var includeCta by remember { mutableStateOf(true) }
    var includeImagePrompt by remember { mutableStateOf(true) }
    var includeReelIdea by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Generate content", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            Surface(
                shape = CircleShape,
                color = Grey100,
                modifier = Modifier.size(40.dp),
                onClick = onClose
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), color = Grey100)
        
        Text("WHAT SHOULD WE CREATE?", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = occasion,
            onValueChange = { occasion = it },
            placeholder = { Text("e.g. Diwali launch of our new masala chai blend, festive vibe", color = Grey500) },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Grey200, focusedBorderColor = BrandOrange)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("POST TYPE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listOf("Daily Post", "Promotional Post", "Festival Wishes", "Product Highlight", "Service Highlight", "Customer Review")) { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { selectedType = type },
                    label = { Text(type) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BrandOrange, selectedLabelColor = Color.White),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        Text("TONE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listOf("Professional", "Friendly", "Luxurious", "Bold", "Witty", "Empathetic")) { tone ->
                FilterChip(
                    selected = selectedTone == tone,
                    onClick = { selectedTone = tone },
                    label = { Text(tone) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BrandOrange, selectedLabelColor = Color.White),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("GOAL", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listOf("Brand Awareness", "Sales", "Engagement", "Education", "Announcement")) { goal ->
                FilterChip(
                    selected = selectedGoal == goal,
                    onClick = { selectedGoal = goal },
                    label = { Text(goal) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BrandOrange, selectedLabelColor = Color.White),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("OPTIONS", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OptionToggle("Include Hashtags", includeHashtags) { includeHashtags = it }
            OptionToggle("Include CTA", includeCta) { includeCta = it }
            OptionToggle("Include Image Prompt", includeImagePrompt) { includeImagePrompt = it }
            OptionToggle("Include Reel Idea", includeReelIdea) { includeReelIdea = it }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (uiState is ContentViewModel.ContentUiState.Error) {
            Text((uiState as ContentViewModel.ContentUiState.Error).message, color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = { 
                viewModel.generateContent(
                    occasion = occasion,
                    postType = selectedType,
                    tone = selectedTone,
                    goal = selectedGoal,
                    includeHashtags = includeHashtags,
                    includeCta = includeCta,
                    includeImagePrompt = includeImagePrompt,
                    includeReelIdea = includeReelIdea,
                    onSuccess = onClose
                )
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
            enabled = uiState !is ContentViewModel.ContentUiState.Generating
        ) {
            if (uiState is ContentViewModel.ContentUiState.Generating) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Generate Content", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
fun OptionToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Grey100
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = BrandOrange)
            )
        }
    }
}
