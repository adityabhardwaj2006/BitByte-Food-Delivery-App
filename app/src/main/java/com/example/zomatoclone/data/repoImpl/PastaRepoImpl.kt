package com.example.zomatoclone.data.repoImpl

import com.example.zomatoclone.data.models.Pasta
import com.example.zomatoclone.domain.Api.PastaApi
import com.example.zomatoclone.domain.repo.PastaRepo
import javax.inject.Inject

class PastaRepoImpl @Inject constructor(
    private val api : PastaApi
) : PastaRepo{
    override suspend fun getPastaList () : List<Pasta>{
        return api.getPastaList()
    }
}