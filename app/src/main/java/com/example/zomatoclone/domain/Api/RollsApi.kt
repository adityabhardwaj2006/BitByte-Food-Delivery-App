package com.example.zomatoclone.domain.Api

import com.example.zomatoclone.data.models.Rolls
import retrofit2.http.GET

interface RollsApi {

    @GET("v3/b/69980191d0ea881f40c8cda8?meta=false")
    suspend fun getRollList() : List<Rolls>
}

