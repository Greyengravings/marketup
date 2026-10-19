package com.example.marketplace.data.remote

import com.example.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface MarketplaceApi {

    // --- Auth ---
    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): TokenResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): TokenResponse

    @GET("auth/me")
    suspend fun getCurrentUser(): UserResponse

    // --- Business ---
    @POST("businesses")
    suspend fun createBusiness(@Body request: BusinessCreate): BusinessResponse

    @GET("businesses/me")
    suspend fun getMyBusiness(): BusinessResponse

    @PATCH("businesses/me")
    suspend fun updateMyBusiness(@Body request: BusinessCreate): BusinessResponse

    // --- AI Content ---
    @POST("ai/generate")
    suspend fun generateContent(@Body request: GenerateContentRequest): PostResponse

    @GET("businesses/me/posts")
    suspend fun listPosts(@Query("status_filter") status: String? = null): List<PostResponse>

    @POST("posts/{post_id}/approve")
    suspend fun approvePost(@Path("post_id") postId: String): PostActionResponse

    @POST("posts/{post_id}/reject")
    suspend fun rejectPost(@Path("post_id") postId: String): PostActionResponse

    companion object {
        const val BASE_URL = "http://10.157.137.1:8000/"
    }
}
