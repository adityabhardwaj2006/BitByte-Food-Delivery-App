package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.data.models.PizzaInfo

interface PizzaRepo {

    suspend fun getPizzaData(): List<PizzaInfo>

}