package com.example.zomatoclone.data.Api

import com.example.zomatoclone.data.models.PizzaInfo
import retrofit2.http.GET


interface RestaurantApi {

    @GET("v3/b/697fb6adae596e708f09c789?meta=false")
    suspend fun getPizza(): List<PizzaInfo>

}
