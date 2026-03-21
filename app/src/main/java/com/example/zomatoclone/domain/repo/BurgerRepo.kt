package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.data.models.Burger

interface BurgerRepo {

    suspend fun getBurgerList() : List<Burger>

}