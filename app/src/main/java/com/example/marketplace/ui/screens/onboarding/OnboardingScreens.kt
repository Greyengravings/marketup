package com.example.marketplace.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketplace.ui.theme.*

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marketplace.MarketplaceApplication
import com.example.marketplace.data.models.LoginRequest
import com.example.marketplace.data.models.SignupRequest
import com.example.marketplace.ui.viewmodel.AuthViewModel
import com.example.marketplace.ui.viewmodel.OnboardingViewModel

@Composable
fun WelcomeScreen(onCreateAccount: () -> Unit, onLogin: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE49333).copy(alpha = 0.5f), BrandBlack),
                        startY = 0f
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(BrandOrange))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("MarketMaster AI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "Grow your business on autopilot.",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "AI-powered social posts, campaign strategy, creator marketplace & analytics — all in one place.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onCreateAccount,
                modifier = Modifier.fillMaxWidth().height(72.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
            ) {
                Text("Create free account", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = onLogin,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("I already have an account", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SignupScreen(onSignupSuccess: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val app = context.applicationContext as MarketplaceApplication
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory(app.authRepository))
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            OnboardingSimpleTopBar(onBack = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Text("Create your\naccount", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
            Text("Start automating your business social presence", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(48.dp))
            
            OnboardingTextField(label = "Full name", placeholder = "Your name", value = name, onValueChange = { name = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Email", placeholder = "you@company.com", value = email, onValueChange = { email = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Password", placeholder = "6+ characters", value = password, onValueChange = { password = it })
            
            Spacer(modifier = Modifier.height(48.dp))
            
            if (uiState is AuthViewModel.AuthUiState.Error) {
                Text((uiState as AuthViewModel.AuthUiState.Error).message, color = Color.Red)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { 
                    viewModel.signup(SignupRequest(email, password), onSignupSuccess)
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                enabled = uiState !is AuthViewModel.AuthUiState.Loading
            ) {
                if (uiState is AuthViewModel.AuthUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Create account", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onBack: () -> Unit, onGoToSignup: () -> Unit) {
    val context = LocalContext.current
    val app = context.applicationContext as MarketplaceApplication
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory(app.authRepository))

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            OnboardingSimpleTopBar(onBack = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Text("Welcome back", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
            Text("Sign in to your MarketMaster AI account", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(48.dp))
            
            OnboardingTextField(label = "Email", placeholder = "you@company.com", value = email, onValueChange = { email = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Password", placeholder = "Your password", value = password, onValueChange = { password = it })
            
            Spacer(modifier = Modifier.height(48.dp))
            
            if (uiState is AuthViewModel.AuthUiState.Error) {
                Text((uiState as AuthViewModel.AuthUiState.Error).message, color = Color.Red)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { 
                    viewModel.login(LoginRequest(email, password), onLoginSuccess)
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                enabled = uiState !is AuthViewModel.AuthUiState.Loading
            ) {
                if (uiState is AuthViewModel.AuthUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Sign In", style = MaterialTheme.typography.titleLarge)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account?", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onGoToSignup) {
                    Text("Sign up", color = BrandOrange, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun OnboardingSimpleTopBar(onBack: () -> Unit) {
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
            onClick = onBack
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.ChevronLeft, contentDescription = null)
            }
        }
    }
}

@Composable
fun BusinessTypeScreen(onContinue: () -> Unit, onBack: () -> Unit, viewModel: OnboardingViewModel) {
    Scaffold(
        containerColor = Color.White,
        topBar = { OnboardingTopBar(step = 1, onBack = onBack) },
        bottomBar = { OnboardingBottomBar(buttonText = "Continue", onClick = onContinue) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(24.dp)) {
            Text("What kind of business do you run?", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
            Text("We'll tailor AI content to your niche.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listOf("Retail", "Food", "Tech", "Beauty", "Service")) { type ->
                    val selected = viewModel.sector == type
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (selected) BrandOrangeLight else Grey100,
                        border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, BrandOrange) else null,
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        onClick = { viewModel.sector = type }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                when(type) {
                                    "Retail" -> Icons.Default.Storefront
                                    "Food" -> Icons.Default.Restaurant
                                    "Tech" -> Icons.Default.Computer
                                    "Beauty" -> Icons.Default.Face
                                    else -> Icons.Default.Category
                                },
                                contentDescription = null,
                                tint = if (selected) BrandOrange else Grey700,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(type, color = if (selected) BrandOrange else BrandBlack, fontWeight = FontWeight.Black, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BusinessDetailsScreen(onContinue: () -> Unit, onBack: () -> Unit, viewModel: OnboardingViewModel) {
    Scaffold(
        containerColor = Color.White,
        topBar = { OnboardingTopBar(step = 2, onBack = onBack) },
        bottomBar = { OnboardingBottomBar(buttonText = "Continue", onClick = onContinue) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text("Tell us about your business", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
            Text("The more we know, the better content we create.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(48.dp))
            
            OnboardingTextField(label = "Business name", placeholder = "e.g. Spice Garden Cafe", value = viewModel.businessName, onValueChange = { viewModel.businessName = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Sub-sector / Niche", placeholder = "e.g. Authentic South Indian food", value = viewModel.subSector, onValueChange = { viewModel.subSector = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Target Audience", placeholder = "e.g. Young professionals, foodies", value = viewModel.targetAudience, onValueChange = { viewModel.targetAudience = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Short description", placeholder = "What do you sell, who's it for?", isLarge = true, value = viewModel.description, onValueChange = { viewModel.description = it })
        }
    }
}

@Composable
fun ContactLocationScreen(onContinue: () -> Unit, onBack: () -> Unit, viewModel: OnboardingViewModel) {
    Scaffold(
        containerColor = Color.White,
        topBar = { OnboardingTopBar(step = 3, onBack = onBack) },
        bottomBar = { OnboardingBottomBar(buttonText = "Continue", onClick = onContinue) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text("Contact & location", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
            Text("Used for local targeting & customer trust.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(48.dp))
            
            OnboardingTextField(label = "City", placeholder = "e.g. Mumbai", value = viewModel.city, onValueChange = { viewModel.city = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Address", placeholder = "Full address (optional)", value = viewModel.address, onValueChange = { viewModel.address = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Phone", placeholder = "+91 90000 00000", value = viewModel.phone, onValueChange = { viewModel.phone = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "WhatsApp", placeholder = "+91 90000 00000", value = viewModel.whatsapp, onValueChange = { viewModel.whatsapp = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Instagram Handle", placeholder = "@username", value = viewModel.instagramHandle, onValueChange = { viewModel.instagramHandle = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Website (optional)", placeholder = "https://", value = viewModel.website, onValueChange = { viewModel.website = it })
        }
    }
}

@Composable
fun BrandVoiceScreen(onFinish: () -> Unit, onBack: () -> Unit, viewModel: OnboardingViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = { OnboardingTopBar(step = 4, onBack = onBack) },
        bottomBar = { 
            OnboardingBottomBar(
                buttonText = "Finish setup", 
                onClick = { viewModel.submitOnboarding(onFinish) },
                isLoading = uiState is OnboardingViewModel.OnboardingUiState.Loading
            ) 
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text("Brand personality & voice", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
            Text("How should your content sound and act?", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(32.dp))

            OnboardingTextField(label = "Brand Personality", placeholder = "e.g. Modern, Minimalist, Traditional", value = viewModel.brandPersonality, onValueChange = { viewModel.brandPersonality = it })
            Spacer(modifier = Modifier.height(24.dp))
            OnboardingTextField(label = "Preferred CTA", placeholder = "e.g. Call us now, Visit our website", value = viewModel.preferredCta, onValueChange = { viewModel.preferredCta = it })
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (uiState is OnboardingViewModel.OnboardingUiState.Error) {
                Text((uiState as OnboardingViewModel.OnboardingUiState.Error).message, color = Color.Red)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("TONE OF VOICE", fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            val voices = listOf("Professional & Friendly", "Playful & Bold", "Luxury & Elegant", "Youthful & Trendy")
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                voices.forEach { voice ->
                    val selected = viewModel.toneOfVoice == voice
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (selected) BrandOrangeLight else Grey100,
                        border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, BrandOrange) else null,
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        onClick = { viewModel.toneOfVoice = voice }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(voice, modifier = Modifier.weight(1f), color = if (selected) BrandOrange else BrandBlack, fontWeight = FontWeight.Black, fontSize = 18.sp)
                            RadioButton(
                                selected = selected,
                                onClick = { viewModel.toneOfVoice = voice },
                                colors = RadioButtonDefaults.colors(selectedColor = BrandOrange)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingTopBar(step: Int, onBack: () -> Unit) {
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
            onClick = onBack
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.ChevronLeft, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.width(24.dp))
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(4) { i ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(if (i < step) BrandOrange else Grey200)
                )
            }
        }
    }
}

@Composable
fun OnboardingBottomBar(buttonText: String, onClick: () -> Unit, isLoading: Boolean = false) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            HorizontalDivider(color = Grey100)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(buttonText, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun OnboardingTextField(
    label: String, 
    placeholder: String, 
    value: String, 
    onValueChange: (String) -> Unit, 
    isLarge: Boolean = false
) {
    Column {
        Text(label, fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Grey500) },
            modifier = Modifier.fillMaxWidth().then(if (isLarge) Modifier.height(140.dp) else Modifier),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Grey200,
                focusedBorderColor = BrandOrange
            )
        )
    }
}
