package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.data.models.IceCream

interface IceCreamRepo {
    suspend fun getIceCreamList () : List<IceCream>
}