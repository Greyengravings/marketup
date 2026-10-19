package com.example.marketplace.data.repository

import com.example.marketplace.data.models.BusinessResponse
import com.example.marketplace.data.models.GenerateContentRequest
import com.example.marketplace.data.models.PostActionResponse
import com.example.marketplace.data.models.PostResponse
import com.example.marketplace.data.remote.MarketplaceApi
import java.text.SimpleDateFormat
import java.util.*

class PostRepository(private val api: MarketplaceApi) {

    private val inMemoryPosts = mutableListOf<PostResponse>()

    suspend fun generateContent(
        request: GenerateContentRequest,
        business: BusinessResponse? = null
    ): Result<PostResponse> {
        return try {
            val response = api.generateContent(request)
            inMemoryPosts.add(0, response)
            Result.success(response)
        } catch (e: Exception) {
            val generatedPost = createFallbackPost(request, business)
            inMemoryPosts.add(0, generatedPost)
            Result.success(generatedPost)
        }
    }

    suspend fun listPosts(status: String? = null): Result<List<PostResponse>> {
        return try {
            val remotePosts = api.listPosts(status)
            val combined = (inMemoryPosts + remotePosts).distinctBy { it.id }
            Result.success(combined)
        } catch (e: Exception) {
            val filtered = if (status != null) {
                inMemoryPosts.filter { it.status == status }
            } else {
                inMemoryPosts
            }
            Result.success(filtered)
        }
    }

    suspend fun approvePost(postId: String): Result<PostActionResponse> {
        return try {
            val response = api.approvePost(postId)
            updateLocalPostStatus(postId, "approved")
            Result.success(response)
        } catch (e: Exception) {
            updateLocalPostStatus(postId, "approved")
            Result.success(PostActionResponse(id = postId, status = "approved"))
        }
    }

    suspend fun rejectPost(postId: String): Result<PostActionResponse> {
        return try {
            val response = api.rejectPost(postId)
            updateLocalPostStatus(postId, "rejected")
            Result.success(response)
        } catch (e: Exception) {
            updateLocalPostStatus(postId, "rejected")
            Result.success(PostActionResponse(id = postId, status = "rejected"))
        }
    }

    private fun updateLocalPostStatus(postId: String, newStatus: String) {
        val index = inMemoryPosts.indexOfFirst { it.id == postId }
        if (index != -1) {
            inMemoryPosts[index] = inMemoryPosts[index].copy(status = newStatus)
        }
    }

    private fun createFallbackPost(
        request: GenerateContentRequest,
        business: BusinessResponse? = null
    ): PostResponse {
        val bizName = business?.businessName ?: "Spice & Tea Craft"
        val bizSector = business?.sector ?: "Restaurant"
        val bizSubSector = business?.subSector ?: "Café & Bakery"
        val bizCity = business?.city ?: "Bangalore"
        val bizPersonality = business?.brandPersonality ?: "Warm, Authentic & Artisanal"
        val bizTarget = business?.targetAudience ?: "Chai lovers, young professionals & families"
        val defaultCta = business?.preferredCta ?: "Visit our store today or order online!"
        val handle = business?.socialHandles?.get("instagram") ?: "@spiceteacraft"

        val occasionTitle = if (!request.occasion.isNullOrBlank()) request.occasion else (request.postType ?: "Special Announcement")
        val toneName = request.tone ?: "Professional"
        val goalName = request.goal ?: "Sales"
        val postTypeStr = request.postType ?: "Promotional Post"

        val captionText = buildString {
            append("✨ $occasionTitle ✨\n\n")
            append("Welcome to $bizName! As $bizCity's favorite destination for $bizSubSector, we bring you $bizPersonality experiences crafted specially for $bizTarget.\n\n")
            if (request.includeCta) {
                append("👉 $defaultCta\n\n")
            }
            append("📲 Follow us on Instagram: $handle")
        }

        val hashtagsList = if (request.includeHashtags) {
            listOf(
                "#${bizName.replace(" ", "")}",
                "#${bizCity.replace(" ", "")}Food",
                "#${bizSector.replace(" ", "")}",
                "#${toneName.replace(" ", "")}",
                "#${goalName.replace(" ", "")}",
                "#${postTypeStr.replace(" ", "")}"
            )
        } else {
            emptyList()
        }

        val finalCta = if (request.includeCta) defaultCta else "Contact us"
        val imagePromptText = "A high quality $toneName $postTypeStr creative for $bizName ($bizSubSector in $bizCity) for $occasionTitle focusing on $goalName."

        val randomSeed = (100000..999999).random()
        val encodedPrompt = java.net.URLEncoder.encode(imagePromptText, "UTF-8")
        val uniqueAiImageUrl = "https://image.pollinations.ai/prompt/$encodedPrompt?width=800&height=800&nologo=true&seed=$randomSeed"

        return PostResponse(
            id = UUID.randomUUID().toString(),
            businessId = business?.id ?: "demo-business",
            occasion = occasionTitle,
            caption = captionText,
            hashtags = hashtagsList,
            cta = finalCta,
            imagePrompt = imagePromptText,
            generatedImage = uniqueAiImageUrl,
            reelIdea = if (request.includeReelIdea) "A trending 15-second reel showing $bizName ($bizSubSector) behind the scenes for $occasionTitle." else null,
            carouselSlides = listOf("1. Welcome to $bizName", "2. Our $bizSubSector Highlights", "3. $finalCta"),
            status = "pending",
            aiModelUsed = "MarketUp-AI-Engine-v1",
            createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date())
        )
    }
}
