package com.example.zomatoclone.data.repoImpl

import com.example.zomatoclone.data.models.Rolls
import com.example.zomatoclone.domain.Api.RollsApi
import com.example.zomatoclone.domain.repo.Repo
import com.example.zomatoclone.domain.repo.RollsRepo
import javax.inject.Inject

class RollsRepoImpl @Inject constructor(
    private val api : RollsApi
) : RollsRepo{
    override suspend fun getRollsList(): List<Rolls> {
        return api.getRollList()
    }
}