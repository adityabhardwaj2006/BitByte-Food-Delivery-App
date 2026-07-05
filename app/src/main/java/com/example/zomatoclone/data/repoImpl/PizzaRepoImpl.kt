package com.example.zomatoclone.data.repoImpl

import com.example.zomatoclone.data.models.PizzaInfo
import com.example.zomatoclone.data.Api.RestaurantApi
import com.example.zomatoclone.domain.repo.PizzaRepo
import javax.inject.Inject

class PizzaRepoImpl @Inject constructor(
    private val restaurantApi: RestaurantApi): PizzaRepo {

    override suspend fun getPizzaData() : List<PizzaInfo> {
        return restaurantApi.getPizza()
    }

}