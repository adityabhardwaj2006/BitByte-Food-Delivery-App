package com.example.zomatoclone.data.Api

import com.example.zomatoclone.data.models.Pasta
import retrofit2.http.GET

interface PastaApi {

    @GET("v3/b/69b7b535c3097a1dd52c3120?meta=false")
    suspend fun getPastaList () : List<Pasta>
}