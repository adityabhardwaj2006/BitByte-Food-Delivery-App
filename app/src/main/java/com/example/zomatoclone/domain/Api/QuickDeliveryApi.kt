package com.example.zomatoclone.domain.Api

import com.example.zomatoclone.data.models.QuickScreenItems
import retrofit2.http.GET

interface QuickDeliveryApi {

    @GET("v3/b/69bd81aec3097a1dd5434ac1?meta=false")
    suspend fun getQuickDeliveryItems() : List<QuickScreenItems>

}