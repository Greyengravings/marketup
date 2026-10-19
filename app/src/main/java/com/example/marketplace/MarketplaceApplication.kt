package com.example.marketplace

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.example.marketplace.data.local.TokenManager
import com.example.marketplace.data.remote.NetworkModule
import com.example.marketplace.data.repository.AuthRepository
import com.example.marketplace.data.repository.BusinessRepository
import com.example.marketplace.data.repository.PostRepository

class MarketplaceApplication : Application(), ImageLoaderFactory {

    lateinit var tokenManager: TokenManager
    lateinit var authRepository: AuthRepository
    lateinit var businessRepository: BusinessRepository
    lateinit var postRepository: PostRepository

    override fun onCreate() {
        super.onCreate()
        
        tokenManager = TokenManager(this)
        val api = NetworkModule.provideMarketplaceApi(tokenManager)
        
        authRepository = AuthRepository(api, tokenManager)
        businessRepository = BusinessRepository(api)
        postRepository = PostRepository(api)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }
}
