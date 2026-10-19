package com.example.marketplace.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String = "bearer"
)

@Serializable
data class RefreshRequest(
    @SerialName("refresh_token") val refreshToken: String
)

@Serializable
data class BusinessCreate(
    @SerialName("business_name") val businessName: String,
    @SerialName("owner_name") val ownerName: String? = null,
    val sector: String,
    @SerialName("sub_sector") val subSector: String? = null,
    val description: String? = null,
    val address: String? = null,
    val city: String? = null,
    val phone: String? = null,
    val whatsapp: String? = null,
    val email: String? = null,
    @SerialName("brand_colors") val brandColors: List<String> = emptyList(),
    @SerialName("brand_personality") val brandPersonality: String? = null,
    @SerialName("tone_of_voice") val toneOfVoice: String? = null,
    @SerialName("target_audience") val targetAudience: String? = null,
    @SerialName("preferred_cta") val preferredCta: String? = null,
    @SerialName("social_handles") val socialHandles: Map<String, String> = emptyMap(),
    val website: String? = null
)

@Serializable
data class BusinessResponse(
    val id: String,
    @SerialName("business_name") val businessName: String,
    @SerialName("owner_name") val ownerName: String? = null,
    val sector: String,
    @SerialName("sub_sector") val subSector: String? = null,
    val description: String? = null,
    val address: String? = null,
    val city: String? = null,
    val phone: String? = null,
    val whatsapp: String? = null,
    val email: String? = null,
    @SerialName("brand_colors") val brandColors: List<String> = emptyList(),
    @SerialName("brand_personality") val brandPersonality: String? = null,
    @SerialName("tone_of_voice") val toneOfVoice: String? = null,
    @SerialName("target_audience") val targetAudience: String? = null,
    @SerialName("preferred_cta") val preferredCta: String? = null,
    @SerialName("social_handles") val socialHandles: Map<String, String> = emptyMap(),
    val website: String? = null,
    @SerialName("logo_url") val logoUrl: String? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class GenerateContentRequest(
    val occasion: String? = null,
    val postType: String? = null,
    val tone: String? = null,
    val goal: String? = null,
    val includeHashtags: Boolean = true,
    val includeCta: Boolean = true,
    val includeImagePrompt: Boolean = true,
    val includeReelIdea: Boolean = false
)

@Serializable
data class PostResponse(
    val id: String,
    @SerialName("business_id") val businessId: String,
    val occasion: String? = null,
    val caption: String,
    val hashtags: List<String>,
    val cta: String,
    @SerialName("image_prompt") val imagePrompt: String,
    @SerialName("generated_image") val generatedImage: String? = null,
    @SerialName("reel_idea") val reelIdea: String? = null,
    @SerialName("carousel_slides") val carouselSlides: List<String> = emptyList(),
    val status: String,
    @SerialName("ai_model_used") val aiModelUsed: String,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class PostActionResponse(
    val id: String,
    val status: String
)

@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    val status: String
)
