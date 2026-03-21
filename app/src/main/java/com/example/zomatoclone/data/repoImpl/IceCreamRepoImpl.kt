package com.example.zomatoclone.data.repoImpl

import com.example.zomatoclone.data.models.IceCream
import com.example.zomatoclone.domain.Api.IceCreamApi
import com.example.zomatoclone.domain.repo.IceCreamRepo
import com.example.zomatoclone.domain.repo.Repo
import javax.inject.Inject

class IceCreamRepoImpl @Inject constructor(
    private val api : IceCreamApi
): IceCreamRepo {
    override suspend fun getIceCreamList(): List<IceCream> {
        return api.getIceCreamList()
    }

}