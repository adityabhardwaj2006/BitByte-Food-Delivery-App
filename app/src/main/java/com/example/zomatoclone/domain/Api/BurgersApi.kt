package com.example.zomatoclone.domain.Api

import com.example.zomatoclone.data.models.Burger
import retrofit2.http.GET

interface BurgersApi {
    @GET("v3/b/69949726d0ea881f40c1a420?meta=false")
    suspend fun getBurgerList() : List<Burger>
}
