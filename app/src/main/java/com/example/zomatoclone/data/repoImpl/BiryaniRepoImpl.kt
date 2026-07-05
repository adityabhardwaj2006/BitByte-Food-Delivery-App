package com.example.zomatoclone.data.repoImpl

import com.example.zomatoclone.data.models.Biryani
import com.example.zomatoclone.data.Api.BiryaniApi
import com.example.zomatoclone.domain.repo.BiryaniRepo
import javax.inject.Inject

class BiryaniRepoImpl @Inject constructor(
    private val api : BiryaniApi) : BiryaniRepo {

    override suspend fun getBiryaniList(): List<Biryani> {
        return api.getBiryaniList()
    }

}