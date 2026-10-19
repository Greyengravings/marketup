package com.example.marketplace.data.repository

import com.example.marketplace.data.models.BusinessCreate
import com.example.marketplace.data.models.BusinessResponse
import com.example.marketplace.data.remote.MarketplaceApi

class BusinessRepository(private val api: MarketplaceApi) {

    var activeBusinessProfile: BusinessResponse = BusinessResponse(
        id = "demo-business",
        businessName = "Spice & Tea Craft",
        ownerName = "Hitesh Nihalani",
        sector = "Restaurant",
        subSector = "Café & Bakery",
        description = "Artisanal handcrafted teas, fresh sourdough breads, and authentic Indian fusion snacks.",
        address = "12th Main Road, Indiranagar",
        city = "Bangalore",
        phone = "+91 98765 43210",
        whatsapp = "+91 98765 43210",
        email = "contact@spiceteacraft.com",
        brandColors = listOf("#F0793D", "#6B4226"),
        brandPersonality = "Warm, Authentic, Artisanal & Welcoming",
        toneOfVoice = "Friendly & Premium",
        targetAudience = "Chai lovers, young professionals, college students and families",
        preferredCta = "Visit our Indiranagar outlet or order on WhatsApp!",
        socialHandles = mapOf("instagram" to "@spiceteacraft", "facebook" to "spiceteacraft"),
        createdAt = ""
    )

    suspend fun createBusiness(request: BusinessCreate): Result<BusinessResponse> {
        return try {
            val response = api.createBusiness(request)
            activeBusinessProfile = response
            Result.success(response)
        } catch (e: Exception) {
            val localProfile = BusinessResponse(
                id = "demo-business",
                businessName = request.businessName,
                ownerName = request.ownerName,
                sector = request.sector,
                subSector = request.subSector,
                description = request.description,
                address = request.address,
                city = request.city,
                phone = request.phone,
                whatsapp = request.whatsapp,
                email = request.email,
                brandColors = request.brandColors,
                brandPersonality = request.brandPersonality,
                toneOfVoice = request.toneOfVoice,
                targetAudience = request.targetAudience,
                preferredCta = request.preferredCta,
                socialHandles = request.socialHandles,
                website = request.website,
                createdAt = ""
            )
            activeBusinessProfile = localProfile
            Result.success(localProfile)
        }
    }

    suspend fun getMyBusiness(): Result<BusinessResponse> {
        return try {
            val response = api.getMyBusiness()
            activeBusinessProfile = response
            Result.success(response)
        } catch (e: Exception) {
            Result.success(activeBusinessProfile)
        }
    }

    suspend fun updateBusiness(request: BusinessCreate): Result<BusinessResponse> {
        return try {
            val response = api.updateMyBusiness(request)
            activeBusinessProfile = response
            Result.success(response)
        } catch (e: Exception) {
            activeBusinessProfile = activeBusinessProfile.copy(
                businessName = request.businessName,
                sector = request.sector,
                subSector = request.subSector,
                description = request.description,
                city = request.city,
                toneOfVoice = request.toneOfVoice,
                targetAudience = request.targetAudience,
                preferredCta = request.preferredCta,
                socialHandles = request.socialHandles
            )
            Result.success(activeBusinessProfile)
        }
    }
}
