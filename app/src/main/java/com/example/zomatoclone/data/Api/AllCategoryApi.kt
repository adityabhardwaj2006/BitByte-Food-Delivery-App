package com.example.zomatoclone.data.Api

import com.example.zomatoclone.data.models.AllCategoryItem
import retrofit2.http.GET

interface AllCategoryApi {

    @GET("v3/b/698398e6ae596e708f116f22?meta=false")
    suspend fun getAllCategory() : List<AllCategoryItem>

}