package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.data.models.AllCategoryItem

interface AllCategoryRepo {

    suspend fun getAllCategory() : List<AllCategoryItem>

}