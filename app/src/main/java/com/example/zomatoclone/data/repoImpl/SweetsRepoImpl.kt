package com.example.zomatoclone.data.repoImpl

import com.example.zomatoclone.data.models.Sweets
import com.example.zomatoclone.domain.Api.SweetsApi
import com.example.zomatoclone.domain.repo.SweetsRepo
import javax.inject.Inject

class SweetsRepoImpl @Inject constructor(
    private val api : SweetsApi
) : SweetsRepo {
    override suspend fun getSweetsList(): List<Sweets> {
        return api.getSweetsList()
    }
}