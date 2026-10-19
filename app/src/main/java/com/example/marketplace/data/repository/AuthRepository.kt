package com.example.marketplace.data.repository

import com.example.marketplace.data.local.TokenManager
import com.example.marketplace.data.models.LoginRequest
import com.example.marketplace.data.models.SignupRequest
import com.example.marketplace.data.remote.MarketplaceApi

class AuthRepository(
    private val api: MarketplaceApi,
    private val tokenManager: TokenManager
) {
    suspend fun signup(request: SignupRequest): Result<Unit> {
        return try {
            val response = api.signup(request)
            tokenManager.saveTokens(response.accessToken, response.refreshToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(request: LoginRequest): Result<Unit> {
        return try {
            val response = api.login(request)
            tokenManager.saveTokens(response.accessToken, response.refreshToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        tokenManager.clearTokens()
    }
}
