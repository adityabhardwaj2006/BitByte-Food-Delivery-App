package com.example.zomatoclone.data.Api

import com.example.zomatoclone.data.models.Sweets
import retrofit2.http.GET

interface SweetsApi {

    @GET("v3/b/69949fc443b1c97be98657d6?meta=false")
    suspend fun getSweetsList() : List<Sweets>

}