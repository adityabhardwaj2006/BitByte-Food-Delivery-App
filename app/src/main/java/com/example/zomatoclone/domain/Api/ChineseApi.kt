package com.example.zomatoclone.domain.Api

import com.example.zomatoclone.data.models.ChineseFood
import retrofit2.http.GET

interface ChineseApi {

    @GET("v3/b/69833ebaae596e708f109510?meta=false")
    suspend fun getChineseFood(): List<ChineseFood>


}