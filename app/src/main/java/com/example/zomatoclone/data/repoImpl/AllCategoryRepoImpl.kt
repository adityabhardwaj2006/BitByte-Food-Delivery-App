package com.example.zomatoclone.data.repoImpl

import com.example.zomatoclone.data.models.AllCategoryItem
import com.example.zomatoclone.domain.Api.AllCategoryApi
import com.example.zomatoclone.domain.repo.AllCategoryRepo
import javax.inject.Inject

class AllCategoryRepoImpl @Inject constructor(private val api : AllCategoryApi): AllCategoryRepo {

    override suspend fun getAllCategory(): List<AllCategoryItem> {
        return api.getAllCategory()
    }

}