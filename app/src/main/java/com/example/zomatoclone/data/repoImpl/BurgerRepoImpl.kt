package com.example.zomatoclone.data.repoImpl

import com.example.zomatoclone.data.models.Burger
import com.example.zomatoclone.data.Api.BurgersApi
import com.example.zomatoclone.domain.repo.BurgerRepo
import javax.inject.Inject

class BurgerRepoImpl @Inject constructor(
    private val api : BurgersApi) : BurgerRepo{

    override suspend fun getBurgerList(): List<Burger> {
        return api.getBurgerList()
    }


}