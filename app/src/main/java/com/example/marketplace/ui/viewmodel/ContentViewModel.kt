package com.example.marketplace.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.marketplace.data.models.GenerateContentRequest
import com.example.marketplace.data.models.PostResponse
import com.example.marketplace.data.repository.BusinessRepository
import com.example.marketplace.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContentViewModel(
    private val repository: PostRepository,
    private val businessRepository: BusinessRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow<ContentUiState>(ContentUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts(status: String? = null) {
        viewModelScope.launch {
            _uiState.value = ContentUiState.Loading
            repository.listPosts(status).onSuccess {
                _uiState.value = ContentUiState.Success(it)
            }.onFailure {
                // Fallback to empty post list when bypassing database
                _uiState.value = ContentUiState.Success(emptyList())
            }
        }
    }

    fun generateContent(
        occasion: String? = null,
        postType: String? = "Post",
        tone: String? = "Professional",
        goal: String? = "Brand Awareness",
        includeHashtags: Boolean = true,
        includeCta: Boolean = true,
        includeImagePrompt: Boolean = true,
        includeReelIdea: Boolean = false,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = ContentUiState.Generating
            val request = GenerateContentRequest(
                occasion = occasion,
                postType = postType,
                tone = tone,
                goal = goal,
                includeHashtags = includeHashtags,
                includeCta = includeCta,
                includeImagePrompt = includeImagePrompt,
                includeReelIdea = includeReelIdea
            )
            
            val activeProfile = businessRepository?.activeBusinessProfile

            repository.generateContent(request, activeProfile).onSuccess {
                loadPosts() // Refresh list
                onSuccess()
            }.onFailure {
                _uiState.value = ContentUiState.Error(it.message ?: "Generation failed")
            }
        }
    }

    fun approvePost(postId: String) {
        viewModelScope.launch {
            repository.approvePost(postId).onSuccess {
                loadPosts() // Refresh
            }
        }
    }

    fun rejectPost(postId: String) {
        viewModelScope.launch {
            repository.rejectPost(postId).onSuccess {
                loadPosts() // Refresh
            }
        }
    }

    sealed class ContentUiState {
        object Loading : ContentUiState()
        object Generating : ContentUiState()
        data class Success(val posts: List<PostResponse>) : ContentUiState()
        data class Error(val message: String) : ContentUiState()
    }

    class Factory(
        private val repository: PostRepository,
        private val businessRepository: BusinessRepository? = null
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ContentViewModel(repository, businessRepository) as T
        }
    }
}
