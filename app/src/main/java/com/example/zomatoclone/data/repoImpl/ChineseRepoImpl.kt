package com.example.zomatoclone.data.repoImpl

import com.example.zomatoclone.data.models.ChineseFood
import com.example.zomatoclone.domain.Api.ChineseApi
import com.example.zomatoclone.domain.repo.ChineseRepo
import javax.inject.Inject

class ChineseRepoImpl @Inject constructor(
    private var api : ChineseApi) : ChineseRepo {

    override suspend fun getChineseFood(): List<ChineseFood> {
        return api.getChineseFood()
    }


}