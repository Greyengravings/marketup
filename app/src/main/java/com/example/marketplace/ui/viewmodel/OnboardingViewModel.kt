package com.example.marketplace.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.marketplace.data.models.BusinessCreate
import com.example.marketplace.data.repository.BusinessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repository: BusinessRepository) : ViewModel() {

    // Simple state to hold onboarding data across screens
    var businessName by mutableStateOf("")
    var description by mutableStateOf("")
    var sector by mutableStateOf("Retail")
    var subSector by mutableStateOf("")
    var address by mutableStateOf("")
    var city by mutableStateOf("")
    var phone by mutableStateOf("")
    var whatsapp by mutableStateOf("")
    var website by mutableStateOf("")
    var toneOfVoice by mutableStateOf("Professional & Friendly")
    var targetAudience by mutableStateOf("")
    var brandPersonality by mutableStateOf("")
    var preferredCta by mutableStateOf("")
    var instagramHandle by mutableStateOf("")
    var facebookHandle by mutableStateOf("")

    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun submitOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = OnboardingUiState.Loading
            val request = BusinessCreate(
                businessName = businessName,
                sector = sector,
                subSector = subSector,
                description = description,
                address = address,
                city = city,
                phone = phone,
                whatsapp = whatsapp,
                website = website,
                brandColors = listOf("#F0793D"),
                brandPersonality = brandPersonality,
                toneOfVoice = toneOfVoice,
                targetAudience = targetAudience,
                preferredCta = preferredCta,
                socialHandles = mapOf(
                    "instagram" to instagramHandle,
                    "facebook" to facebookHandle
                )
            )
            repository.createBusiness(request).onSuccess {
                _uiState.value = OnboardingUiState.Success
                onSuccess()
            }.onFailure {
                _uiState.value = OnboardingUiState.Error(it.message ?: "Failed to create business")
            }
        }
    }

    sealed class OnboardingUiState {
        object Idle : OnboardingUiState()
        object Loading : OnboardingUiState()
        object Success : OnboardingUiState()
        data class Error(val message: String) : OnboardingUiState()
    }

    class Factory(private val repository: BusinessRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return OnboardingViewModel(repository) as T
        }
    }
}
