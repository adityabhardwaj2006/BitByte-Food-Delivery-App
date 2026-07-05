package com.example.zomatoclone.data.Api

import com.example.zomatoclone.data.models.Biryani
import retrofit2.http.GET

interface BiryaniApi {
    @GET("v3/b/69949f5943b1c97be98656ca?meta=false")
    suspend fun getBiryaniList() : List<Biryani>
}