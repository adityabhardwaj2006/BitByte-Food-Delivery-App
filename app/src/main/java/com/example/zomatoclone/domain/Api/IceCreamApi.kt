package com.example.zomatoclone.domain.Api

import com.example.zomatoclone.data.models.IceCream
import retrofit2.http.GET

interface IceCreamApi {
    @GET("v3/b/69b7ba8ac3097a1dd52c4449?meta=false")
    suspend fun getIceCreamList () : List<IceCream>
}