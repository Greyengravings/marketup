package com.example.marketplace.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.marketplace.data.models.BusinessResponse
import com.example.marketplace.data.models.PostResponse
import com.example.marketplace.data.repository.BusinessRepository
import com.example.marketplace.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val businessRepository: BusinessRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            
            val businessResult = businessRepository.getMyBusiness()
            val postsResult = postRepository.listPosts()
            
            // Mock default business for bypass mode
            val defaultBusiness = BusinessResponse(
                id = "demo-business",
                businessName = "Demo Business",
                sector = "Retail",
                createdAt = ""
            )

            val business = businessResult.getOrDefault(defaultBusiness)
            val posts = postsResult.getOrDefault(emptyList())

            _uiState.value = HomeUiState.Success(
                business = business,
                recentPosts = posts
            )
        }
    }

    sealed class HomeUiState {
        object Loading : HomeUiState()
        data class Success(
            val business: BusinessResponse,
            val recentPosts: List<PostResponse>
        ) : HomeUiState()
        data class Error(val message: String) : HomeUiState()
    }

    class Factory(
        private val businessRepository: BusinessRepository,
        private val postRepository: PostRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(businessRepository, postRepository) as T
        }
    }
}
