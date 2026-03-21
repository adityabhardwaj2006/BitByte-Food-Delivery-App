package com.example.zomatoclone.data.repoImpl

import com.example.zomatoclone.data.models.QuickScreenItems
import com.example.zomatoclone.domain.Api.QuickDeliveryApi
import com.example.zomatoclone.domain.repo.QuickDeliveryRepo
import com.example.zomatoclone.domain.repo.Repo
import javax.inject.Inject

class QuickDeliveryRepoImpl @Inject constructor(
    private val api : QuickDeliveryApi
) : QuickDeliveryRepo {
    override suspend fun getQuickDeliveryItems(): List<QuickScreenItems> {
        return api.getQuickDeliveryItems()
    }


}